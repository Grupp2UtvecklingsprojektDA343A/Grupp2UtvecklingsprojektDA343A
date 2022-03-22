package server.entity;

import globalEntity.Message;
import server.control.Controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientHandler extends Thread {
    private final ServerReceiver serverReceiver;
    private final ServerSender serverSender;


    public ClientHandler(Controller controller, ObjectOutputStream oos, ObjectInputStream ois) {
        serverReceiver = new ServerReceiver(controller, ois);
        serverReceiver.start();
        serverSender = new ServerSender(oos);
        serverSender.start();
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
        serverReceiver.running = false;
        serverSender.interrupt();
        interrupt();
    }

}
