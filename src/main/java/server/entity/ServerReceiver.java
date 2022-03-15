package server.entity;

import globalEntity.Message;
import globalEntity.User;
import server.control.Controller;
// import globalEntity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ServerReceiver extends Thread {
    private final Controller controller;
    private final ObjectInputStream ois;
    private Server server;
    private Message message;
    public volatile boolean running = true;

    public ServerReceiver(Controller controller, ObjectInputStream ois) {
        this.controller = controller;
        this.ois = ois;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public synchronized void run() {
        try {
            while (running) {
                Message message = (Message) ois.readObject(); // klienten skickar något (alltid ett message)
                User sender = message.getSender();
                User receiver = message.getReceiver();

                switch(message.getType()) {
                    case Message.CONTACTS -> {
                        controller.createFriendList(message);
                    }
                    case Message.TEXT -> {
                        String text = String.format("%s sent a message to %s.", sender, receiver);

                        Traffic traffic = new Traffic.Builder().text(text).serverRecieved(LocalDateTime.now()).build();
                        controller.sendMessage(message, traffic);
                    }
                    case Message.IMAGE -> {}
                    case Message.LOGOUT -> {
                        controller.disconnect(message);
                    }
                    case Message.TEXT_AND_IMAGE -> {}
                    case Message.NOTIFY_RECEIVED -> {
                        /*Traffic traffic = new Traffic.Builder().text(
                            message.getReceiver().getUsername()+ " received "
                            + message.getSender()
                            + "'s message at ").clientRecieved(message.getReceived()).build();
                        server.notifyReceived(traffic);*/
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
