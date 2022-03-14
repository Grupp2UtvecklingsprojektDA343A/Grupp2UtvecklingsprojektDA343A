package server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Traffic implements Serializable {
    private String text;
    private LocalDateTime serverReceivedTime;
    private LocalDateTime clientRecievedTime;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getServerReceivedTime() {
        return serverReceivedTime;
    }

    public void setServerReceivedTime(LocalDateTime serverReceivedTime) {
        this.serverReceivedTime = serverReceivedTime;
    }

    public LocalDateTime getClientRecievedTime() {
        return clientRecievedTime;
    }

    public void setClientRecievedTime(LocalDateTime clientRecievedTime) {
        this.clientRecievedTime = clientRecievedTime;
    }

    public static class Builder{
        private final Traffic traffic = new Traffic();

        public Traffic build(){
            return traffic;
        }

        public Builder text(String text){
            traffic.setText(text);
            return this;
        }

        public Builder serverRecieved(LocalDateTime time){
            traffic.setServerReceivedTime(time);
            return this;
        }

        public Builder clientRecieved(LocalDateTime time){
            traffic.setClientRecievedTime(time);
            return this;
        }
    }
}
