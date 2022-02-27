package client.gui;

import client.ImageHandler;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ContactsPanel extends DefaultPanel implements KeyListener {
    public ContactsPanel(MainWindow gui, boolean showMenuBar) {
        super(gui, showMenuBar);

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
        ImageIcon profilePicture = ImageHandler.createImageIcon("http://webshare.mah.se/am3281/arlako.png");
        if(profilePicture != null) {
            Image img  = profilePicture.getImage();
            img = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            profilePicture = new ImageIcon(img);
        }
        add(new UserButton("another user", profilePicture), constraints);

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
