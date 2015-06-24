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

    private PointCollectorListner listner;
    private List<Point> points = new ArrayList<Point>();

    public void setListner(PointCollectorListner listner) {
        this.listner = listner;
    }

    public boolean onTouch(View v, MotionEvent event) {

        int x = Math.round(event.getX());
        int y = Math.round(event.getY());

        String message = String.format("Coordinates: (%d, %d)", x, y);
        Log.d(MainActivity.DEBUGTAG, message);
        //Toast.makeText(ImageActivity.this, message, Toast.LENGTH_LONG).show();

        points.add(new Point(x, y));

        if(points.size()==4){

            if (listner!=null){
                listner.pointsCollected(points);
            }
        }


        return false;
    }
}
