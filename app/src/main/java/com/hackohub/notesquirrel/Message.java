package com.hackohub.notesquirrel;

/**
 * Created by Vishal on 05/07/2015.
 */
public class Message {
    private String title;
    private String datetime;
    private int id;

    public Message(int id, String title, String datetime) {
        this.title = title;
        this.datetime = datetime;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
