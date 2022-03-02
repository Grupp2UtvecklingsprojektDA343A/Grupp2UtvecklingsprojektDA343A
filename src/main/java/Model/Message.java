package Model;

import client.model.User;

import javax.swing.*;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private User sender;
    private User receiver;
    private String message;
    private ImageIcon image;
    private Date sent;
    private Date received;
    /**
     * 1st constructor for building a complete message with text and image.
     * @param sender the sender user.
     * @param receiver the receiver user.
     * @param message String containing text message.
     * @param image ImageIcon.
     * @param sent Date the message was sent by a client.
     * @param received Date message was received by the server.
     */
    public Message(User sender, User receiver, String message, ImageIcon image, Date sent, Date  received){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.image = image;
        this.sent = sent;
        this.received = received;
    }
    /**
     * 2nd Constructor to build a message containing only a text.
     * @param sender the sender user.
     * @param receiver the receiver user.
     * @param message String containing text message.
     * @param sent Date the message was sent by a client.
     * @param received Date message was received by the server.
     */
    public Message(User sender, User receiver, String message, Date sent, Date  received){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.sent = sent;
        this.received = received;
    }

    /**
     * 3rd Constructor to build a message containing only a text.
     * @param sender the sender user.
     * @param receiver the receiver user.
     * @param image ImageIcon.
     * @param sent Date the message was sent by a client.
     * @param received Date message was received by the server.
     */
    public Message(User sender, User receiver, ImageIcon image, Date sent, Date  received){
        this.sender = sender;
        this.receiver = receiver;
        this.image = image;
        this.sent = sent;
        this.received = received;
    }
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
     * Getter for a text message.
     * @return the String containing text is returned.
     */
    public String getMessage() {
        return message;
    }
    /**
     * Getter for a image
     * @return the ImageIcon containing an image is returned.
     */
    public ImageIcon getImage() {
        return image;
    }
    /**
     * Getter for what date a message was sent.
     * @return the date a message was sent is returned.
     */
    public Date getSent() {
        return sent;
    }
    /**
     * Getter for what date a message was received by the server.
     * @return the date a message was received by the server.
     */
    public Date getReceived() {
        return received;
    }



}
