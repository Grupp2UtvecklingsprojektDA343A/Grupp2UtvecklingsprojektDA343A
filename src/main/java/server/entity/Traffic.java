package server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Traffic implements Serializable {
    private String text;
    private LocalDateTime eventTime;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
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

        public Builder eventTime(LocalDateTime time){
            traffic.setEventTime(time);
            return this;
        }
    }
}
