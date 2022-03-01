package main;

import client.Client;

import java.io.IOException;

public class Main {
    public static void main(String[] args)  {
        if(args.length > 0 && args[0].equals("s")) {
            // starta server (new Server();)
        } else {
            Client client = null;
            try {
                client = new Client();
                client.showGUI();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
