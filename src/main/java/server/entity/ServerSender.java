package server.entity;

import entity.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSender extends Thread {
    private ObjectOutputStream oos;
    private Socket socket;
    private Message message;

    public ServerSender(Socket socket) {
        this.socket = socket;
    }

    public synchronized void run() {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            while(true) {
                oos.writeObject(message);
                oos.flush();
                wait();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void send(Message message) {
      this.message = message;
      notifyAll();
    }
}
