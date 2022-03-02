import client.controller.Client;
import client.model.User;

import java.io.IOException;

public class Main {
    public static void main(String[] args)  {
        if(args.length > 0 && args[0].equals("s")) {
            // starta server (new Server();)
        } else {
            Client client = null;
            client = new Client();
            client.showGUI();
        }
    }
}
