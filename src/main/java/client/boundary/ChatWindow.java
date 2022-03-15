package client.boundary;

import client.control.Client;
import client.control.WindowHandler;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatWindow extends DefaultWindow {
    private final DefaultListModel<Object> chatMessages = new DefaultListModel<>();
    private JTextArea textInput;
    private final String currentChatter;

    public ChatWindow(Client client, String currentChatter, ImageIcon profilePicture, WindowHandler windowHandler) {
        super(client, true);
        this.currentChatter = currentChatter;
        setTitle("Arlako chatt with: " + currentChatter);
        setLocationRelativeTo(null);
        setVisible(true);

       addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                windowHandler.removeChatWindow(currentChatter);
            }
        });

        addMenuOptions(windowHandler);
        createWindow(profilePicture);
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

    private void createWindow(ImageIcon profilePicture) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        Dimension dimension = new Dimension(400, 50);


        textInput = new JTextArea();
        textInput.setPreferredSize(dimension);
        textInput.setBackground(Color.WHITE);
        textInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textInput.setPreferredSize(dimension);

        JLabel name = new JLabel(currentChatter);

        JLabel onlineStatus = new JLabel("Online");

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(l -> {
            if(!textInput.getText().isEmpty()) {
                sendMessage(textInput.getText());
            }
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

        constraints.gridx = 0;
        constraints.gridy = 3;
        add(textInput, constraints);

        constraints.gridy = 3;
        constraints.gridx = 1;
        add(sendButton, constraints);

        setVisible(true);
        pack();

    }

    private void uploadFile() {
    }

    public void addMessage(String message, ImageIcon image) {
        addMessage(message);
        addMessage(image);
    }

    public void addMessage(String message) {
        chatMessages.addElement(message);
    }

    public void addMessage(ImageIcon image) {
        chatMessages.addElement(image);
    }

    public void sendMessage(String text) {
        LocalDateTime timestamp = LocalDateTime.now();
        addMessage(timestamp.format(DateTimeFormatter.ISO_LOCAL_TIME) + ": " + text);
        getClient().sendMessage(currentChatter, text, timestamp);
    }

    @Override
    void closeApplication() {

    }

    public void loggedIn() {
        setTitle("OFFLINE: Arlako chatt with: " + currentChatter);
    }

    public void loggedOut() {
        setTitle("OFFLINE: Arlako chatt with: " + currentChatter);
    }
}
