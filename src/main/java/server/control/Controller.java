package server.control;

import globalEntity.Message;
import globalEntity.User;
import server.boundary.ServerUI;
import server.entity.Server;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;

public class Controller implements PropertyChangeListener {
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private Server server;
    private ServerUI serverUI;

    public Controller(){
        server = new Server(this, 20008);
        server.addListener(this);
        serverUI = new ServerUI(server);
    }

    public void addListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        pcs.firePropertyChange("message", null, evt);
        if(evt.getPropertyName().equals("loginOK")){
            serverUI.updateTraffic(evt.getNewValue() + " just logged in.");
        } else if (evt.getPropertyName().equals("logout")){
            serverUI.updateTraffic(evt.getNewValue() + " just logged out.");
        } else if (evt.getPropertyName().equals("sent")){
            serverUI.updateTraffic((String) evt.getNewValue());
        } else if (evt.getPropertyName().equals("loginFail")){
            serverUI.updateTraffic(evt.getNewValue() + " failed to login.");
        }
    }

    public synchronized boolean userExists(User user) {
        return server.userExists(user);
    }

    public void addToTraffic(String traffic){
        //serverUI.updateTraffic(traffic);
    }

    public void sendMessage(Message message){
        server.sendMessage(message);
    }


    public void disconnect(Message message) {
        server.disconnect(message);
    }
    
    public void createFriendList(Message message){
        User user = message.getSender();
        ArrayList<User> users = new ArrayList<>(message.getContacts().size());
        Collections.copy(users, message.getContacts());
        server.createFriendList(user, users);
    }
}