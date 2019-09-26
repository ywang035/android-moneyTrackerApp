package com.example.moneyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private static final int TEXT_REQUEST = 1;
    private static final String LIST_KEY = "rList";
    private final LinkedList<String> recordList = new LinkedList<>();

    private RecyclerView recyclerView;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.moneyapp";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        readFile();

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
                String returnedMessage =
                        data.getStringExtra(AddingActivity.EXTRA_REPLY);


                // get list size and add received input to record list
                int recordListSize = recordList.size();
                recordList.addFirst(returnedMessage);

                System.out.println("list @ adding: " + recordList);

                // Notify the adapter, that the data has changed.
                recyclerView.getAdapter().notifyItemInserted(0);
                // Scroll to the bottom.
                recyclerView.smoothScrollToPosition(0);
            }
        }
    }

    // write to internal storage
    @Override
    protected void onPause(){
        super.onPause();
//        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
//        preferencesEditor.putString(LIST_KEY, String.valueOf(recordList));
//        preferencesEditor.apply();
    }

    // read shared preference (saved data)
    private void readFile(){

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        String str = mPreferences.getString(LIST_KEY, null);
        System.out.println("read in: " + str);
        System.out.println("read in length: " + str.length());

        if(str.length()!=2){
            // remove [ and ] from reading

            String recordIn = mPreferences.getString(LIST_KEY,null);
            String recordEdit = recordIn.substring(1, recordIn.length()-1);

            String[] arrOfStr = recordEdit.split(", ");

            for (String a : arrOfStr){
                    recordList.addLast(a);
            }
//            System.out.println("list @ reading: " + recordList);
        }
    }
}
