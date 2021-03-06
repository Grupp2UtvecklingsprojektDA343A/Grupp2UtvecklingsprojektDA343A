package client.boundary;


import client.control.Client;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.prefs.Preferences;

public class LoginWindow extends DefaultWindow implements KeyListener {
    private final JButton bLogin = new JButton("login");
    private final JLabel spinningGear = new JLabel(ImageHandler.createImageIcon("/gear.gif"));
    private JLabel bProfilePicture;
    private ImageIcon profilePicture;
    private String username;
    private String host;
    private int port;

    public LoginWindow(Client client, boolean showMenuBar) {
        super(client, showMenuBar);

        java.util.prefs.Preferences preferences = java.util.prefs.Preferences.userRoot().node("/ArlaKoChat");
        profilePicture = ImageHandler.createImageIcon(preferences.get("profilbild", "/arlako.png"), 57, 57);
        username = preferences.get("username", "Mr. Kaffe");
        host = preferences.get("host", "localhost");
        port = preferences.getInt("port", 20008);

        setSize(500, 500);
        Dimension jTextFieldPreferredSize = new Dimension(100, 19);

        bProfilePicture = new JLabel("Välj bild", profilePicture, JLabel.CENTER);
        JLabel lUsername = new JLabel("Username: ");
        JLabel lHost = new JLabel("Host: ");
        JLabel lPort = new JLabel("Port: ");
        // JTextField tfUsername = new JTextField(username + new java.util.Random().nextInt(0, 9));
        JTextField tfUsername = new JTextField(username);
        JTextField tfHost = new JTextField(host);
        JTextField tfPort = new JTextField("20008");

        bProfilePicture.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "JPG, PNG, BMP & GIF", "jpg", "png", "bmp", "gif"
                );
                fileChooser.setFileFilter(filter);
                int returnVal = fileChooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isFile()) {
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    profilePicture = ImageHandler.createImageIcon(path, 57, 57);
                    bProfilePicture.setText(null);
                    bProfilePicture.setIcon(profilePicture);
                    preferences.put("profilbild", path);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        bProfilePicture.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        tfUsername.setPreferredSize(jTextFieldPreferredSize);
        tfHost.setPreferredSize(jTextFieldPreferredSize);

        // exit.addActionListener(l -> super.getMainFrame().closeApplication());
        bLogin.addActionListener(l -> {
            boolean missingFields = false;

            if (tfUsername.getText().isBlank()) {
                tfUsername.setBackground(super.BAD_COLOR);
                missingFields = true;
            }

            if (tfHost.getText().isBlank()) {
                tfHost.setBackground(super.BAD_COLOR);
                missingFields = true;
            }

            int port = 0;
            if (tfPort.getText().isBlank()) {
                missingFields = true;
                tfPort.setBackground(super.BAD_COLOR);
            } else {
                try {
                    port = Integer.parseInt(tfPort.getText());
                } catch (NumberFormatException e) {
                    missingFields = true;
                    tfPort.setBackground(super.BAD_COLOR);
                }
            }

            if (!missingFields) {
                username = tfUsername.getText();
                host = tfHost.getText();
                getClient().logIn(username, profilePicture, host, port, this);
                spinningGear.setVisible(true);
                preferences.put("username", username);
                preferences.put("host", host);
                preferences.putInt("port", port);
            }
        });

        DocumentListener resetColor = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                JComponent source = (JComponent) e.getDocument().getProperty("source");
                source.setBackground(Color.WHITE);
                revalidate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {}

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        };

        tfUsername.getDocument().putProperty("source", tfUsername);
        tfUsername.getDocument().addDocumentListener(resetColor);
        tfHost.getDocument().addDocumentListener(resetColor);
        tfHost.getDocument().putProperty("source", tfHost);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(1, 5, 1, 5);


        constraints.gridy = 0; // rad
        constraints.gridx = 0; // column
        // constraints.gridwidth = 1;
        add(lUsername, constraints);

        constraints.gridy = 0; // rad
        constraints.gridx = 1; // column
        // constraints.gridwidth = 2;
        add(tfUsername, constraints);

        constraints.gridy = 0; // rad
        constraints.gridx = 2; // column
        constraints.gridheight = 3;
        add(bProfilePicture, constraints);

        constraints.gridy = 1; // rad
        constraints.gridx = 0; // column
        constraints.gridheight = 1;
        add(lHost, constraints);

        constraints.gridy = 1; // rad
        constraints.gridx = 1; // column
        // constraints.gridwidth = 2;
        add(tfHost, constraints);

        constraints.gridy = 2; // rad
        constraints.gridx = 0; // column
        // constraints.gridwidth = 1;
        add(lPort, constraints);

        constraints.gridy = 2; // rad
        constraints.gridx = 1; // column
        // constraints.gridwidth = 2;
        add(tfPort, constraints);

        constraints.gridy = 3; // rad
        constraints.gridx = 0; // column
        constraints.gridwidth = 2;
        add(bLogin, constraints);

        constraints.gridy = 4; // rad
        constraints.gridx = 0; // column
        constraints.gridwidth = 3;
        spinningGear.setVisible(false);
        add(spinningGear, constraints);


        tfUsername.addKeyListener(this);
        tfHost.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            bLogin.doClick();
        } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            closeApplication();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void closeApplication() {
        getClient().logOut(null, false);
    }
    public void done() {
        spinningGear.setVisible(false);
    }
}
