package main;

import client.Client;
import client.User;

import java.io.IOException;

public class Main {
    public static void main(String[] args)  {
        if(args.length > 0 && args[0].equals("s")) {
            // starta server (new Server();)
        } else {
            Client client = null;
            try {
                client = new Client("127.0.0.1",1337);
                client.showGUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
