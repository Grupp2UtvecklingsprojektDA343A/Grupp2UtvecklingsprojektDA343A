package client.gui;

import client.IChat;
import javax.swing.*;
import java.awt.*;

public class ChatWindow extends JFrame implements IChat {

    public ChatWindow(MainWindow mainWindow, String currentChatter) {
        Dimension dimension = new Dimension(400, 400);
        setSize(dimension);
        GridLayout gridLayout = new GridLayout(1,1);
        setLayout(gridLayout);
        DefaultPanel panel = new TextInputPanel(mainWindow, true);
        add(panel);
        setVisible(true);
        setTitle("Arlako chatt window: " + currentChatter);
    }

    private class TextInputPanel extends DefaultPanel {
        private JList<Object> conversation;
        private JScrollPane conversationArea;
        private JTextArea textInput;
        private JLabel name, onlineStatus, profilePicture;
        private JButton sendButton;

        public TextInputPanel(MainWindow mainWindow, boolean showMenuBar) {
            super(mainWindow, showMenuBar);
            Dimension dimension = new Dimension(400, 50);
            textInput = new JTextArea();
            textInput.setPreferredSize(dimension);
            setVisible(true);
            name = new JLabel("Namn Namnsson");
            onlineStatus = new JLabel("Onfline");
            profilePicture = new JLabel(new ImageIcon("images//arlako"));
            conversationArea = new JScrollPane();
            sendButton = new JButton("Send");
            GridBagConstraints constraints = new GridBagConstraints();

            textInput.setBackground(Color.WHITE);
            textInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            textInput.setPreferredSize(dimension);
            conversationArea.setBackground(Color.WHITE);
            conversationArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            conversationArea.setPreferredSize(new Dimension(300, 300));

            constraints.gridy = 0;
            constraints.gridx = 0;
            constraints.gridheight = 3;
            add(conversationArea, constraints);
            constraints.gridheight = 1;

            constraints.gridy = 0;
            constraints.gridx = 1;
            add(profilePicture, constraints);

            constraints.gridy = 1;
            add(name, constraints);

            constraints.gridy = 2;
            add(onlineStatus, constraints);

            constraints.gridx = 0;
            constraints.gridy = 3;
            add(textInput, constraints);

            constraints.gridy = 3;
            constraints.gridx = 1;
            add(sendButton, constraints);

        }
        public void closeApplication(){

        }
    }
}
