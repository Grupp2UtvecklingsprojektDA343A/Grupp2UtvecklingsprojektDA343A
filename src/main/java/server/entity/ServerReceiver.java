package server.entity;

import globalEntity.Message;
import server.control.Controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDateTime;

public class ServerReceiver extends Thread {
    private final Controller controller;
    private final ObjectInputStream ois;
    public volatile boolean running = true;

    public ServerReceiver(Controller controller, ObjectInputStream ois) {
        this.controller = controller;
        this.ois = ois;
    }


    public synchronized void run() {
        try {
            while (running) {
                Message message = (Message) ois.readObject(); // klienten skickar något (alltid ett message)

                switch(message.getType()) {
                    case Message.CONTACTS -> {
                        controller.createFriendList(message);
                    }
                    case Message.TEXT -> {
                        Traffic traffic = new Traffic.Builder().text(message.getSender().getUsername()
                             + " sent a message to "
                             + message.getReceiver().getUsername()
                             + ".").eventTime(LocalDateTime.now()).build();
                        controller.sendMessage(message, traffic);
                    }
                    case Message.IMAGE -> {
                        Traffic traffic = new Traffic.Builder().text(message.getSender().getUsername()
                             + " sent an image to "
                             + message.getReceiver().getUsername()
                             + ".").eventTime(LocalDateTime.now()).build();
                        controller.sendMessage(message, traffic);
                    }
                    case Message.VIBRATE -> {
                        Traffic traffic = new Traffic.Builder().text(message.getSender().getUsername()
                             + " sent a vib to "
                             + message.getReceiver().getUsername()
                             + ".").eventTime(LocalDateTime.now()).build();
                        controller.sendMessage(message, traffic);
                    }
                    case Message.LOGOUT -> {
                        controller.disconnect(message);
                    }
                    case Message.NOTIFY_RECEIVED -> {
                        Traffic traffic = new Traffic.Builder().text(message.getReceiver().getUsername()
                            + " received a message. ")
                            .eventTime(message.getReceived()).build();
                        controller.notifyUser(traffic);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
