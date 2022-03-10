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
import javax.swing.*;

public class ServerUI extends JFrame implements PropertyChangeListener {
    private Server server;
    private JPanel panel; //innehållet i fönstret
    private ArrayList<String> traffic;
    private JList<String> trafficList;
    private DefaultListModel<String> dlm = new DefaultListModel<>();

    public ServerUI(Controller controller){
        controller.addListener(this);

        setLayout(null);
        setVisible(true);
        setSize(400,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        trafficList = new JList<>(dlm);

        JScrollPane jsp = new JScrollPane();
        jsp.setSize(400,500);
        jsp.setViewportView(trafficList);
        jsp.setLocation(0,0);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(jsp);
        addTestTraffic();
        new ServerUIThread().start();
    }

    private void getTraffic(ArrayList<String> traffic){

    }

    public void addTestTraffic(){
        dlm.addElement("Test 1");
        dlm.addElement("Test 2");
        dlm.addElement("Test 3");
    }

    public void updateTraffic(String trafficInfo){
        JLabel jLabel = new JLabel(trafficInfo);
        trafficList.add(jLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("Something happened");
        System.out.println(evt);/*
        if (evt.getPropertyName().equals("message")){
            traffic.add(evt.toString());
        } else if (evt.getPropertyName().equals("logged in")){
            traffic.add(evt.toString());
        } else if (evt.getPropertyName().equals("logged out")){
            traffic.add(evt.toString());
        }*/
    }

    private class ServerUIThread extends Thread {
        @Override
        public void run(){
            while (true){
                //String traffic =
                //updateTraffic();

            }
        }
    }
}
