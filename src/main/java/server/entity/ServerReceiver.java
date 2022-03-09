package server.entity;

import globalEntity.Message;
import server.control.Controller;
// import globalEntity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReceiver extends Thread {
    private final Controller controller;
    private final Socket socket;
    private ObjectInputStream ois;
    private Message message;

    public ServerReceiver(Controller controller, Socket socket) {
        this.socket = socket;
        this.controller = controller;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public synchronized void run() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
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
