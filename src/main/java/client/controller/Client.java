package client.controller;

import sharedModel.Message;
import client.model.User;
import client.view.MainWindow;
/*
Agerar som Controller
 */

import javax.swing.*;
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
    private MainWindow mainWindow;

    private Socket socket;
    private String ip;
    private int port;

    private User user;
    private HashMap<String, User> frinendList;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private Message message = null;

    public Client(String ip, int port) throws IOException {
        this.ip = ip; //todo insert values when server is up and running
        this.port = port;
        this.user= user;
        //connect();
    }

    public void showGUI() {
        // SwingUtilities.invokeLater(() -> gui = new GUI(this));
        mainWindow = new MainWindow(this);
    }

    public void logIn() {
        String username = null;
        ImageIcon profilePicture = null;
        mainWindow.showContacts(username, profilePicture);
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
            oos.writeObject(message);
            oos.flush();
            disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void recieve(){
        try {
            message = (Message) ois.readObject();
            String guiMessage = message.getMessage();
            Icon icon = message.getImage();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void newUser(String username, Icon icon) throws IOException {
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
}
