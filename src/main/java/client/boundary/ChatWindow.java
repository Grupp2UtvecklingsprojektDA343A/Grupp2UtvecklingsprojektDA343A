package client.boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ChatWindow extends JFrame implements IChat {

    public ChatWindow(MainWindow mainWindow, String currentChatter) {
        // Dimension dimension = new Dimension(500, 500);
        // setSize(dimension);
        GridLayout gridLayout = new GridLayout(1,1);
        setLayout(gridLayout);
        DefaultPanel panel = new TextInputPanel(mainWindow, true);
        add(panel);
        setVisible(true);
        setTitle("Arlako chatt window: " + currentChatter);
        pack();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                mainWindow.removeChatWindow(currentChatter);
                dispose();
            }
        });
    }

    @Override
    public void showMessage() {

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
                getMainWindow().uploadFile();
            });
            addToFileMenu(uploadFile);

            JMenuItem showContacts = new JMenuItem("Contacts");
            showContacts.addActionListener(l -> {
                getMainWindow().open();
            });
            addToFileMenu(showContacts);

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

        }
        public void closeApplication(){

        }
    }
}
