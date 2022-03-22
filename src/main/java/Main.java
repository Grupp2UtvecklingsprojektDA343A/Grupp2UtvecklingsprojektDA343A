import client.control.Client;
import server.control.Controller;
import server.entity.Server;

public class Main {
    public static void main(String[] args)  {
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
