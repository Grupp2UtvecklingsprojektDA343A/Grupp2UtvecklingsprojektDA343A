package client.control;

import client.boundary.ChatWindow;
import client.boundary.ContactsWindow;
import client.boundary.DefaultWindow;
import client.boundary.ImageHandler;
import client.boundary.LoginWindow;
import globalEntity.User;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;

public class WindowHandler {
    private LoginWindow logInWindow;
    private ContactsWindow contactsWindow;
    private final HashMap<User, ChatWindow> chatWindows = new HashMap<>();
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

     public LoginWindow getLogInWindow() {
        return logInWindow;
    }

    // CONTACT WINODWS //
    public void openContactsWindow(String username, ImageIcon profilePicture) {
        if(contactsWindow == null) {
            contactsWindow = new ContactsWindow(client, this, true, username, profilePicture);
        }

        showWindow(contactsWindow);
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

    public void updateListOfContacts(ArrayList<User> loggedInUsers) {
        for(User user : loggedInUsers) {
            if(!user.getUsername().equals(client.getUsername())) {
                contactsWindow.addUser(user.getUsername(), user.getIcon());
            }
        }
    }

    // CHAT WINDOWS //
    public void openChatWindow(User user) {
        if(isChatWindowOpen(user)) {
            focusChatWindow(user);
        } else {
            addChatWindow(user, new ChatWindow(client, user.getUsername(), user.getIcon(), this));
        }
    }

    public boolean isChatWindowOpen(User user) {
        return chatWindows.containsKey(user);
    }

    public void focusChatWindow(User user) {
        showWindow(chatWindows.get(user));
    }

    public void addChatWindow(User user, ChatWindow chatWindow) {
        chatWindows.put(user, chatWindow);
        focusChatWindow(user);
    }

    public void removeChatWindow(String username) {
        chatWindows.remove(new User(username, null));
    }

    public void displayMessage(User sender, String text, String time) {
        ChatWindow chatWindow = chatWindows.get(sender);
        if(chatWindow == null) {
            openChatWindow(sender);
            chatWindow = chatWindows.get(sender);
        }
        String fulltext = time+": "+text;
        chatWindow.addMessage(fulltext);
    }

    // GLOBAL
    public void closeAllWindows() {
        if(logInWindow != null) {
            logInWindow.dispose();
            logInWindow = null;
        }

        if(contactsWindow != null) {
            contactsWindow.dispose();
            contactsWindow = null;
        }

        for(User user : chatWindows.keySet()) {
            chatWindows.get(user).dispose();
        }
        chatWindows.clear();
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
