package client;

import client.gui.MainWindow;
/*
Agerar som Controller
 */
public class Client {
    private MainWindow mainWindow;

    public void showGUI() {
        // SwingUtilities.invokeLater(() -> gui = new GUI(this));
        mainWindow = new MainWindow(this);
    }

    public void logIn() {
        mainWindow.showContacts();
    }
}
