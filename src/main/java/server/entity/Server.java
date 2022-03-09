package server.entity;

import globalEntity.Message;
import globalEntity.User;
import server.control.Controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


public class Server implements PropertyChangeListener {
    private final Controller controller;
    private ArrayList<String> traffic = new ArrayList<>();
    private Buffer<Message> messageBuffer = new Buffer<>();
    private HashMap<User, Buffer<Message>> messageOnHold = new HashMap<>();
    private final HashMap<User, ClientHandler> loggedInUsers = new HashMap<>();
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private LocalDateTime date;
    private ServerSocket serverSocket;


    public Server(Controller controller, int port) {
        this.controller = controller;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Connection().start();
    }
    public void addToTraffic(String whatHappened, String who, LocalDateTime when) {
        String trafficInfo;
        trafficInfo = String.format(whatHappened, who, when.toString());
        traffic.add(trafficInfo);
    }
    public void messagesToSend(Message message) {
        messageBuffer.put(message);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void addListener() {

    }

    public void readFriends(String filename) { //läser vänlista
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            ArrayList<User> friends = new ArrayList<>();
            for(User friend : friends) {
                friend = (User) ois.readObject();
                friends.add(friend);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void createFriends(String filename) {
        ArrayList<User> friends = new ArrayList<>(); //ta in storlek på arraylist !
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            for(User friend : friends) {
                oos.writeObject(friend);
            }
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(globalEntity.User user) {
        return loggedInUsers.containsKey(user);
    }

    public void sendMessage(Message reply) {
        User receiver = reply.getReceiver();
        ClientHandler clientHandler = loggedInUsers.get(receiver);
        clientHandler.send(reply);
    }

    // private void updateListForAllContacts() {
    //     synchronized (loggedInUsers) {
    //         for(Map.Entry<User, ClientHandler> entry : loggedInUsers.entrySet()) {
    //             Message message = new Message.Builder()
    //                 .type(Message.CONTACTS)
    //                 .contacts(loggedInUsers.keySet().toArray(new User[0]))
    //                 .build();
    //             try {
    //                 entry.getValue().send(message);
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     }
    // }


    private class Connection extends Thread {
        public void run() {
            Socket socket = null;
            User user = null;
            System.out.println("Server startar");
            while (true) {
                try {
                    socket = serverSocket.accept();
                    new ClientHandler(controller, socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }
}
