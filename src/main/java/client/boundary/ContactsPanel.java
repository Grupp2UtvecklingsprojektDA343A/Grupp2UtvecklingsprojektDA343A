package client.boundary;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ContactsPanel extends DefaultPanel implements KeyListener, IContacts {
    private GridBagConstraints constraints = new GridBagConstraints();

    public ContactsPanel(MainWindow mainWindow, boolean showMenuBar, String username, ImageIcon profilePicture) {
        super(mainWindow, showMenuBar);

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
    public void closeApplication() {
        //TODO
    }

    @Override
    public void AddUser(String username, ImageIcon profilePicture) {
        ++constraints.gridy; // rad
        constraints.gridx = 0; // column
        constraints.gridwidth = 2;

        UserButton userButton = new UserButton("another user", profilePicture);
        userButton.addActionListener(l -> {
            if(getMainWindow().isChatWindowOpen(username)) {
                getMainWindow().focusChatWindow(username);
            } else {
                ChatWindow chatWindow = new ChatWindow(getMainWindow(), username);
                getMainWindow().addChatWindow(username, chatWindow);
            }
        });
        add(userButton, constraints);
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
