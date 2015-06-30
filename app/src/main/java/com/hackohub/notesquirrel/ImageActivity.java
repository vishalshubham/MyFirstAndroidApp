package com.hackohub.notesquirrel;

// Created by: Vishal Chaudhary

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;


public class ImageActivity extends ActionBarActivity implements PointCollectorListner{

    private static final String PASSWORD_SET = "PASSWORD_SET";
    private static final int POINT_CLOSENESS = 40;
    private PointCollector pointCollector = new PointCollector();
    private Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        addImageTouchListener();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean passpointsSet = prefs.getBoolean(PASSWORD_SET, false);

        if(!passpointsSet){
            showSetPasspointsPrompt();
        }

        pointCollector.setListner(this);
    }

    public void showSetPasspointsPrompt(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setTitle("Create your Passpoint Sequence");
        builder.setMessage("Touch any 4 points and remember them for future login");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addImageTouchListener(){
        ImageView image = (ImageView)findViewById(R.id.touch_image);
        image.setOnTouchListener(pointCollector);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void savePasspoints(final List<Point> savedPoints){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.storing_data);

        final AlertDialog dialog = builder.create();
        dialog.show();

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d(MainActivity.DEBUGTAG, "Point Saved : " + savedPoints.size());
                db.storePoints(savedPoints);
                Log.d(MainActivity.DEBUGTAG, "Point Saved : " + savedPoints.size());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(PASSWORD_SET, true);
                editor.commit();

                pointCollector.clear();
                dialog.dismiss();
            }

        };
        task.execute();
    }

    private void verifyPasspoints(final List<Point> touchedPoints){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.checking_passpoints);
        final AlertDialog dialog = builder.create();
        dialog.show();

        AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPostExecute(Boolean pass) {
                dialog.dismiss();
                pointCollector.clear();

                if(pass){
                    Intent i = new Intent(ImageActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(ImageActivity.this, "Access Denied", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                List<Point> savedPoints = db.getPoints();
                Log.d(MainActivity.DEBUGTAG, "Loaded points: " + savedPoints.size());

                if(savedPoints.size()!=PointCollector.NUM_POINTS || touchedPoints.size()!=PointCollector.NUM_POINTS){
                    return false;
                }

                for( int i=0; i<PointCollector.NUM_POINTS; i++){
                    Point savedPoint = savedPoints.get(i);
                    Point touchedPoint = touchedPoints.get(i);

                    int xDiff = savedPoint.x - touchedPoint.x;
                    int yDiff = savedPoint.y - touchedPoint.y;

                    int distSquared = xDiff*xDiff + yDiff*yDiff;

                    Log.d(MainActivity.DEBUGTAG, "Distance Squared : " + distSquared);

                    if(distSquared>POINT_CLOSENESS*POINT_CLOSENESS){
                        return false;
                    }
                }

                return true;
            }
        };
        task.execute();
    }

    @Override
    public void pointsCollected(final List<Point> points) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean passpointsSet = prefs.getBoolean(PASSWORD_SET, false);

        if(!passpointsSet){
            Log.d(MainActivity.DEBUGTAG, "Saving Passpoints ..." + points.size());
            savePasspoints(points);
        }
        else{
            Log.d(MainActivity.DEBUGTAG, "Verifying Passpoints ...");
            verifyPasspoints(points);
        }
    }
}
