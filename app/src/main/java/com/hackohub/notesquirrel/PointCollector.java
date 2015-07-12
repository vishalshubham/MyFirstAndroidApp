package com.hackohub.notesquirrel;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal on 23/06/2015.
 */
public class PointCollector implements View.OnTouchListener{

    public static final int NUM_POINTS = 4;
    private PointCollectorListner listner;
    private List<Point> points = new ArrayList<Point>();

    public void setListner(PointCollectorListner listner) {
        this.listner = listner;
    }

    public boolean onTouch(View v, MotionEvent event) {

        int x = Math.round(event.getX());
        int y = Math.round(event.getY());

        String message = String.format("Coordinates: (%d, %d)", x, y);
        Log.d(NoteActivity.DEBUGTAG, message);

        points.add(new Point(x, y));
        Log.d(NoteActivity.DEBUGTAG, "Point added" + points.size());

        if(points.size()==NUM_POINTS){

            if (listner!=null){
                Log.d(NoteActivity.DEBUGTAG, "4 Points collected");
                listner.pointsCollected(points);
                //points.clear();
            }
        }
        return false;
    }

    public void clear(){
        points.clear();
    }
}
