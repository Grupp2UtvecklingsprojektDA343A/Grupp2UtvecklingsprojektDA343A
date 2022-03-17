package client.boundary;

import client.control.Client;
import client.control.WindowHandler;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.ConcurrentHashMap;

public class ContactsWindow extends DefaultWindow implements KeyListener {
    private final GridBagConstraints constraints = new GridBagConstraints();
    private final ConcurrentHashMap<String, JButton[]> listOfUser = new ConcurrentHashMap<>();

    public ContactsWindow(Client client, WindowHandler wH, boolean showMenuBar, String username, ImageIcon pPicture) {
        super(client, showMenuBar);

        JMenuItem logOut = new JMenuItem("Log out");
        logOut.addActionListener(l -> {
            getClient().logOut(this, true);
        });
        addToFileMenu(logOut);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                wH.contactsWindowClosed();
            }
        });

        setSize(200, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        JLabel lImage = new JLabel(pPicture);
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

        pack();
    }


    public void addUser(String username, ImageIcon profilePicture) {
        if(isOnList(username)) {
            listOfUser.get(username)[1].setContentAreaFilled(true);
        } else {
            ++constraints.gridy; // rad

            FriendButton friend = new FriendButton(username);
            constraints.gridx = 0; // column
            constraints.gridwidth = 1;
            add(friend, constraints);

            constraints.gridx = 1; // column
            constraints.gridwidth = 2;

            UserButton userButton = new UserButton(username, profilePicture);
            add(userButton, constraints);
            listOfUser.put(username, new JButton[]{friend, userButton});
        }
        pack();
    }


    public boolean isOnList(String username) {
        return listOfUser.containsKey(username);
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

    public void loggedOut(String username, boolean isFriend) {
        JButton[] components = listOfUser.get(username);

        if(isFriend) {
            components[1].setContentAreaFilled(false);
        } else {
            getPanel().remove(components[0]); // friend
            getPanel().remove(components[1]); // user button
            listOfUser.remove(username);
        }

        repaint();
        pack();
    }

    public void clickStar(String username) {
        JButton b = listOfUser.get(username)[0];
        b.doClick();
    }

    private class UserButton extends JButton {
        public UserButton(String username, ImageIcon profilePicture) {
            setText(username);
            setIcon(profilePicture);
            addActionListener(l -> {
                getClient().startChatWithUser(username, getClient().isOnline(username));
            });
        }
    }

    private class FriendButton extends JButton {
        private boolean isFriend = false;
        private final ImageIcon star = ImageHandler.createImageIcon("/favorite.png", 50, 50);
        private final ImageIcon unstar = ImageHandler.createImageIcon("/unfavorite.png", 50, 50);

        public FriendButton(String username) {
            setIcon(unstar);
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);

            addActionListener(l -> {
                isFriend = !isFriend;
                if(isFriend) {
                    setIcon(star);
                } else {
                    setIcon(unstar);
                }
                getClient().saveContact(username, isFriend);
            });
        }
    }
}
