import client.control.Client;
import server.entity.Server;

public class Main {
    public static void main(String[] args)  {
        if(args.length > 0 && args[0].equals("s")) {
            startServer();
        } else if(args.length > 0 && args[0].equals("sc")) {
            startServer();
            startClient();
        } else {
            startClient();
        }
    }

    private static void startServer() {
        Server server = new Server(20008);
    }

    private static void startClient() {
        new Client().showGUI();
    }
}
