package client.control;

import client.boundary.ChatWindow;
import client.boundary.ContactsWindow;
import client.boundary.DefaultWindow;
import client.boundary.LoginWindow;
import globalEntity.User;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WindowHandler {
    private LoginWindow logInWindow;
    private ContactsWindow contactsWindow;
    private final ConcurrentHashMap<User, ChatWindow> chatWindows = new ConcurrentHashMap<>();
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
            client.logOut(null, true);
        } else {
            showWindow(contactsWindow);
        }
    }

    public void updateListOfContacts(ArrayList<User> loggedInUsers) {
        for(User user : loggedInUsers) {
            if(chatWindows.containsKey(user)) {
                chatWindows.get(user).loggedIn();
            }

            contactsWindow.addUser(user.getUsername(), user.getIcon());
        }
    }

    public void clickStar(User user) {
        contactsWindow.clickStar(user.getUsername());
    }

    // CHAT WINDOWS //
    public void openChatWindow(User user, boolean online) {
        if(isChatWindowOpen(user)) {
            focusChatWindow(user);
        } else {
            addChatWindow(user, new ChatWindow(client, user.getUsername(), user.getIcon(), this, online));
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

    public void displayMessage(User sender, String text, String time, boolean online) {
        ChatWindow chatWindow = chatWindows.get(sender);
        if(chatWindow == null) {
            openChatWindow(sender, online);
            chatWindow = chatWindows.get(sender);
        }
        String fulltext = time+": "+text;
        chatWindow.addMessage(fulltext);
    }

    public void displayImage(User sender, ImageIcon image, String time, boolean online) {
        ChatWindow chatWindow = chatWindows.get(sender);
        if(chatWindow == null) {
            openChatWindow(sender, online);
            chatWindow = chatWindows.get(sender);
        }
        chatWindow.addMessage(time+": ", image);
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

    public void displayImageAndText(User sender, ImageIcon image, String text, String text1) {
        //förberedelser för image och text i ett message
    }


    public void setToOffline(User user, boolean isFriend) {
        if(chatWindows.containsKey(user)) {
            chatWindows.get(user).loggedOut(isFriend);
        }

        contactsWindow.loggedOut(user.getUsername(), isFriend);
    }
}
