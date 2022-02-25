package client;

import javax.swing.JFrame;
import java.awt.Dimension;

public class Client {
    public void showGUI() {
        // Skapa Inloggningsfönstret.
        LoginPanel loginPanel = new LoginPanel(false);

        JFrame window = new JFrame();
        Dimension dimension = new Dimension(500, 500);
        window.setSize(dimension);
        window.setVisible(true);
    }
}
