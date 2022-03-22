package client.boundary;

import client.control.Client;
import client.control.WindowHandler;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MultiChatWindow extends DefaultWindow {
    private final DefaultListModel<Object> chatMessages = new DefaultListModel<>();
    private final JTextArea textInput = new JTextArea();
    private final String currentChatter;
    private final JButton sendButton = new JButton("Send");
    private final JButton sendImageButton = new JButton("Image");
    private final StatusLabel onlineStatus = new StatusLabel();
    private final ImageIcon online = ImageHandler.createImageIcon("/online.png", 50, 50);
    private final ImageIcon offline = ImageHandler.createImageIcon("/offline.png", 50, 50);

    public MultiChatWindow(Client client, String currentChatter, ImageIcon pPicture, WindowHandler wh, boolean online) {
        super(client, true);
        this.currentChatter = currentChatter;
        setTitle("Arlako chatt with: " + currentChatter);
        setLocationRelativeTo(null);
        setVisible(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                wh.removeChatWindow(currentChatter);
            }
        });

        addMenuOptions(wh);
        createWindow(pPicture, online);
    }

    private void addMenuOptions(WindowHandler windowHandler) {
        JMenuItem uploadFile = new JMenuItem("Upload file");
        uploadFile.addActionListener(l -> {
            uploadFile();
        });
        addToFileMenu(uploadFile);

        JMenuItem showContacts = new JMenuItem("Contacts");
        showContacts.addActionListener(l -> {
            windowHandler.showContactWindow();
        });
        addToFileMenu(showContacts);
    }

    private void createWindow(ImageIcon profilePicture, boolean isOnline) {
        JLabel name = new JLabel(currentChatter);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        Dimension dimension = new Dimension(400, 50);


        textInput.setPreferredSize(dimension);
        textInput.setBackground(Color.WHITE);
        textInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textInput.setPreferredSize(dimension);

        if(isOnline) {
            onlineStatus.setIcon(online, true);
        } else {
            onlineStatus.setIcon(offline, false);
            if(!getClient().isFriend(currentChatter)) {
                sendButton.setEnabled(false);
                sendImageButton.setEnabled(false);
                textInput.setEnabled(false);

                sendButton.setContentAreaFilled(false);
                sendImageButton.setContentAreaFilled(false);
                textInput.setBackground(Color.GRAY);
            }
        }

        sendButton.addActionListener(l -> {
            if(!textInput.getText().isEmpty()) {
                sendMessage(textInput.getText());
            }
        });

        sendImageButton.addActionListener(l -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "JPG, PNG, BMP & GIF", "jpg", "png", "bmp", "gif"
            );
            fileChooser.setFileFilter(filter);
            int returnVal = fileChooser.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isFile()) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                sendMessage(ImageHandler.createImageIcon(path));
            }
        });

        onlineStatus.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(onlineStatus.status) {
                    sendVibrate();
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

        JScrollPane conversationArea = new JScrollPane(new JList<>(chatMessages));
        conversationArea.setBackground(Color.WHITE);
        conversationArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        conversationArea.setPreferredSize(new Dimension(300, 300));

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy = 0;
        constraints.gridx = 0;
        constraints.gridheight = 3;
        add(conversationArea, constraints);
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.NONE;

        constraints.gridy = 0;
        constraints.gridx = 1;
        add(new JLabel(profilePicture), constraints);

        constraints.gridy = 1;
        add(name, constraints);

        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(onlineStatus, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        add(textInput, constraints);

        constraints.gridy = 3;
        constraints.gridx = 1;
        add(sendImageButton, constraints);

        constraints.gridy = 3;
        constraints.gridx = 2;
        add(sendButton, constraints);

        setVisible(true);
        pack();

    }

    private void uploadFile() {
    }

    public void addMessage(String message, ImageIcon image) {
        addMessage(message);
        addMessage(ImageHandler.resizeImageIcon(image, 50, 50));
    }

    public void addMessage(String message) {
        chatMessages.addElement(message);
    }

    public void addMessage(ImageIcon image) {
        chatMessages.addElement(image);
    }

    public void sendVibrate() {
        LocalDateTime timestamp = LocalDateTime.now();
        addMessage(timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME) + ": " + "vib sent!");
        getClient().sendVibrate(currentChatter, timestamp);
    }

    public void sendMessage(String text) {
        LocalDateTime timestamp = LocalDateTime.now();
        addMessage(timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME) + ": " + text);
        getClient().sendMultiple(null, text);
    }

    public void sendMessage(ImageIcon imageIcon) {
        LocalDateTime timestamp = LocalDateTime.now();
        addMessage(timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME) + ": ", imageIcon);
        getClient().sendMultiple(imageIcon, null);
    }

    @Override
    void closeApplication() {

    }

    public void loggedIn() {
        onlineStatus.setIcon(online, true);
        sendButton.setEnabled(true);
        sendImageButton.setEnabled(true);
        textInput.setEnabled(true);

        sendButton.setContentAreaFilled(true);
        sendImageButton.setContentAreaFilled(true);
        textInput.setBackground(Color.WHITE);
    }

    public void loggedOut(boolean isFriend) {
        if(!isFriend) {
            sendButton.setEnabled(false);
            sendImageButton.setEnabled(false);
            textInput.setEnabled(false);

            sendButton.setContentAreaFilled(false);
            sendImageButton.setContentAreaFilled(false);
            textInput.setBackground(Color.GRAY);
        }

        onlineStatus.setIcon(offline, false);
    }


    public void vibrate() {
        int VIBRATION_LENGTH = 30;
        int VIBRATION_VELOCITY = 10;
        try {/* w  ww .  j  a va2 s.  c o m*/
            final int originalX = this.getLocationOnScreen().x;
            final int originalY = this.getLocationOnScreen().y;
            for (int i = 0; i < VIBRATION_LENGTH; i++) {
                Thread.sleep(10);
                this.setLocation(originalX, originalY + VIBRATION_VELOCITY);
                Thread.sleep(10);
                this.setLocation(originalX, originalY - VIBRATION_VELOCITY);
                Thread.sleep(10);
                this.setLocation(originalX + VIBRATION_VELOCITY, originalY);
                Thread.sleep(10);
                this.setLocation(originalX, originalY);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private class StatusLabel extends JLabel {
        public boolean status;

        public void setIcon(Icon icon, boolean status) {
            super.setIcon(icon);
            this.status = status;
        }
    }
}
