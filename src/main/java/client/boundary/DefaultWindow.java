package client.boundary;


import client.control.Client;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public abstract class DefaultWindow extends JFrame {
    public final Color BAD_COLOR = new Color(255,199,206);
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu menuFile = new JMenu("File");
    private final Client client;
    private JPanel panel = new JPanel();

    public DefaultWindow(Client client, boolean showMenuBar) {
        this.client = client;

        ImageIcon arlako = ImageHandler.createImageIcon("/arlako.png");
        if(arlako != null) {
            setIconImage(arlako.getImage());
        }

        if(showMenuBar) {
            JPanel filePanel = new JPanel(new BorderLayout());
            menuBar.add(menuFile);
            JMenuItem menuItemExit = new JMenuItem("Exit");
            menuFile.add(menuItemExit);
            menuItemExit.addActionListener(l -> closeApplication());
            filePanel.add(menuBar, BorderLayout.NORTH);
        }

        add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridBagLayout());
    }

    abstract void closeApplication();

    Client getClient() {
        return client;
    }

    void add(Component component, GridBagConstraints constraints) {
        panel.add(component, constraints);
    }

    void addToFileMenu(JMenuItem item) {
        menuFile.add(item);
    }

    void addJMenu(JMenu menu) {
        menuBar.add(menu);
    }

    public JPanel getPanel() {
        return panel;
    }
}
