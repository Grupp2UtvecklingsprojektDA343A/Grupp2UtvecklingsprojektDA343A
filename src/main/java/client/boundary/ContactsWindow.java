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
import java.util.concurrent.ConcurrentHashMap;

public class ContactsWindow extends DefaultWindow implements KeyListener {
    private final GridBagConstraints constraints = new GridBagConstraints();
    private final ConcurrentHashMap<String, UserButton> listOfUser = new ConcurrentHashMap<>();

    public ContactsWindow(Client client, WindowHandler windowHandler, boolean showMenuBar, String username, ImageIcon profilePicture) {
        super(client, showMenuBar);

        JMenuItem logOut = new JMenuItem("Log out");
        logOut.addActionListener(l -> {
            getClient().logOut(this);
        });
        addToFileMenu(logOut);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                windowHandler.contactsWindowClosed();
            }
        });

        setSize(200, 500);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

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

        repaint();
        revalidate();
        pack();
    }


    public void addUser(String username, ImageIcon profilePicture) {
        if(!isOnList(username)) {
            ++constraints.gridy; // rad

            FriendButton friend = new FriendButton(username);
            constraints.gridx = 0; // column
            constraints.gridwidth = 1;
            add(friend, constraints);

            constraints.gridx = 1; // column
            constraints.gridwidth = 2;

            UserButton userButton = new UserButton(username, profilePicture);
            add(userButton, constraints);
            listOfUser.put(username, userButton);
        }

        repaint();
        revalidate();
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

    public void loggedOut(String username) {
        getPanel().remove(listOfUser.get(username));
        repaint();
        revalidate();
        pack();
        System.out.println("nu tog vi bort: " + username);
    }

    private class UserButton extends JButton {
        public UserButton(String username, ImageIcon profilePicture) {
            setText(username);
            setIcon(profilePicture);
            addActionListener(l -> {
                getClient().startChatWithUser(username);
            });
        }
    }

    private class FriendButton extends JButton {
        private boolean isFriend = false;
        private String username;
        private final ImageIcon star = ImageHandler.createImageIcon("/favorite.png", 50, 50);
        private final ImageIcon unstar = ImageHandler.createImageIcon("/unfavorite.png", 50, 50);

        public FriendButton(String username) {
            this.username = username;
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
                repaint();
            });
        }
    }
}
