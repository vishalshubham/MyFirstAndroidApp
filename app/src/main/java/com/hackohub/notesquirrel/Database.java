package com.hackohub.notesquirrel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal on 23/06/2015.
 */
public class Database extends SQLiteOpenHelper {

    private static final String POINT_TABLE = "DB_POINTS";
    private static final String COL_ID = "ID";
    private static final String COL_X = "X";
    private static final String COL_Y = "Y";
    private static final String NOTE_TABLE = "DB_NOTE_TABLE";
    private static final String COL_NOTE = "NOTE";
    private static final String COL_DATE = "DATE";

    public Database(Context context) {
        super(context, "note.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s INTEGER PRIMARY KEY, %s INTEGER NOT NULL, %s INTEGER NOT NULL)", POINT_TABLE, COL_ID, COL_X, COL_Y);
        db.execSQL(sql);
        sql = String.format("create table %s (%s INTEGER PRIMARY KEY, %s VARCHAR NOT NULL, %s VARCHAR NOT NULL)", NOTE_TABLE, COL_ID, COL_NOTE, COL_DATE);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void storePoints(List<Point> points){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(POINT_TABLE, null, null);
        int i=0;

        for(Point point: points){
            ContentValues values = new ContentValues();

            Log.d(NoteActivity.DEBUGTAG, "Point: " + point.x + " - " + point.y);

            values.put(COL_ID, ++i);
            values.put(COL_X, point.x);
            values.put(COL_Y, point.y);

            db.insert(POINT_TABLE, null, values);
        }
        db.close();
    }

    public void storeNote(NoteData noteData){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        Log.d(NoteActivity.DEBUGTAG, "Id: " + noteData.getNote_id() + " - Note: " + noteData.getNote_name() + " - Date: " + noteData.getNote_date());

        values.put(COL_ID, noteData.getNote_id());
        values.put(COL_NOTE, noteData.getNote_name());
        values.put(COL_DATE, noteData.getNote_date());
        db.insert(NOTE_TABLE, null, values);

        db.close();
    }

    public List<Point> getPoints(){
        List<Point> points = new ArrayList<Point>();

        SQLiteDatabase db = getWritableDatabase();

        String sql = String.format("select * from %s order by %s", POINT_TABLE, COL_ID);

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            int x = cursor.getInt(1);
            int y = cursor.getInt(2);

            points.add(new Point(x, y));
        }

        db.close();
        return points;
    }

    public List<NoteData> getNotes(){
        List<NoteData> notes = new ArrayList<NoteData>();

        SQLiteDatabase db = getWritableDatabase();

        String sql = String.format("select * from %s order by %s", NOTE_TABLE, COL_ID);

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            NoteData noteData = new NoteData();
            String note_name = cursor.getString(1);
            String note_date = cursor.getString(2);
            noteData.setNote_name(note_name);
            noteData.setNote_date(note_date);
            notes.add(noteData);
        }

        db.close();
        return notes;
    }

    public int getTotalNotes(){

        SQLiteDatabase db = getWritableDatabase();

        String sql = String.format("select count(*) from %s", NOTE_TABLE);

        Cursor cursor = db.rawQuery(sql, null);

        int total_notes = cursor.getInt(0);

        db.close();
        return total_notes;
    }
}
