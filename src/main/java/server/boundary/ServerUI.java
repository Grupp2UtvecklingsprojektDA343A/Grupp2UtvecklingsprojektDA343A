package server.boundary;

import client.boundary.ImageHandler;
import globalEntity.User;
import server.control.Controller;
import server.entity.Server;
import server.entity.Traffic;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;

public class ServerUI extends JFrame implements PropertyChangeListener {
    private Server server;
    private JPanel panel; //innehållet i fönstret
    private ArrayList<String> traffic;
    private JList<String> trafficList;
    private DefaultListModel<String> dlm = new DefaultListModel<>();
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.A");

    public ServerUI(Server server){
        this.server = server;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        //controller.addListener(this);
        server.addListener(this);

        setTitle("Traffic log:");
        setLayout(new GridLayout(1,1));
        setVisible(true);
        setSize(400,500);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon arlako = ImageHandler.createImageIcon("/arlako.png");
        if(arlako != null) {
            setIconImage(arlako.getImage());
        }

        trafficList = new JList<>(dlm);

        JScrollPane jsp = new JScrollPane();
        // jsp.setSize(400,500);
        jsp.setViewportView(trafficList);
        // jsp.setLocation(0,0);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(jsp);
        //addTestTraffic();
        // new ServerUIThread().start();
    }

    public void addTestTraffic(){
        dlm.addElement("Test 1");
        dlm.addElement("Test 2");
        dlm.addElement("Test 3");
    }

    public void updateTraffic(String trafficInfo){//nytt
        dlm.addElement(trafficInfo);//nytt
    }

    public void updateTraffic(Traffic[] trafficInfo){
        for (Traffic element : trafficInfo){
            dlm.addElement(element.getText());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Traffic newValue = (Traffic) evt.getNewValue();
        String fullMessage = String.format("C:%s S:%s - %s",
            newValue.getClientRecievedTime().format(format),
            newValue.getServerReceivedTime().format(format),
            newValue.getText());

        updateTraffic(fullMessage);
    }

    private class ServerUIThread extends Thread {
        @Override
        public void run(){
            while (true){


            }
        }
    }
}
