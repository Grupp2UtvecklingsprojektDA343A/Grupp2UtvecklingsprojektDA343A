package client;

import client.gui.MainWindow;
/*
Agerar som Controller
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

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

    public void send(){

    }

    public void recieve(){

    }
}
