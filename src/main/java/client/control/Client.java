package client.control;

import client.boundary.DefaultWindow;
import entity.Message;
import entity.User;
import javax.swing.ImageIcon;
import java.io.DataInputStream;
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
    private final WindowHandler windowHandler = new WindowHandler(this);


    public void showGUI() {
        // SwingUtilities.invokeLater(() -> gui = new GUI(this));
        windowHandler.openLogInWindow();
    }

    public void logIn(String username, ImageIcon profilePicture, String host, int port) {
        this.user = new User(username, null);
        windowHandler.closeLogInWindow();
        try {
            connect(host, port);
            windowHandler.openContactsWindow(username, profilePicture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logOut(DefaultWindow parent) {
        try {
            disconnect();
        } catch (IOException e) {
            WindowHandler.showErrorMessage(parent, e.toString(), "Fel vid utloggning");
            e.printStackTrace();
        }
    }

    private void connect(String ip, int port) throws IOException {
        this.socket = new Socket(ip,port);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    public void receive(){
        try {
            message = (Message) ois.readObject();
            int type = message.getType();

            switch(type) {
                case Message.CONTACTS -> {
                    User[] loggedInUsers = message.getContacts();
                }

                case Message.TEXT -> {
                    System.out.println("bara text");
                    // mainWindow.newImageMessage(icon,sender);
                    // String sender = String.valueOf(message.getSender());
                    // String guiMessage = message.getMessage();
                    // ImageIcon icon = message.getImage();
                }

                case Message.IMAGE -> {
                    System.out.println("bara bild");
                }

                case Message.TEXT_AND_IMAGE -> {
                    System.out.println("text och bild");
                }

                default -> {
                    System.err.println("FEL?");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void newUser(String username, ImageIcon icon) throws IOException {
        oos.writeObject(new User(username,icon));
        oos.flush();
        oos.close();
    }

    public void getAllUser(){
        try(DataInputStream dis = new DataInputStream(socket.getInputStream())) {
            ArrayList<String> allUsers = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeApplication() {
        try {
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        windowHandler.closeAllWindows();
    }

    public void startChatWithUser(String username) {
        new threadHandler().start();
        windowHandler.openChatWindow(username);
    }
    private class threadHandler extends Thread{
        private InputClient inputClient;
        private OutputClient outputClient;
        @Override
        public void run() {
            while(!Thread.interrupted()){
                System.out.println("cool");
                inputClient = new InputClient(ois);
                outputClient = new OutputClient(oos);
                inputClient.start();
            }
        }
    }
}
