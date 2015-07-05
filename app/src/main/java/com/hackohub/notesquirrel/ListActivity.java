package com.hackohub.notesquirrel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;


public class ListActivity extends ActionBarActivity {

    public static final String NOTE_NAME = "note_name";

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
                            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            int count = prefs.getInt("list_size", 0);
                            editor.putString("option_" + (++count), input.getText().toString());
                            editor.putInt("list_size", count);
                            editor.commit();
                            Log.d(NoteActivity.DEBUGTAG, "list_size" + count);
                            Log.d(NoteActivity.DEBUGTAG, input.getText().toString());
                            Intent i = new Intent(ListActivity.this, ListActivity.class);
                            startActivity(i);
                            ListActivity.this.finish();
                        }
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
        int size = prefs.getInt("list_size", -1);

        ArrayList<String> options = new ArrayList<String>();
        if(size == -1){
            Log.d(NoteActivity.DEBUGTAG, "Size: " + size);
            editor.putInt("list_size", 0);
            editor.commit();
        }
        else{
            Log.d(NoteActivity.DEBUGTAG, "Size: " + size);
            for(int i=1; i<=size; i++){
                String str = prefs.getString("option_"+i, null);
                Log.d(NoteActivity.DEBUGTAG, str);
                options.add(str);
            }
        }

        String[] values = options.toArray(new String[options.size()]);

        if(options.size()==0){
            Log.d(NoteActivity.DEBUGTAG, "size 0");

        }
        else{
            Log.d(NoteActivity.DEBUGTAG, "size " + options.size());
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

            ListView listView = (ListView)findViewById(R.id.main_list);

            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                    Intent i = new Intent(ListActivity.this, NoteActivity.class).putExtra(NOTE_NAME, adapter.getItemAtPosition(position).toString());
                    startActivity(i);
                    ListActivity.this.finish();
                    Log.d(NoteActivity.DEBUGTAG, "Position: " + position + "; Value: " + adapter.getItemAtPosition(position) + ";");
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
