package Model;

import client.User;

import javax.swing.*;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private User sender;
    private User receiver;
    private String message;
    private ImageIcon image;

    public Message(User sender, User receiver, String message, ImageIcon image, Date sent, Date  received){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.image = image;
    }
    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public ImageIcon getImage() {
        return image;
    }
}
