package server.entity;

import globalEntity.Message;
import server.control.Controller;
// import globalEntity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReceiver extends Thread {
    private final Controller controller;
    private final ObjectInputStream ois;
    private Message message;

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
            while (true) {
                Message message = (Message) ois.readObject(); // klienten skickar något (alltid ett message)
                controller.parseMessage(message);
                // setMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
