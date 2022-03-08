package client.control;

import entity.Message;
import entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;

public class InputClient extends Thread {
    private ObjectInputStream ois;
    private Message message;
    public InputClient(ObjectInputStream ois) {
        this.ois = ois;
    }

    @Override
    public void run() {
        System.out.println("yesy ");
        try {
            message = (Message) ois.readObject();
            int type = message.getType();

            switch(type) {
                case Message.CONTACTS -> {
                    User[] loggedInUsers = message.getContacts();
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
        }
    }

    public Message getMessage() {
        return message;
    }
}
