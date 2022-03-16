package server.boundary;

import client.boundary.ImageHandler;
import server.entity.Server;
import server.entity.Traffic;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;

public class TrafficLogGUI extends JFrame {
    private Server server;
    private JList<String> trafficList;
    private DefaultListModel<String> dlm = new DefaultListModel<>();
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss.A");

    public TrafficLogGUI(Server server){
        this.server = server;
        SwingUtilities.invokeLater(this::createAndShowGUI);
    }

    private void createAndShowGUI() {
        setTitle("Traffic log:");
        setLayout(new GridLayout(1,1));
        setVisible(true);
        setSize(700,500);
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
    }
    public void updateTraffic(ArrayList<Traffic> trafficInfo){
        dlm.addElement(
              trafficInfo.get(trafficInfo.size()-1).getServerReceivedTime()
            + ": "
            + trafficInfo.get(trafficInfo.size()-1).getText());
    }
}
