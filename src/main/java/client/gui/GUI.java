package client.gui;

import client.Client;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;
import java.awt.GridLayout;

public class GUI extends JFrame {
        private DefaultPanel panel;
        private final Client client;

        public GUI(Client client) {
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
            // Skapa Inloggningsfönstret.
            panel = new LogInPanel(this);
            // Koppla till fönstret
            add(panel);
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

        private void changePanel(Dimension dimension, DefaultPanel panel) {
            setSize(dimension);
            changePanel(panel);
        }

        private void changePanel(DefaultPanel panel) {
            remove(this.panel);
            this.panel = panel;
            add(this.panel);
            repaint();
            revalidate();
        }
    }