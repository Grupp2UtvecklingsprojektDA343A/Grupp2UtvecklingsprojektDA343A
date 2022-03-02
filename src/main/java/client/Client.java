package client;

import Model.Message;
import client.gui.MainWindow;
/*
Agerar som Controller
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    private MainWindow mainWindow;

    private Socket socket;
    private String ip;
    private int port;

    private User user;
    private HashMap<Integer, User> friendList;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    public Client() throws IOException {
        this.ip = ""; //todo insert values when server is up and running
        this.port = 0;
    }

    public void showGUI() {
        // SwingUtilities.invokeLater(() -> gui = new GUI(this));
        mainWindow = new MainWindow(this);
    }

    public void logIn() {
        mainWindow.showContacts();
    }

    public void connect() throws IOException {
        this.socket = new Socket(ip,port);
        this.oos = new ObjectOutputStream(socket.getOutputStream());
        this.ois = new ObjectInputStream(socket.getInputStream());
    }

    public void disconnect() throws IOException {
        socket.close();
    }

    public void send(Message message){
        // Skriv en motod som skickar Till servern
        try {
            connect();
            oos.writeObject(message);
            oos.flush();
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void recieve(){
        try {
            connect();
            Message message = (Message) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
