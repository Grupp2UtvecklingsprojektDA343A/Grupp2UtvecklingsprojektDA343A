package server.control;

import globalEntity.Message;
import globalEntity.User;
import server.boundary.TrafficLogGUI;
import server.entity.Server;
import server.entity.Traffic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Controller implements PropertyChangeListener {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Server server;
    private TrafficLogGUI trafficLogGUI;
    private ArrayList<Traffic> trafficList = new ArrayList<>();

    public Controller(){
        if (! Files.isDirectory(Paths.get("files/"))) {
            Path p = FileSystems.getDefault().getPath("files/");
            try {
                Files.createDirectory(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        server = new Server(this, 20008);
        server.addListener(this);
        trafficLogGUI = new TrafficLogGUI(server);
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        writeTrafficToFile(evt);
        readTrafficFile();
    }

    public synchronized boolean userExists(User user) {
        return server.userExists(user);
    }

    public void addToTraffic(String traffic){
        //serverUI.updateTraffic(traffic);
    }

    public void writeTrafficToFile(PropertyChangeEvent evt){
        String path = "files/traffic";
        try {
            FileOutputStream fos = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            Traffic traffic = (Traffic) evt.getNewValue();
            trafficList.add(traffic);
            oos.writeObject(trafficList);
            oos.flush();
            oos.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void readTrafficFile(){
        ArrayList<Traffic> traffic = new ArrayList<>();
        try {
            String path  = "files/traffic";
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
            traffic = (ArrayList<Traffic>) ois.readObject();
            trafficLogGUI.updateTraffic(traffic);
            ois.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendMessage(Message message, Traffic traffic){
        server.sendMessage(message, traffic);
    }

    public void disconnect(Message message) {
        createFriendList(message);
        server.disconnect(message);
    }

    public void createFriendList(Message message){
        User user = message.getSender();
        ArrayList<User> users = new ArrayList<>(message.getContacts().size());
        for (int i = 0; i < users.size(); i++){
            users.add(message.getContacts().get(i));
        }
        server.createFriendList(user, users);
    }

    public void notifyUser(Traffic traffic) {
        server.notifyReceived(traffic);
    }
}