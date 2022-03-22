package globalEntity;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class is used to build messages.
 * @author Christian Edvall & Linnéa Flystam.
 * @version 1.0
 */
public class Message implements Serializable {
    public static final int CONTACTS = 1;
    public static final int TEXT = 2;
    public static final int IMAGE = 3;
    public static final int TEXT_AND_IMAGE = 4;
    public static final int LOGIN = 5;
    public static final int LOGIN_SUCCESS = 6;
    public static final int LOGIN_FAILED = 7;
    public static final int LOGOUT = 8;
    public static final int NOTIFY_RECEIVED = 9;
    public static final int USER_LOGGED_OUT = 10;
    public static final int USER_LOGGED_IN = 11;
    public static final int VIBRATE = 12;

    //@Serial
    //private static final long serialVersionUID = -687991492884005033L;
    private User sender;
    private User receiver;
    private String message;
    private ImageIcon image;
    private LocalDateTime sent;
    private LocalDateTime received;
    private int type;
    private ArrayList<User> contacts;

    /**
     * Getter for user.
     * @return the user that sent a message is returned.
     */
    public User getSender() {
        return sender;
    }
    /**
     * Getter for receiver.
     * @return the user that receives a message is returned.
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     *
     * @param sender
     */
    public void setSender(User sender) {
        this.sender = sender;
    }

    /**
     * Getter for a text message.
     * @return the String containing text is returned.
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for a image
     * @return the ImageIcon containing an image is returned.
     */
    public ImageIcon getImage() {
        return image;
    }

    /**
     *
     * @param image
     */
    public void setImage(ImageIcon image) {
        this.image = image;
    }

    /**
     * Getter for what date a message was sent.
     * @return the date a message was sent is returned.
     */
    public LocalDateTime getSent() {
        return sent;
    }

    /**
     * Setter for received date, using LocalDateTime method now().
     * @param sent
     */
    //Fungerar ej dessa, skapa variabel i server som istället skickar in detta via parameter.
    public void setSent(LocalDateTime sent) {
        this.sent = sent;
    }
    /**
     * Getter for what date a message was received by the server.
     * @return the date a message was received by the server.
     */
    public LocalDateTime getReceived() {
        return received;
    }

    /**
     *
     * @param receiver
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    /**
     *
     * @return
     */
    public int getType() {
        return type;
    }

    /**
     * Setter for received date, using LocalDateTime method now().
     * @param received
     */
    //Fungerar ej dessa, skapa variabel i server som istället skickar in detta via parameter.
    public void setReceived(LocalDateTime received) {
        this.received = received;
    }

    /**
     *
     * @return
     */
    public ArrayList<User> getContacts() {
        return contacts;
    }

    /**
     *
     * @param contacts
     */
    public void setContacts(ArrayList<User> contacts) {
        this.contacts = contacts;
    }

    public static class Builder {
        private final Message message = new Message();

        public Message build() {
            return message;
        }

        public Builder sender(User sender) {
            message.setSender(sender);
            return this;
        }

        public Builder receiver(User receiver) {
            message.setReceiver(receiver);
            return this;
        }

        public Builder message(String message) {
            this.message.setMessage(message);
            return this;
        }

        public Builder image(ImageIcon image) {
            message.setImage(image);
            return this;
        }

        public Builder sent(LocalDateTime sent) {
            message.setSent(sent);
            return this;
        }

        public Builder received(LocalDateTime received) {
            message.setReceived(received);
            return this;
        }

        public Builder type(int type) {
            message.type = type;
            return this;
        }

        public Builder contacts(ArrayList<User> contacts) {
            message.setContacts(contacts);
            return this;
        }
    }
}
