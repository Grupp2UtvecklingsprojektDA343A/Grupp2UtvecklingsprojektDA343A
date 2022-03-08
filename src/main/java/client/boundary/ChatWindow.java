package client.boundary;

import client.control.Client;
import client.control.WindowHandler;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ChatWindow extends DefaultWindow implements IChat {
    private JList<Object> conversation;
    private JScrollPane conversationArea;
    private JTextArea textInput;
    private JLabel name, onlineStatus, profilePicture;
    private JButton sendButton;

    public ChatWindow(Client client, String currentChatter, WindowHandler windowHandler) {
        super(client, true);
        // Dimension dimension = new Dimension(500, 500);
        // setSize(dimension);
        setTitle("Arlako chatt window: " + currentChatter);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                windowHandler.removeChatWindow(currentChatter);
            }
        });

        Dimension dimension = new Dimension(400, 50);
        textInput = new JTextArea();
        textInput.setPreferredSize(dimension);
        setVisible(true);
        setLocationRelativeTo(null);
        name = new JLabel("Namn Namnsson");
        onlineStatus = new JLabel("Onfline");
        profilePicture = new JLabel(ImageHandler.createImageIcon("/arlako.png", 30, 30));
        conversationArea = new JScrollPane();
        sendButton = new JButton("Send");
        GridBagConstraints constraints = new GridBagConstraints();
        Insets insets = new Insets(5, 5, 5, 5);
        constraints.insets = insets;

        JMenuItem uploadFile = new JMenuItem("Upload file");
        uploadFile.addActionListener(l -> {
            uploadFile();
        });
        addToFileMenu(uploadFile);

        JMenuItem showContacts = new JMenuItem("Contacts");
        showContacts.addActionListener(l -> {
            windowHandler.showContactWindow();
        });
        addToFileMenu(showContacts);;

        textInput.setBackground(Color.WHITE);
        textInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textInput.setPreferredSize(dimension);
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
        add(profilePicture, constraints);

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

    @Override
    public void showMessage() {

    }

    @Override
    void closeApplication() {

    }
}
