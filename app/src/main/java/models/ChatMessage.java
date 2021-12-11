package models;

import java.util.Date;

public class ChatMessage {
    private String message;
    private long time;
    private String uid;

    public ChatMessage(String message,String uid){
        this.message = message;
        time = new Date().getTime();
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
