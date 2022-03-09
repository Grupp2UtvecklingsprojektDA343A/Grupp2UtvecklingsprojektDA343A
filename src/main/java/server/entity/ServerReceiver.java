package server.entity;

import globalEntity.Message;
// import globalEntity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReceiver extends Thread {

    private ObjectInputStream ois;
    private Socket socket;
    private Message message;

    public ServerReceiver(Socket socket) {
        this.socket = socket;
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
                Message message = (Message) ois.readObject();
                switch(message.getType()) {
                    case Message.CONTACTS -> {}
                    case Message.TEXT -> {}
                    case Message.IMAGE -> {}
                    case Message.TEXT_AND_IMAGE -> {}
                    case Message.LOGIN ->  {
                        globalEntity.User sender = message.getSender();
                        if(userExists(sender)) {

                        }
                        // skicka tillbala om vi lan logga in eller inte?
                    }
                }
                setMessage(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
