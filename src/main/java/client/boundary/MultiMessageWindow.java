package client.boundary;

import client.control.Client;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;

public class MultiMessageWindow extends JFrame {
    private Client client;
    private DefaultListModel<String> dlm;
    private JList<String> onlineUsers;
    private ArrayList<String> currentlyOnline;
    private JPanel panel;

    public MultiMessageWindow(){
        currentlyOnline = client.getCurrentlyOnline();
        dlm = new DefaultListModel<>();
        onlineUsers = new JList<>(dlm);
        panelSetup();
    }

    public void panelSetup(){
        for (int i = 0; i < currentlyOnline.size(); i++) {
            dlm.addElement(currentlyOnline.get(i));
        }
        panel.setLayout(new BorderLayout());
    }
    private class RPanel extends JPanel{

    }
    private class LPanel extends JPanel{
        private JButton send;
        private JButton add;
        private JButton remove;


    }
}
