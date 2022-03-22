package client.control;

import globalEntity.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class OutputClient{
    private final ObjectOutputStream oos;

    public OutputClient(ObjectOutputStream oos) {
        this.oos=oos;
    }
    public void send(Message message){
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
