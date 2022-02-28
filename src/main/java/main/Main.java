package main;

import client.Client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length > 0 && args[0].equals("s")) {
            // starta server
        } else {
            Client client = new Client();
            client.showGUI();
        }
    }
}
