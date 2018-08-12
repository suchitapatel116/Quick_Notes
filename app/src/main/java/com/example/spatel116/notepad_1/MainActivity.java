package com.example.spatel116.notepad_1;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView tvTitle;
    private TextView tvDateTime;
    private EditText edDescription;

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ");

        tvTitle = (TextView)findViewById(R.id.tv_title);
        tvDateTime = (TextView)findViewById(R.id.tv_date);
        edDescription = (EditText)findViewById(R.id.ed_description);

        String formattedDate = new SimpleDateFormat("EEE MMM d, HH:mm a").format(Calendar.getInstance().getTime());
        tvDateTime.setText(formattedDate);
    }

    @Override
    protected void onPause()
    {
        Log.d(TAG, "onPause: ");

        //For initial stage the item found would be null as file is not created and there is no data present, so initialize the item.
        if(item == null)
            item = new Item();

        //When activity pause, ie. goes to some other state then store the current date and time indicating last update time.
        String formattedDate = new SimpleDateFormat("EEE MMM d, HH:mm a").format(Calendar.getInstance().getTime());
        item.setDateTime(formattedDate);
        item.setNote(edDescription.getText().toString());
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        Log.d(TAG, "onStop: ");
        if(!edDescription.getText().toString().equals(null))
            saveItem();
        super.onStop();
    }

    private void saveItem()
    {
        Log.d(TAG, "saveItem: Saving data in JSON file");
        try
        {
            FileOutputStream outStrm = getApplicationContext().openFileOutput(getResources().getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter jWriter = new JsonWriter(new OutputStreamWriter(outStrm, getResources().getString(R.string.encoding)));

            jWriter.setIndent("     ");
            jWriter.beginObject();

            jWriter.name("date_time").value(item.getDateTime());
            jWriter.name("note").value(item.getNote());

            jWriter.endObject();
            jWriter.close();


            Toast.makeText(this, getString(R.string.note_saved), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Log.d(TAG, "saveItem: Exception while saving the file");
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume()
    {
        Log.d(TAG, "onResume: Load the file");
        item = loadFromFile();
        if(item != null)
        {
            //It saved item found then display existing details
            tvDateTime.setText(item.getDateTime());
            edDescription.setText(item.getNote());
            tvTitle.setText(getString(R.string.dt_title_lastUpdate));
        }
        else
        {
            //If file not found then it is first time, so display current date and time
            tvTitle.setText(getResources().getString(R.string.dt_title_curr));
            String formattedDate = new SimpleDateFormat("EEE MMM d, HH:mm a").format(Calendar.getInstance().getTime());
            tvDateTime.setText(formattedDate);
        }
        super.onResume();
    }

    private Item loadFromFile() 
    {
        Log.d(TAG, "loadFromFile: Fetch the data from json file");
        try
        {
            InputStream inpStrm = getApplicationContext().openFileInput(getResources().getString(R.string.file_name));
            JsonReader jReader = new JsonReader(new InputStreamReader(inpStrm, getResources().getString(R.string.encoding)));

            Log.d(TAG, "loadFromFile: before while");
            //Create new item when file is successfully created
            item = new Item();

            jReader.beginObject();
            while(jReader.hasNext())
            {
                //Get the name of next tag in json file
                String tag_name = jReader.nextName();
                if(tag_name.equals("date_time"))
                    item.setDateTime(jReader.nextString());
                else if(tag_name.equals("note"))
                    item.setNote(jReader.nextString());
                else
                    //If some other tag found then skip it.
                    jReader.skipValue();
            }
            jReader.endObject();
        }
        catch (FileNotFoundException e)
        {
            Log.d(TAG, "loadFromFile: File not found exception");
            item = null;
        }
        catch (Exception e)
        {
            Log.d(TAG, "loadFromFile: Exception");
            e.printStackTrace();
            item = null;
        }
        return item;
    }

}
