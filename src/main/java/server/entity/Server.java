package server.entity;

import entity.Message;
import entity.User;
import server.control.Controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    ArrayList<String> traffic = new ArrayList<>();
    Buffer<Message> messageBuffer = new Buffer<>();
    HashMap<User, Buffer<Message>> messageOnHold = new HashMap<>();
    final HashMap<User, ClientHandler> loggedInUsers = new HashMap<>();
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    LocalDateTime date;
    ServerSocket serverSocket;


    public Server(int port) {
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

    public void createFriends(String filename) {
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

    private void updateListForAllContacts() {
        synchronized (loggedInUsers) {
            for(Map.Entry<User, ClientHandler> entry : loggedInUsers.entrySet()) {
                Message message = new Message.Builder()
                    .type(Message.CONTACTS)
                    .contacts(loggedInUsers.keySet().toArray(new User[0]))
                    .build();
                try {
                    entry.getValue().send(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                    new ClientHandler(socket, user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    private class ClientHandler extends Thread {
        private Socket socket;
        private final ObjectInputStream ois;
        private final ObjectOutputStream oos;

        public ClientHandler(Socket socket, User username) throws IOException {
            this.socket = socket;
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            pcs.firePropertyChange("username", null, username);
            loggedInUsers.put(username, this);
            new Thread(() -> {
                updateListForAllContacts();
            }).start(); // ny tråd som säger till alla inloggade om ny person
            start();
        }

        @Override
        public void run() {
            try {
                ServerReceiver serverReceiver = new ServerReceiver(socket);
                serverReceiver.start();
                ServerSender serverSender = new ServerSender(socket);
                serverSender.start();

                while (true) {
                    Message message = serverReceiver.getMessage();
                    message.setReceived(LocalDateTime.now());
                    messageBuffer.put(message);
                    messageOnHold.put(messageBuffer.get().getReceiver(), messageBuffer);
                    if(message.getReceiver().getLoggedIn() && ! messageOnHold.isEmpty())  {
                       serverSender.send(messageBuffer.get());
                    }
                }
            } catch (InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }
}
