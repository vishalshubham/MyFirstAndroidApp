package com.hackohub.notesquirrel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;


public class NoteActivity extends ActionBarActivity {

    public static final String DEBUGTAG = "VC";
    public static final String TEXTFILE = "notesquirrel.txt";
    public static final String FILESAVED = "FileSaved";
    public static final String RESET_PASSPOINTS = "ResetPasspoints";
    public static final int PHOTO_TAKEN = 0;
    private File imageFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        addSaveButtonListner();
        addLockButtonListner();

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        boolean fileSaved = prefs.getBoolean(FILESAVED, false);

        if(fileSaved){
            loadSavedFile();
        }
    }

    private void loadSavedFile(){
        try {
            FileInputStream fis = openFileInput(TEXTFILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));

            EditText editText = (EditText)findViewById(R.id.text);
            String line;

            while((line=reader.readLine())!=null){
                editText.append(line);
                editText.append("\n");
            }

        } catch (Exception e) {
            Log.d(DEBUGTAG, "Unable to read file");
        }
    }

    private void addLockButtonListner(){
        Button lockBtn = (Button)findViewById(R.id.lock_button);

        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.text);
                String text = editText.getText().toString();

                try {
                    Intent i = new Intent(NoteActivity.this, ImageActivity.class);
                    startActivity(i);
                } catch (Exception e) {
                    Log.d(DEBUGTAG, "Unable to lock");
                }
            }
        });
    }

    private void addSaveButtonListner(){
        Button saveBtn = (Button)findViewById(R.id.save_button);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.text);
                String text = editText.getText().toString();

                try {
                    FileOutputStream fos = openFileOutput(TEXTFILE, Context.MODE_PRIVATE);
                    fos.write(text.getBytes());
                    fos.close();
                    Toast.makeText(NoteActivity.this, R.string.note_saved, Toast.LENGTH_LONG).show();
                    SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(FILESAVED, true);
                    editor.commit();
                } catch (Exception e) {
                    Log.d(DEBUGTAG, "Unable to save file");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.menu_passpoints_reset:
                Intent i = new Intent(NoteActivity.this, ImageActivity.class);
                i.putExtra(RESET_PASSPOINTS, true);
                startActivity(i);
                return true;
            case R.id.menu_loginimage_reset:
                File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                imageFile = new File(picturesDirectory, "passpoints_image");

                Intent i2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(i2, PHOTO_TAKEN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PHOTO_TAKEN){
            Bitmap photo = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

            if(photo!=null){

            }
            else{
                Toast.makeText(this, R.string.unable_to_save, Toast.LENGTH_LONG).show();
            }
        }
    }
}
