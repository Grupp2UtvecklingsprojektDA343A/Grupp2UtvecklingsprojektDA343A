package main;

import client.Client;

public class Main {
    public static void main(String[] args) {
        if(args.length > 0 && args[0].equals("s")) {
            // starta server
        } else {
            Client client = new Client();
            client.showGUI();
        }
    }
}
