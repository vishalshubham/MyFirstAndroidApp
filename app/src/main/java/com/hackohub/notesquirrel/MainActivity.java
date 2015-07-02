package com.hackohub.notesquirrel;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    public static final String TEXTFILE = "NoteSquirrel.txt";
    public static final String FILEPREF = "Username Saved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLoginButtonListner();
        setContent();
    }

    public void setLoginButtonListner(){
        Button saveBtn = (Button)findViewById(R.id.login);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = (EditText)findViewById(R.id.username);
                String text = editText.getText().toString();
                EditText editText1 = (EditText)findViewById(R.id.password);
                String text1 = editText1.getText().toString();

                if (text.isEmpty() == true || text1.isEmpty() == true){
                    Toast.makeText(MainActivity.this, R.string.empty_login, Toast.LENGTH_LONG).show();
                    Log.d(NoteActivity.DEBUGTAG, "Blank login credentials");
                }
                else {
                    try {
                        FileOutputStream fos = openFileOutput(TEXTFILE, Context.MODE_PRIVATE);
                        fos.write(text.getBytes()); // Commented out to check below preferences functionality
                        fos.close();
                        Log.d(NoteActivity.DEBUGTAG, "Login Button Clicked" + text);

//                    SharedPreferences prefs = getPreferences(MODE_PRIVATE);    Either use FileInputStream internal storage(Above code) or Preferences(this code)
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putString(FILEPREF, text);
//                    editor.commit();
                    } catch (Exception e) {
                        Log.d(NoteActivity.DEBUGTAG, "Unable to save file" + e);
                    }
                }
            }
        });
    }


    public void setContent(){
        try {
            FileInputStream fis = openFileInput(TEXTFILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));

            String line;
            EditText editText = (EditText)findViewById(R.id.username);

            while ((line = reader.readLine())!=null){
                editText.append(line); // Commented out to check below preferences functionality
            }
            fis.close();
//            SharedPreferences prefs = getPreferences(MODE_PRIVATE);    Either use FileInputStream internal storage(Above code) or Preferences(this code)
//            editText.append(prefs.getString(FILEPREF, "Username"));
        } catch (Exception e) {
            Log.d(NoteActivity.DEBUGTAG, "Unable to read file" + e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
