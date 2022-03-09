package server.control;

import globalEntity.Message;
import globalEntity.User;
import server.boundary.ServerUIX;
import server.entity.ClientHandler;
import server.entity.Server;
import server.entity.ServerReceiver;

import java.awt.print.Book;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Controller implements PropertyChangeListener{
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Server server;

    public Controller(){
        server = new Server(this, 20008);
        server.addListener(this);
        ServerUIX serverUIX = new ServerUIX();
    }

    public void addListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        pcs.firePropertyChange("message", null, evt);
        System.out.println("fjupp");
    }

    public boolean userExists(globalEntity.User user) {
        return server.userExists(user);
    }

    public synchronized void parseMessage(Message message) {
        globalEntity.User sender = message.getSender();
        globalEntity.User receiver = message.getReceiver();

        switch(message.getType()) {
            case Message.CONTACTS -> {}
            case Message.TEXT -> {}
            case Message.IMAGE -> {}
            case Message.TEXT_AND_IMAGE -> {}
        }
    }

    public synchronized boolean login(User user) {
        return userExists(user);
    }
}