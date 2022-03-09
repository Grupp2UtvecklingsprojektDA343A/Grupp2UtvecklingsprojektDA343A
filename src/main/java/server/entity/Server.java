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
import java.util.Map;


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

    public boolean userExists(User user) {
        return loggedInUsers.containsKey(user);
    }

    public void sendMessage(Message reply) {
        User receiver = reply.getReceiver();
        ClientHandler clientHandler = loggedInUsers.get(receiver);
        clientHandler.send(reply);
    }

    public void addLoggedInUser(User user, ClientHandler clientHandler) {
        loggedInUsers.put(user, clientHandler);
        for(Map.Entry<User, ClientHandler> entry : loggedInUsers.entrySet()) {
            System.out.println("alla som finns: " + entry.getKey().getUsername());
        }
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
                    new LoginHandler(socket).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class LoginHandler extends Thread {
        private final Socket socket;

        public LoginHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ClientHandler clientHandler = null;
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                while(clientHandler == null) {
                    User user = (User) ois.readObject();
                    Message reply;
                    System.out.println(user.getUsername() + " vill logga in: " + user);

                    if(!controller.userExists(user)) { // kan logga in
                        System.out.println("kan logga in");
                        reply =  new Message.Builder().type(Message.LOGIN_SUCCESS).build();
                        clientHandler = new ClientHandler(controller, socket);
                        addLoggedInUser(user, clientHandler);
                        clientHandler.start();
                    } else { // kan inte logga in
                        System.out.println("kan inte logga in");
                        reply =  new Message.Builder().type(Message.LOGIN_FAILED).build();
                    }
                    oos.writeObject(reply);
                    oos.flush();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            interrupt(); // Stoppa tråden
        }
    }

    public void addListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }
}
