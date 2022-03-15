package client.control;

import client.boundary.DefaultWindow;
import client.boundary.LoginWindow;
import globalEntity.Message;
import globalEntity.User;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*todo
Funktionalitet för klient
Ska kunna ansluta till systemet
Avsluta sin anslutning
Skicka, ta emot och visa meddelanden (som konversation)
Innan man ansluter måste man ha skapat en användare med unikt anv id och bild.
Klienter ska ha en collection av “kontakter”, där andra användare ska kunna sparas.
Utifrån dessa ska en användare kunna välja till vem man vill skicka meddelandet.
Användare som skapas ska lagras lokalt på en fil på hårddisken
Text och bild ska matas in på ett smidigt sätt (bild väljs med JFileChooser).
Servern måste kunna hålla reda på anslutna klienter.
Servern måste kunna lagra klienter i en objektsamling.
 */

public class Client {
    private Socket socket;
    private User user;
    private ConcurrentHashMap<String, User> friendList = new ConcurrentHashMap<>();
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Message message = null;
    private InputClient inputClient;
    private OutputClient outputClient;
    private ConcurrentHashMap<String, User> currentlyOnline = new ConcurrentHashMap<>();
    private final WindowHandler windowHandler = new WindowHandler(this);
    private boolean disconnected;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    public void closeApplication() {
        try {
            outputClient.send(new Message.Builder().type(Message.LOGOUT).sender(user).build());
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stopThreads();
        // outputClient
        windowHandler.closeAllWindows();
    }

    public String[] convert(){
        String[] temp = new String[currentlyOnline.size()];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = currentlyOnline.get(i).getUsername();
        } return temp;
    }

    private void connect(String ip, int port) throws IOException {
        this.socket = new Socket(ip,port);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    public String getUsername() {
        return user.getUsername();
    }

    public boolean isFriend(String username) {
        System.out.println("loggade ut: " + username);
        System.out.println("jag är: " + user.getUsername());
        return friendList.containsKey(username);
    }

    public void logIn(String username, ImageIcon profilePicture, String host, int port, LoginWindow loginWindow) {
        new Thread(() -> {
            user = new User(username, profilePicture);
            Message newLogin = new Message.Builder().type(Message.LOGIN).sender(user).build();

            try {
                connect(host, port);
                oos.writeObject(newLogin);
                oos.flush();
                // Ta emot
                Message answer = (Message) ois.readObject();
                // 4.1 kunde inte logga in
                if (answer.getType() == Message.LOGIN_SUCCESS){
                    windowHandler.openContactsWindow(username, profilePicture);
                    new ThreadHandler(this).start();
                    windowHandler.closeLogInWindow();
                    updateListOfContacts(answer.getContacts());
                }else{
                    loginWindow.done();
                    WindowHandler.showErrorMessage(windowHandler.getLogInWindow(),"Failed loggin","loggin failed");
                    windowHandler.openLogInWindow();
                }
                // 4.2 kan logga in

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(3);
            }
        }).start();
    }

    public void logOut(DefaultWindow parent) {
        try {
            disconnect();
            stopThreads();
            windowHandler.closeAllWindows();
            disconnected = true;
            pcs.firePropertyChange("true", null, user);
        } catch (IOException e) {
            WindowHandler.showErrorMessage(parent, e.toString(), "Fel vid utloggning");
            e.printStackTrace();
        } finally {
            showGUI();
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    public void setToOnline(User user){
        if(!currentlyOnline.contains(user)) {
            currentlyOnline.put(user.getUsername(), user);
        }


        windowHandler.updateListOfContacts(new ArrayList<>(List.of(user)));
    }

    public void setToOffline(User user){
        boolean friend = isFriend(user.getUsername());
        if(friend) {
            currentlyOnline.remove(user.getUsername());
        }

        windowHandler.setToOffline(user, friend);
    }

    public void updateListOfContacts(ArrayList<User> loggedInUsers){
        ConcurrentHashMap<String, User> oldOnlineList = new ConcurrentHashMap<>(currentlyOnline);

        for(User user : oldOnlineList.values()) {
            if(!loggedInUsers.contains(user)) {
                if(friendList.containsKey(user.getUsername())) {
                    setToOffline(user);
                } else {
                    currentlyOnline.remove(user.getUsername());
                }
            }
        }

        for(User user : loggedInUsers) {
            if(!currentlyOnline.containsKey(user.getUsername())) {
                currentlyOnline.put(user.getUsername(), user);
            }
        }

        windowHandler.updateListOfContacts(loggedInUsers);
    }

    public void sendMessage(String username, String text, LocalDateTime timestamp) {
        Message message = new Message.Builder()
            .type(Message.TEXT)
            .message(text)
            .sent(timestamp)
            .sender(user)
            .receiver(currentlyOnline.get(username))
            .build();
        outputClient.send(message);
    }

    public void showGUI() {
        SwingUtilities.invokeLater(() -> {
            windowHandler.openLogInWindow();
        });
    }

    public void startChatWithUser(String username) {
        windowHandler.openChatWindow(currentlyOnline.get(username));
    }

    private void stopThreads() {
        inputClient.running = false;
    }

    public void displayMessage(User sender, String text, String time) {
        windowHandler.displayMessage(sender, text, time);
    }

    public void displayImage(User sender, ImageIcon image, String time) {
        windowHandler.displayImage(sender,image,time);
    }

    public void displayImageAndText(User sender, ImageIcon image, String text, String time) {
        windowHandler.displayImageAndText(sender,image,text,text);
    }

    public void notifyReceived() {
        Message message = new Message.Builder().type(9).receiver(this.user).received(LocalDateTime.now()).build();
        outputClient.send(message);
    }

    public void saveContact(String username, boolean isFriend) {
        if(isFriend) {
            friendList.put(username, currentlyOnline.get(username));
        } else {
            friendList.remove(username);
        }
    }

    public User getUser() {
        return user;
    }

    private class ThreadHandler extends Thread{
        private Client client;
        public ThreadHandler(Client client){
            this.client = client;
        }

        @Override
        public void run() {
            inputClient = new InputClient(client,ois);
            outputClient = new OutputClient(oos);
            inputClient.start();
        }
    }
}
