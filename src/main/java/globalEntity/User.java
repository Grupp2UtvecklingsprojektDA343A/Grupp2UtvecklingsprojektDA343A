package globalEntity;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {
    //todo class with username, id, profile pic
    private String username;

    private ImageIcon icon;
    private boolean loggedIn;
    public User(String username, ImageIcon icon){
        this.username = username;

        this.icon = icon;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public ImageIcon getIcon() {
        return icon;
    }
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    public boolean getLoggedIn(){
        return loggedIn;
    }
    public void setLoggedIn(boolean status){
        this.loggedIn = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
