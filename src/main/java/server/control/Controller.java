package server.control;

import globalEntity.Message;
import globalEntity.User;
import server.boundary.ServerUI;
import server.entity.Server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Controller implements PropertyChangeListener {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Server server;

    public Controller(){
        server = new Server(this, 20008);
        server.addListener(this);
        ServerUI serverUI = new ServerUI(this);
    }

    public void addListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        pcs.firePropertyChange("message", null, evt);
        System.out.println("fjupp");
    }

    public synchronized void parseMessage(Message message) {
        User sender = message.getSender();
        User receiver = message.getReceiver();

        switch(message.getType()) {
            case Message.CONTACTS -> {}
            case Message.TEXT -> {}
            case Message.IMAGE -> {}
            case Message.TEXT_AND_IMAGE -> {}
        }
    }

    public synchronized boolean userExists(User user) {
        return server.userExists(user);
    }
}