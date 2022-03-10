package server.entity;

import globalEntity.Message;
import server.control.Controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final Controller controller;
    // private ObjectInputStream ois;
    // private ObjectOutputStream oos;
    private ServerReceiver serverReceiver;
    private ServerSender serverSender;


    public ClientHandler(Controller controller, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.socket = socket;
        this.controller = controller;
        // this.ois = ois;
        // this.oos = oos;
        serverReceiver = new ServerReceiver(controller, ois);
        serverReceiver.start();
        serverSender = new ServerSender(controller, oos);
        serverSender.start();
        // pcs.firePropertyChange("username", null, username);
    }
    public synchronized void send(Message message) {
        serverSender.send(message);
    }
    public ServerSender getServerSender() {
        try {
            serverSender.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverSender;
    }
    public void closeThread(){
        serverReceiver.interrupt();
        serverSender.interrupt();
        interrupt();
    }

}