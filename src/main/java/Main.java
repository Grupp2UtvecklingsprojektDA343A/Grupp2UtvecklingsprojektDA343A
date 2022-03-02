import client.controller.Client;

public class Main {
    public static void main(String[] args)  {
        if(args.length > 0 && args[0].equals("s")) {
            startServer();
        } else if(args.length > 0 && args[0].equals("sc")) {
            startServer();
            startClient();
        }
        else {
            startClient();
        }
    }

    private static void startServer() {
        // starta server (new Server();)
    }

    private static void startClient() {
        Client client = null;
        client = new Client();
        client.showGUI();
    }
}
