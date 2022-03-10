package server.entity;

import client.control.Client;
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
import java.util.Map;

public class Server implements PropertyChangeListener {
    private final Controller controller;
    private ArrayList<String> traffic = new ArrayList<>();
    private Buffer<Message> messageBuffer = new Buffer<>();
    private HashMap<User, Buffer<Message>> messageOnHold = new HashMap<>();
    private final HashMap<User, ClientHandler> loggedInUsers = new HashMap<>();
    private final HashMap<User, HashMap<User, ChatHandler>> chatHandler4 = new HashMap<>();
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private LocalDateTime date;
    private ServerSocket serverSocket;
    private Client client;


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

    public ArrayList<User> readFriendlist(User user) {
        String filename = String.format("files/"+user.getUsername()+"_friends.dat");
        ArrayList<User> friends = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            for(User friend : friends) {
                friend = (User) ois.readObject();
                friends.add(friend);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return friends;
    }

    public void createFriendlist(User user, ArrayList<User> users) {
        ArrayList<User> friends = new ArrayList<>(users.size());
        for(int i = 0; i < users.size(); i++) {
            friends.add(users.get(i));
        }
        String filename = String.format("files/"+user.getUsername()+"_friends.dat");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            for(User friend : friends) {
                oos.writeObject(friend);
            }
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setToOnline(User user){
        //client.setToOnline(user);
    }

    public void setToOffline(User user){
        client.setToOffline(user);
    }

    public boolean userExists(User user) {
        return loggedInUsers.containsKey(user);
    }

    public void sendMessage(Message reply) {
        User receiver = reply.getReceiver();
        ClientHandler clientHandler = loggedInUsers.get(receiver);
        clientHandler.send(reply);
    }

    public synchronized void addLoggedInUser(User user, ClientHandler clientHandler) {
        new Thread(() -> {
            loggedInUsers.put(user, clientHandler);
            updateListForAllContacts();
        }).start();
    }

    private void updateListForAllContacts() {
        synchronized (loggedInUsers) {
            for(Map.Entry<User, ClientHandler> entry : loggedInUsers.entrySet()) {
                Message message = new Message.Builder()
                    .type(Message.CONTACTS)
                    .contacts(loggedInUsers.keySet().toArray(new User[0]))
                    .build();

                entry.getValue().send(message);
            }
        }
    }
    private class Connection extends Thread {
        public void run() {
            Socket socket = null;
            User user = null;
            System.out.println("Server startar");
            while (true) {
                try {
                    socket = serverSocket.accept();
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    Message msg = (Message) ois.readObject();

                    if(msg.getType() == Message.LOGIN) {
                        new LoginHandler(socket, oos, ois).start();
                    } else {
                        // ny chatt

                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class LoginHandler extends Thread {
        private final Socket socket;
        private final ObjectOutputStream oos;
        private final ObjectInputStream ois;
        private ClientHandler clientHandler;

        public LoginHandler(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
            this.socket = socket;
            this.oos = oos;
            this.ois = ois;

        }

        @Override
        public void run() {
            clientHandler = null;
            try {
                while(clientHandler == null) {
                    User user = (User) ois.readObject();
                    Message reply;

                    if(!controller.userExists(user)) { // kan logga in
                        reply =  new Message.Builder().type(Message.LOGIN_SUCCESS).build();
                        System.out.println(reply.getType());
                        clientHandler = new ClientHandler(controller, socket, oos, ois);
                        addLoggedInUser(user, clientHandler);
                        clientHandler.start();
                        clientHandler.getServerSender().send(reply);
                    } else { // kan inte logga in
                        reply =  new Message.Builder().type(Message.LOGIN_FAILED).build();
                        oos.writeObject(reply);
                        oos.flush();
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            interrupt(); // Stoppa tråden
        }
        public void closeThread(PropertyChangeEvent evt){
            if(evt.getPropertyName().equals("true") && evt.getNewValue() instanceof User){
                if(loggedInUsers.containsKey(evt.getNewValue())){
                    clientHandler.closeThread(evt.getNewValue(), loggedInUsers.values());
                }
            } else{
                    System.out.println("user not found");
                }
        }
    }

    public void addListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    private class ChatHandler {

    }
}




