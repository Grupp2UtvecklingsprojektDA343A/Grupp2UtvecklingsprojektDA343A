package client.control;

import client.boundary.ChatWindow;
import client.boundary.ContactsWindow;
import client.boundary.DefaultWindow;
import client.boundary.ImageHandler;
import client.boundary.LoginWindow;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.HashMap;

public class WindowHandler {
    private LoginWindow logInWindow;
    private ContactsWindow contactsWindow;
    private final HashMap<String, ChatWindow> chatWindows = new HashMap<>();
    private Client client;

    public WindowHandler(Client client) {
        this.client = client;
    }

    // LOG IN WINDOWS //
    public void openLogInWindow() {
        if(logInWindow == null) {
            logInWindow = new LoginWindow(client, true);
        }

        showWindow(logInWindow);
    }

    public void closeLogInWindow() {
        if(logInWindow != null) {
            logInWindow.dispose();
        }
    }

    // CONTACT WINODWS //
    public void openContactsWindow(String username, ImageIcon profilePicture) {
        if(contactsWindow == null) {
            contactsWindow = new ContactsWindow(client, this, true, username, profilePicture);
        }

        showWindow(contactsWindow);
        contactsWindow.addUser("kaffe", ImageHandler.createImageIcon("/arlako.png", 40, 40));
    }

    public void showContactWindow() {
        showWindow(contactsWindow);
    }

    public void contactsWindowClosed() {
        if(chatWindows.size() == 0) {
            client.closeApplication();
        } else {
            showWindow(contactsWindow);
        }
    }

    // CHAT WINDOWS //
    public void openChatWindow(String username) {
        if(isChatWindowOpen(username)) {
            focusChatWindow(username);
        } else {
            addChatWindow(username, new ChatWindow(client, username, this));
        }
    }

    public boolean isChatWindowOpen(String username) {
        return chatWindows.containsKey(username);
    }

    public void focusChatWindow(String username) {
        showWindow(chatWindows.get(username));
    }

    public void addChatWindow(String username, ChatWindow chatWindow) {
        chatWindows.put(username, chatWindow);
        focusChatWindow(username);
    }

    public void removeChatWindow(String username) {

    }

    // GLOBAL
    public void closeAllWindows() {
        if(logInWindow != null) {
            logInWindow.dispose();
        }

        if(contactsWindow != null) {
            contactsWindow.dispose();
        }

        for(String user : chatWindows.keySet()) {
            chatWindows.get(user).dispose();
        }
    }

    private void showWindow(JFrame window) {
        window.setVisible(true);
        window.requestFocus();
    }

    // STATIC
    public static void showErrorMessage(DefaultWindow parent, String errorMessage, String title) {
        JOptionPane.showMessageDialog(parent, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }


}
