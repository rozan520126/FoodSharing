package models;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatMessage {
    private String message;
    private String uid;
    private String pid;
    private long time;


    public ChatMessage(String message,String uid,String pid){
        this.message = message;
        this.uid = uid;
        this.pid = pid;
        time = new Date().getTime();

    }
    public ChatMessage(){}

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
