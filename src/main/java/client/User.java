package client;

import javax.swing.*;

public class User {
    //todo class with username, id, profile pic
    private String username;
    private int id;
    public User(String username,int id){
        this.username = username;
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
