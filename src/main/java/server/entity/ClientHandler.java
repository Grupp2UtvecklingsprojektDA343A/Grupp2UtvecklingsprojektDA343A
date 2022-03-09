package server.entity;

import globalEntity.Message;
import server.control.Controller;

import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final Controller controller;
    private ServerReceiver serverReceiver;
    private ServerSender serverSender;


    public ClientHandler(Controller controller, Socket socket) {
        this.socket = socket;
        this.controller = controller;
        // pcs.firePropertyChange("username", null, username);
        start();
    }

    @Override
    public void run() {
        serverReceiver = new ServerReceiver(controller, socket);
        serverReceiver.start();
        serverSender = new ServerSender(controller, socket);
        serverSender.start();
    }

    public synchronized void send(Message message) {
        serverSender.send(message);
    }
}