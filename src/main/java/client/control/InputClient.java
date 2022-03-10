package client.control;

import globalEntity.Message;
import globalEntity.User;

import java.io.IOException;
import java.io.ObjectInputStream;

public class InputClient extends Thread {
    private ObjectInputStream ois;
    private Message message;
    private Client client;
    public InputClient(Client client,ObjectInputStream ois) {
        this.ois = ois;
        this.client = client;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            try {
                message = (Message) ois.readObject();
                int type = message.getType();

                switch(type) {
                    case Message.CONTACTS -> {
                        client.showAllUsers(message.getContacts());
                    }

                    case Message.TEXT -> {
                        System.out.println("bara text");
                        // mainWindow.newImageMessage(icon,sender);
                        // String sender = String.valueOf(message.getSender());
                        // String guiMessage = message.getMessage();
                        // ImageIcon icon = message.getImage();
                    }

                    case Message.IMAGE -> {
                        System.out.println("bara bild");
                    }

                    case Message.TEXT_AND_IMAGE -> {
                        System.out.println("text och bild");
                    }

                    default -> {
                        System.err.println("FEL?");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(3);
            }
        }
    }

    public Message getMessage() {
        return message;
    }
}
