package client.boundary;

import client.control.Client;
import client.control.WindowHandler;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ContactsWindow extends DefaultWindow implements KeyListener, IContacts {
    private final GridBagConstraints constraints = new GridBagConstraints();
    private WindowHandler windowHandler;

    public ContactsWindow(Client client, WindowHandler windowHandler, boolean showMenuBar, String username, ImageIcon profilePicture) {
        super(client, showMenuBar);
        this.windowHandler = windowHandler;

        setSize(200, 500);

        JLabel lImage = new JLabel(profilePicture);
        JLabel lUsername = new JLabel(username);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 5, 1, 5);

        constraints.gridy = 0; // rad
        constraints.gridx = 0; // column
        // constraints.gridwidth = 1;
        add(lImage, constraints);

        constraints.gridy = 0; // rad
        constraints.gridx = 1; // column
        // constraints.gridwidth = 1;
        add(lUsername, constraints);

        JMenuItem logOut = new JMenuItem("Log out");
        logOut.addActionListener(l -> {
            getClient().logOut(this);
        });
        addToFileMenu(logOut);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                windowHandler.contactsWindowClosed();
            }
        });
    }

    @Override
    public void addUser(String username, ImageIcon profilePicture) {
        ++constraints.gridy; // rad
        constraints.gridx = 0; // column
        constraints.gridwidth = 2;

        UserButton userButton = new UserButton("another user", profilePicture);
        userButton.addActionListener(l -> {
            getClient().startChatWithUser(username);
        });
        add(userButton, constraints);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            closeApplication();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    void closeApplication() {

    }

    private static class UserButton extends JButton {
        public UserButton(String name, ImageIcon profilePicture) {
            setText(name);
            setIcon(profilePicture);
            addActionListener(l -> {
                // Öppna fönster mot kontakt
            });
        }
    }
}