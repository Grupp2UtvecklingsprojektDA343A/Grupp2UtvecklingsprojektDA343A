package server.controller;

import client.controller.Client;
import server.model.Buffer;
import sharedModel.Message;
import sharedModel.User;

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


public class Server implements PropertyChangeListener {
    ArrayList<String> traffic = new ArrayList<>();
    Buffer<Message> messageBuffer = new Buffer<>();
    ServerSocket serverSocket;
    HashMap<String, Message> messageOnHold = new HashMap<>();
    LocalDateTime date;
    HashMap<String, User> loggedInUsers = new HashMap<>();

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

    private class Connection extends Thread {
        public void run() {
            Socket socket = null;
            User user = null;
            System.out.println("Server startar");
            while (true) {
                try {
                    socket = serverSocket.accept();
                    new ClientHandler(socket, user);
                    //lägg till en lista av inloggade klienter.
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class ClientHandler extends Thread {
        private Socket socket;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public ClientHandler(Socket socket, User username) {
            this.socket = socket;
            loggedInUsers.put(username.getUsername(), username);

        }

        @Override
        public void run() {
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                oos = new ObjectOutputStream(socket.getOutputStream());
                while (true) {
                    Message message = (Message) ois.readObject();
                    message.setReceived(date.now());
                    messageBuffer.put(message);
                    if(message.getReceiver().getLoggedIn() == true)  {
                       oos.writeObject(messageBuffer.get());
                       oos.flush();
                    } else if (message.getReceiver().getLoggedIn() == false) {
                        messageOnHold.put(message.getReceiver().getUsername(), message);
                    }
                }
            } catch (IOException | ClassNotFoundException |InterruptedException e ) {
                e.printStackTrace();
            }


        }
    }
}
