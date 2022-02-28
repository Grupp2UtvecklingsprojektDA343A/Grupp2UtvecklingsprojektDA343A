package client.gui;

/*
Pratar med Client och med hela gränssnittet (package.GUI).
 */

import client.Client;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;
import java.awt.GridLayout;

public class MainWindow extends JFrame {
        private DefaultPanel currentPanel;
        private final Client client;

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
            // Visa fönstret
            setVisible(true);
        }

        public void logIn() {
            client.logIn();
        }

        public void showContacts() {
            Dimension dimension = new Dimension(200, 500);
            changePanel(dimension, new ContactsPanel(this, true));
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
}