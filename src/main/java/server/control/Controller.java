package server.control;

import globalEntity.Message;
import globalEntity.User;
import server.boundary.ServerUI;
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
import java.util.ArrayList;
import java.util.Collections;

public class Controller implements PropertyChangeListener {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Server server;
    private ServerUI serverUI;
    private ArrayList<Traffic> trafficList = new ArrayList<>();
    private Traffic trafficArray[] = null;

    public Controller(){
        server = new Server(this, 20008);
        server.addListener(this);
        serverUI = new ServerUI(server);
    }

    public void addListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        pcs.firePropertyChange("message", null, evt);

        if(evt.getPropertyName().equals("loginOK")){
            //serverUI.updateTraffic(evt.getNewValue() + " just logged in.");
        } else if (evt.getPropertyName().equals("logout")){
            //serverUI.updateTraffic(evt.getNewValue() + " just logged out.");
        } else if (evt.getPropertyName().equals("sent")){
            trafficToList();
            logTraffic(evt);
            /*
            logTraffic(evt);
            Traffic traffic = (Traffic) evt.getNewValue();
            serverUI.updateTraffic(traffic.getText());
             */
            //serverUI.updateTraffic((String) evt.getNewValue());
        } else if (evt.getPropertyName().equals("loginFail")){
            //serverUI.updateTraffic(evt.getNewValue() + " failed to login.");
        } else if (evt.getPropertyName().equals("receivedByUser")){
            trafficToList();
            logTraffic(evt);
        }
    }

    public synchronized boolean userExists(User user) {
        return server.userExists(user);
    }

    public void addToTraffic(String traffic){
        //serverUI.updateTraffic(traffic);
    }

    public void logTraffic(PropertyChangeEvent evt){
        String path = "files//traffic";
        try {
            FileOutputStream fos = new FileOutputStream(path);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            Traffic traffic = (Traffic) evt.getNewValue();

            trafficList.add(traffic);
            oos.writeObject(trafficList);
            oos.flush();
            oos.close();

            for (int i = 0; i < this.trafficList.size(); i++) {
                System.out.println(this.trafficList.get(i).getText());
            }

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void trafficToList(){

        try {
            String path = "files//traffic";
            FileInputStream fis = new FileInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            trafficArray = (Traffic[]) ois.readObject();
            serverUI.updateTraffic(trafficArray);

        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        for (int i = 0; i < trafficArray.length; i++) {
            System.out.println(trafficArray[i]);
        }
        System.out.println();
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
        Collections.copy(users, message.getContacts());
        server.createFriendList(user, users);
    }
}