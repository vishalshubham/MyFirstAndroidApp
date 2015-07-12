package com.hackohub.notesquirrel;

/**
 * Created by Vishal on 11/07/2015.
 */
public class NoteData {
    private int note_id;
    private String note_name;
    private String note_date;


    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getNote_name() {
        return note_name;
    }

    public void setNote_name(String note_name) {
        this.note_name = note_name;
    }

    public String getNote_date() {
        return note_date;
    }

    public void setNote_date(String note_date) {
        this.note_date = note_date;
    }
}
