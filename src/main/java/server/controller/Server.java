package server.controller;

import server.model.Buffer;
import sharedModel.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


public class Server {
    ArrayList<String> traffic = new ArrayList<>();
    Buffer<Message> messageBuffer = new Buffer<>();
    ServerSocket serverSocket;
    HashMap<String, Message> messageOnHold = new HashMap<>();
    LocalDateTime date;
    public Server(String ip, int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addToTraffic(String whatHappened, String who, LocalDateTime when) {
    }
    public void messagesToSend(Message message) {
        messageBuffer.put(message);
    }
    private class FirstThread extends Thread {
        public void run() {
            Socket socket = null;
            System.out.println("Server startar");
            while (true) {
                try {
                    socket = serverSocket.accept();
                    new ClientHandler(socket);
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

        public ClientHandler(Socket socket) {
            this.socket = socket;
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
