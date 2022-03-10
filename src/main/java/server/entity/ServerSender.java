package server.entity;

import globalEntity.Message;
import server.control.Controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSender extends Thread implements PropertyChangeListener {
    private final Controller controller;
    private final ObjectOutputStream oos;
    private Socket socket;
    private Message message;

    public ServerSender(Controller controller, ObjectOutputStream oos) {
        this.controller = controller;
        this.oos = oos;
    }
    public synchronized void run() {
        // try {
        //     while(true) {
        //         oos.writeObject(message);
        //         oos.flush();
        //         wait();
        //     }
        // } catch (IOException | InterruptedException e) {
        //     e.printStackTrace();
        // }
    }
    public synchronized void send(Message message) {
        // this.message = message;
        // notifyAll();
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "login" -> {
            }
        }
    }
}
