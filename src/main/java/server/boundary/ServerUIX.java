package server.boundary;

import server.control.Controller;
import server.entity.Server;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class ServerUIX implements PropertyChangeListener {
    private Server server;
    private JFrame window;
    private JPanel panel; //innehållet i fönstret
    private JList list;
    private JScrollPane jScrollPane;
    private Controller controller;
    private ArrayList<String> traffic;

    public ServerUIX(Controller controller){
        controller.addListener(this);
    }

    private void getTraffic(ArrayList<String> traffic){

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Trams");
        System.out.println(evt);/*
        if (evt.getPropertyName().equals("message")){
            traffic.add(evt.toString());
        } else if (evt.getPropertyName().equals("logged in")){
            traffic.add(evt.toString());
        } else if (evt.getPropertyName().equals("logged out")){
            traffic.add(evt.toString());
        }*/
    }
}
