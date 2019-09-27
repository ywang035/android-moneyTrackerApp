package com.example.moneyapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private static final int TEXT_REQUEST = 1;

    private final LinkedList<String> recordList = new LinkedList<>();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        try {
            readFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get a handle to the RecyclerView.
        recyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        RecordListAdapter rAdapter = new RecordListAdapter(this, recordList);
        // Connect the adapter with the RecyclerView.
        recyclerView.setAdapter(rAdapter);
        // Give the RecyclerView a default layout manager.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    // float action button navigate to adding activity
    public void navToAddingActivity(View view) {
        Intent intent = new Intent(this, AddingActivity.class);
        startActivityForResult(intent, TEXT_REQUEST);
    }

    // receive data from adding activity
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String returnedMessage = data.getStringExtra(AddingActivity.EXTRA_REPLY);
                // get list size and add received input to record list
                recordList.addFirst(returnedMessage);
                // Notify the adapter, that the data has changed.
                recyclerView.getAdapter().notifyItemInserted(0);
                // Scroll to the bottom.
                recyclerView.smoothScrollToPosition(0);
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        writeFile(recordList, getApplicationContext());
    }

    // write to local file
    private void writeFile(LinkedList<String> data, Context context){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            for (int i = 0; i < data.size(); i++) {
                outputStreamWriter.write(data.get(i)+"\n");
            }
            outputStreamWriter.close();
        }
        catch (IOException e) {
            System.out.println("Exception File write failed: " + e.toString());
        }
    }

    // read from local file
    private void readFile() throws IOException {
        try {
            InputStream inputStream = getApplicationContext().openFileInput("config.txt");

            if ( inputStream != null ) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String readString = "";

                while ( (readString = bufferedReader.readLine()) != null ) {
                    recordList.addLast(readString);
                }
                inputStream.close();
            }

        }
        catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("file cant read");
        }
    }
}
