package client.boundary;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LogInPanel extends DefaultPanel implements KeyListener {
    private final JButton bLogin = new JButton("login");

    public LogInPanel(MainWindow mainWindow) {
        super(mainWindow, false);
        setLayout(new GridBagLayout());
        Dimension jTextFieldPreferredSize = new Dimension(100, 19);

        JLabel lUsername = new JLabel("Username: ");
        JLabel lHost = new JLabel("Host: ");
        JLabel lPort = new JLabel("Port: ");
        JTextField tfUsername = new JTextField("Mr. Kaffe");
        JTextField tfHost = new JTextField("localhost");
        JTextField tfPort = new JTextField("1337");

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
                getMainWindow().logIn(tfUsername.getText(), tfHost.getText(), port);
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
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(1, 5, 1, 5);


        constraints.gridy = 0; // rad
        constraints.gridx = 0; // column
        // constraints.gridwidth = 1;
        add(lUsername, constraints);

        constraints.gridy = 0; // rad
        constraints.gridx = 1; // column
        // constraints.gridwidth = 2;
        add(tfUsername, constraints);

        constraints.gridy = 1; // rad
        constraints.gridx = 0; // column
        // constraints.gridwidth = 1;
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

        tfUsername.addKeyListener(this);
        tfHost.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            bLogin.doClick();
        } else if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
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
}
