package server.boundary;

import globalEntity.User;
import server.control.Controller;
import server.entity.Server;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.*;

public class ServerUI extends JFrame implements PropertyChangeListener {
    private Server server;
    private JPanel panel; //innehållet i fönstret
    private ArrayList<String> traffic;
    private JList<String> trafficList;
    private DefaultListModel<String> dlm = new DefaultListModel<>();

    public ServerUI(Server server){
        //controller.addListener(this);
        server.addListener(this);

        setTitle("Traffic log:");
        setLayout(null);
        setVisible(true);
        setSize(400,500);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        trafficList = new JList<>(dlm);

        JScrollPane jsp = new JScrollPane();
        jsp.setSize(400,500);
        jsp.setViewportView(trafficList);
        jsp.setLocation(0,0);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(jsp);
        //addTestTraffic();
        new ServerUIThread().start();
    }

    public void addTestTraffic(){
        dlm.addElement("Test 1");
        dlm.addElement("Test 2");
        dlm.addElement("Test 3");
    }

    public void updateTraffic(String trafficInfo){//nytt
        dlm.addElement(trafficInfo);//nytt
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // System.out.println("Something happened");
        // System.out.println();
        // if (evt.getPropertyName().equals("message")){
        //     User user = (User) evt.getNewValue();
        //     String name = user.getUsername();
        //     traffic.add(name + " just logged in.");
        // } else if (evt.getPropertyName().equals("login")){
        //     updateTraffic(evt + " just logged in.");
        // } else if (evt.getPropertyName().equals("logout")){
        //     updateTraffic(evt + " just logged out.");
        // }
    }

    private class ServerUIThread extends Thread {
        @Override
        public void run(){
            while (true){


            }
        }
    }
}
