package client.view;

/*
Pratar med Client och med hela gränssnittet (package.GUI).
 */

import client.controller.Client;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;

public class MainWindow extends JFrame {
        private DefaultPanel currentPanel;
        private final Client client;
        private HashMap<String, ChatWindow> openChatWindows = new HashMap<>();

        public MainWindow(Client client) {
            this.client = client;
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            // Skapa huvudfönstret
            Dimension dimension = new Dimension(500, 500);
            setSize(dimension);
            setLayout(new GridLayout(1,1));
            // Skapa inloggningsfönstret (LoginPanel).
            currentPanel = new LogInPanel(this);
            // Koppla till fönstret
            add(currentPanel);
            // Göm istället för att stänga
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            // setExtendedState(JFrame.ICONIFIED);

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentHidden(ComponentEvent e) {
                    // super.componentHidden(e);
                    if(openChatWindows.size() == 0) {
                        closeApplication();
                    }
                }
            });

            // Visa fönstret
            setVisible(true);
        }

        public void logIn() {
            client.logIn();
        }

        public void showContacts(String username, ImageIcon profilePicture) {
            Dimension dimension = new Dimension(200, 500);
            changePanel(dimension, new ContactsPanel(this, true, username, profilePicture));
        }

        private void changePanel(Dimension dimension, DefaultPanel newPanel) {
            setSize(dimension);
            changePanel(newPanel);
        }

        private void changePanel(DefaultPanel newPanel) {
            remove(this.currentPanel);
            // Kasta den gamla panelen
            this.currentPanel = newPanel;
            // Spara referensen som kommer in
            add(this.currentPanel);
            // Lägg till currentPanel till gränssnittet
            repaint();
            // Rita allting som är nytt
            revalidate();
            // Kontrollera vad vi ska ha kvar
        }

    public void uploadFile() {
        System.out.println("TODO");
    }

    public void closeApplication(){
        System.exit(0);
    }

    public void open() {
        setVisible(true);
    }

    protected void addChatWindow(String username, ChatWindow chatWindow) {
        openChatWindows.put(username, chatWindow);
    }

    protected void removeChatWindow(String username) {
        openChatWindows.remove(username);
    }

    protected boolean isChatWindowOpen(String username) {
        return openChatWindows.containsKey(username);
    }

    protected void focusChatWindow(String username) {
        ChatWindow chatWindow = openChatWindows.get(username);
        chatWindow.requestFocus();
    }
}