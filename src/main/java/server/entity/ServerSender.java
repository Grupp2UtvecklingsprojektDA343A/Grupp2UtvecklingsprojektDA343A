package server.entity;

import globalEntity.Message;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ServerSender extends Thread implements PropertyChangeListener {
    private final ObjectOutputStream oos;

    public ServerSender(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public synchronized void send(Message message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "login" -> {
            }
        }
    }
}
