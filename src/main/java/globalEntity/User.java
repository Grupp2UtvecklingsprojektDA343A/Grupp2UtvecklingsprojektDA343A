package globalEntity;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private ImageIcon icon;

    public User(String username, ImageIcon icon){
        this.username = username;
        this.icon = icon;
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getIcon() {
        return icon;
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
