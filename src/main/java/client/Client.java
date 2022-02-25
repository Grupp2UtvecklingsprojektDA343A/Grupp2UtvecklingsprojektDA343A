package client;

import client.gui.GUI;

public class Client {
    private GUI gui;

    public void showGUI() {
        // SwingUtilities.invokeLater(() -> gui = new GUI(this));
        gui = new GUI(this);
    }

    public void logIn() {
        gui.showContacts();
    }
}
