import client.control.Client;
import server.control.Controller;
import server.entity.Server;

public class Main {
    public static void main(String[] args)  {
        // if(args.length > 0 && args[0].equals("s")) {
        //     startServer();
        // } else if(args.length > 0 && args[0].equals("sc")) {
        //     startServer();
        //     startClient();
        // } else {
        //     startClient();
        // }
        startServer();
        startClient();
        startClient();
    }

    private static void startServer() {
        Controller controller = new Controller();
    }

    private static void startClient() {
        new Client().showGUI();
    }
}
