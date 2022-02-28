package client;

import javax.swing.*;

public class User {
    //todo class with username, id, profile pic
    private String username;
    private int id;
    private Icon profilePic;

    public User (String username,int id,Icon icon){
        this.username=username;
        this.id=id;
        this.profilePic = icon;
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

    public Icon getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Icon profilePic) {
        this.profilePic = profilePic;
    }
}
