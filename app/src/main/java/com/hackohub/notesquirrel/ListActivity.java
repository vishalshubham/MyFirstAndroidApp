package com.hackohub.notesquirrel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ListActivity extends ActionBarActivity {

    public static final String NOTE_NAME = "note_name";
    public static final int PHOTO_TAKEN = 0;
    public static final String OPTION_PRE = "option_";
    public static final String DATETIME_PRE = "datetime_";
    public static final String COUNT = "count";
    public static final String LIST_SIZE = "list_size";
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setListListner();
        setAddNoteListner();
        setExitListner();
    }

    private void setExitListner() {
        Button exitBtn = (Button)findViewById(R.id.exit_button);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListActivity.this.finish();
            }
        });
    }

    private void setAddNoteListner() {

        Button addNoteButton = (Button)findViewById(R.id.add_note_button);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setTitle("Enter the title of your new note");
                final EditText input = new EditText(ListActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("Add Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().equals("")) {
                            Toast.makeText(ListActivity.this, "You need to enter a name for the note", Toast.LENGTH_LONG).show();
                        } else {
                            saveNote();
                            Intent i = new Intent(ListActivity.this, ListActivity.class);
                            startActivity(i);
                            ListActivity.this.finish();
                        }
                    }

                    private void saveNote(){
                        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        int count = prefs.getInt(LIST_SIZE, 0);
                        count++;

                        String dateString = getFormattedDate(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                        editor.putString(OPTION_PRE + (count), input.getText().toString());
                        editor.putString(DATETIME_PRE + (count), dateString);
                        editor.putInt(LIST_SIZE, count);
                        editor.commit();

                        Log.d(NoteActivity.DEBUGTAG, LIST_SIZE + count);
                        Log.d(NoteActivity.DEBUGTAG, input.getText().toString() + dateString);
                    }

                    private String getFormattedDate(String dateString) {
                        String year = dateString.substring(0, 4);
                        String month = dateString.substring(4, 6);
                        String date = dateString.substring(6, 8);
                        String hour = dateString.substring(9, 11);
                        String minute = dateString.substring(11, 13);
                        String second = dateString.substring(13, 15);

                        return "Created on " + date + "/" + month + "/" + year + " - " + hour + ":" + minute + ":" + second;
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    private void setListListner() {

        Log.d(NoteActivity.DEBUGTAG, "Inside setListListner");
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int size = prefs.getInt(LIST_SIZE, -1);

        ArrayList<Message> messages = new ArrayList<Message>();
        if(size == -1){
            Log.d(NoteActivity.DEBUGTAG, "Size: " + size);
            editor.putInt(LIST_SIZE, 0);
            editor.commit();
        }
        else{
            Log.d(NoteActivity.DEBUGTAG, "Size: " + size);
            for(int i=1; i<=size; i++){
                String title = prefs.getString(OPTION_PRE+i, null);
                String date = prefs.getString(DATETIME_PRE+i, null);
                Log.d(NoteActivity.DEBUGTAG, title);
                messages.add(new Message(i, title, date));
            }
        }

        final MessageAdapter messageAdapter = new MessageAdapter(this, messages);
        //String[] values = messages.toArray(new String[messages.size()]);

        if(messages.size()==0){
            Log.d(NoteActivity.DEBUGTAG, "size 0");

        }
        else{
            Log.d(NoteActivity.DEBUGTAG, "size " + messages.size());
            //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, values);

            ListView listView = (ListView)findViewById(R.id.main_list);

            listView.setAdapter(messageAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                    Intent i = new Intent(ListActivity.this, NoteActivity.class);
                    i.putExtra(NOTE_NAME, messageAdapter.getTitle(position));
                    i.putExtra(COUNT, Integer.toString(position+1));
                    startActivity(i);
                    ListActivity.this.finish();
                    Log.d(NoteActivity.DEBUGTAG, "Position: " + position + "; Value: " + messageAdapter.getTitle(position) + ";");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_passpoints_reset:
                Intent i = new Intent(ListActivity.this, ImageActivity.class);
                i.putExtra(NoteActivity.RESET_PASSPOINTS, true);
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
}
