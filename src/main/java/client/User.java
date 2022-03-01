package client;

import javax.swing.*;

public class User {
    //todo class with username, id, profile pic
    private String username;
    private int id;
    private Icon icon;
    public User(String username,int id, Icon icon){
        this.username = username;
        this.id = id;
        this.icon = icon;
    }

    public String getUsername() {
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

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
