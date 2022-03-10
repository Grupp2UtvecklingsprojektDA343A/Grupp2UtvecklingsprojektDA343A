package client.control;

import client.boundary.DefaultWindow;
import client.boundary.GUItest;
import client.boundary.LoginWindow;
import globalEntity.Message;
import globalEntity.User;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap<String, User> friendList;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Message message = null;
    private InputClient inputClient;
    private OutputClient outputClient;
    private ArrayList<User> currentlyOnline = new ArrayList<>();
    private final WindowHandler windowHandler = new WindowHandler(this);


    public void closeApplication() {
        try {
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
            temp[i] = currentlyOnline.get(i).toString();
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
    public void newUser(String username, ImageIcon icon) throws IOException {
        oos.writeObject(new User(username,icon));
        oos.flush();
        oos.close();
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
        } catch (IOException e) {
            WindowHandler.showErrorMessage(parent, e.toString(), "Fel vid utloggning");
            e.printStackTrace();
        } finally {
            showGUI();
        }
    }
    public void setToOnline(User user){
        currentlyOnline.add(user);
        GUItest guItest = new GUItest(convert());
    }
    public void setToOffline(User user){
        currentlyOnline.remove(user);
    }
    public void showAllUsers(User[] loggedInUsers){
        windowHandler.updateListOfContacts(loggedInUsers);
    }
    public void showGUI() {
        SwingUtilities.invokeLater(() -> {
            windowHandler.openLogInWindow();
        });
    }
    public void startChatWithUser(String username) {
        windowHandler.openChatWindow(username);
    }
    private void stopThreads() {
        inputClient.running = false;
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
