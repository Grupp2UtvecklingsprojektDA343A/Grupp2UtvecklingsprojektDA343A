package server;

import Model.Buffer;
import Model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server {
    ArrayList<String> traffic = new ArrayList<>();
    Buffer<Message> messageBuffer = new Buffer<>();
    ServerSocket serverSocket;

    public Server(String ip, int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> void addToTraffic(T t) {
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
                    messageBuffer.put(message);
                    if(socket.getInputStream().read() != -1)  { // && socket.getInputStream().read() == null)  (
                       oos.writeObject(messageBuffer.get());
                       oos.flush();
                    } else {
                        //lagra meddelande
                    }

                }
            } catch (IOException | ClassNotFoundException |InterruptedException e ) {
                e.printStackTrace();
            }


        }
    }
}
