package globalEntity;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {
    //todo class with username, id, profile pic
    private String username;

    private Icon icon;
    private boolean loggedIn;
    public User(String username, Icon icon){
        this.username = username;

        this.icon = icon;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Icon getIcon() {
        return icon;
    }
    public void setIcon(Icon icon) {
        this.icon = icon;
    }
    public boolean getLoggedIn(){
        return loggedIn;
    }
    public void setLoggedIn(boolean status){
        this.loggedIn = status;
    }
}