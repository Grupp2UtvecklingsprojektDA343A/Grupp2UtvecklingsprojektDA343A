package client.gui;

import client.ImageHandler;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ContactsPanel extends DefaultPanel implements KeyListener {
    public ContactsPanel(MainWindow mainWindow, boolean showMenuBar) {
        super(mainWindow, showMenuBar);

        Dimension jTextFieldPreferredSize = new Dimension(100, 19);

        JLabel lImage = new JLabel("'image'");
        JLabel lUsername = new JLabel("'username'");

        GridBagConstraints constraints = new GridBagConstraints();
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

        constraints.gridy = 1; // rad
        constraints.gridx = 0; // column
        constraints.gridwidth = 2;
        ImageIcon profilePicture = ImageHandler.createImageIcon("http://webshare.mah.se/am3281/arlako.png", 30, 30);
        int userId = 1;
        UserButton bTemplateUser = new UserButton("another user", profilePicture);
        bTemplateUser.addActionListener(l -> {
            if(getMainWindow().isChatWindowOpen(userId)) {
                getMainWindow().focusChatWindow(userId);
            } else {
                ChatWindow chatWindow = new ChatWindow(mainWindow, "another user");
                getMainWindow().addChatWindow(userId, chatWindow);
            }
        });
        add(bTemplateUser, constraints);

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

    private class UserButton extends JButton {
        public UserButton(String name, ImageIcon profilePicture) {
            setText(name);
            setIcon(profilePicture);
            addActionListener(l -> {
                // Öppna fönster mot kontakt
            });
        }
    }
}
