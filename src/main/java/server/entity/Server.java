package server.entity;

import entity.Message;
import entity.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    HashMap<String, Message> messageOnHold = new HashMap<>();
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
    private class ClientHandler extends Thread {
        private Socket socket;
        private final ObjectInputStream ois;
        private final ObjectOutputStream oos;

        public ClientHandler(Socket socket, User username) throws IOException {
            this.socket = socket;
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            loggedInUsers.put(username, this);
            new Thread(() -> {
                updateListForAllContacts();
            }).start(); // ny tråd som säger till alla inloggade om ny person
            start();
        }

        public void send(Message message) throws IOException {
            synchronized (oos) {
                oos.writeObject(message);
                oos.flush();
            }
        }

        public Message receive() throws IOException, ClassNotFoundException {
            synchronized (oos) {
                return (Message) ois.readObject();
            }
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
                    if(message.getReceiver().getLoggedIn() && ! messageBuffer.isEmpty())  {
                       serverSender.send(messageBuffer.get());
                    } else {
                        messageOnHold.put(message.getReceiver().getUsername(), message);
                    }
                }
            } catch (InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }
}
