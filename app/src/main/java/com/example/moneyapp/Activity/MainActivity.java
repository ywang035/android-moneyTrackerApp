package com.example.moneyapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneyapp.Helper.AssetData;
import com.example.moneyapp.Helper.EventMessage;
import com.example.moneyapp.Fragment.FilterDialogFragment;
import com.example.moneyapp.Fragment.MonthPickerFragment;
import com.example.moneyapp.R;
import com.example.moneyapp.Helper.TransactionData;
import com.example.moneyapp.Adapter.TransactionListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int TEXT_REQUEST = 1;

    private List<TransactionData> transactionList = new ArrayList<>();
    private List<AssetData> assetList = new ArrayList<>();

    private RecyclerView recyclerView_transaction;
    private FloatingActionButton mFloatingActionButton;
    private DrawerLayout drawerLayout;
    private TransactionListAdapter transactionAdapter;
    private Button resetFilterBtn;

    private TextView toolbarNet;
    private TextView toolbarOut;
    private TextView toolbarIn;
    private TextView toolbarHome;
    private TextView toolBarDate;


    private String book1TransactionList = "transaction_data_1.txt";
    private String book1AssetList = "asset_data_1.txt";
    private String book2TransactionList = "transaction_data_2.txt";
    private String book2AssetList = "asset_data_2.txt";

    private String saveTransactionList = "transaction_data_1.txt";
    private String saveAssetList = "asset_data_1.txt";

    Double out = 0.0;
    Double in = 0.0;
    Double net = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toobar handling
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(null);

        // set up xml element in toolbar
        toolbarNet = findViewById(R.id.toolbar_net);
        toolbarOut = findViewById(R.id.toolbar_out);
        toolbarIn = findViewById(R.id.toolbar_in);
        toolbarHome = findViewById(R.id.toolbar_title);
        toolBarDate = findViewById(R.id.toolbar_date);

        // set month dropdown
        Calendar calendar = Calendar.getInstance();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);
        toolBarDate.setText(month + " " + year);


        // side navigation (drawer) handling
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.navi_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);


        // RecyclerView handling - transaction list
        recyclerView_transaction = findViewById(R.id.recyclerview_transactiondata);
        transactionAdapter = new TransactionListAdapter(this, transactionList, assetList); // Create an adapter and supply the data to be displayed.
        recyclerView_transaction.setAdapter(transactionAdapter); // Connect the adapter with the RecyclerView.
        recyclerView_transaction.setLayoutManager(new LinearLayoutManager(this)); // Give the RecyclerView a default layout manager.


        // FAB handling
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        // make FAB show/hide when scroll recyclerview
        recyclerView_transaction.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 5 && mFloatingActionButton.getVisibility() == View.VISIBLE) {
                    mFloatingActionButton.hide();
                } else if (dy < -5 && mFloatingActionButton.getVisibility() != View.VISIBLE) {
                    mFloatingActionButton.show();
                }
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        resetFilterBtn = findViewById(R.id.resetFilter_button);

        EventBus.getDefault().register(this);

        // must have to read file from internal storage
        try {
            readFileObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // write (save) transaction list + asset list when pause
    @Override
    protected void onPause() {
        super.onPause();
        writeFileObject(transactionList, assetList, getApplicationContext()); // call write function
    }


    @Override
    protected void onStop() {
        super.onStop();
        writeFileObject(transactionList, assetList, getApplicationContext()); // call write function
    }


    // write transaction list and asset list to txt file
    public void writeFileObject(List<TransactionData> transactionDataList, List<AssetData> assetDataList, Context context) {

        try {

            OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(context.openFileOutput(saveTransactionList, Context.MODE_PRIVATE));
            OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(context.openFileOutput(saveAssetList, Context.MODE_PRIVATE));

            // writing transaction list
            for (int i = 0; i < transactionDataList.size(); i++) {

                TransactionData data = transactionDataList.get(i);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
                String formatDateTime = data.getDate().format(formatter);

                String dataStr = data.getTransactionID() + "!/!/" + formatDateTime + "!/!/" + data.getDirection() + "!/!/"
                        + data.getCategory() + "!/!/" + data.getAsset() + "!/!/" +
                        data.getMoney() + "!/!/" + data.getNote() + "!/!/" + data.getAssetID();

                outputStreamWriter1.write(dataStr + "\n");

            }

            // write asset list
            for (int i = 0; i < assetDataList.size(); i++) {
                AssetData data = assetDataList.get(i);
                String dataStr = data.getId() + "!/!/" + data.getName() + "!/!/" + data.getAmount() + "!/!/" + data.getLimit() + "!/!/" + data.getType();
                outputStreamWriter2.write(dataStr + "\n");
            }

            outputStreamWriter1.close();
            outputStreamWriter2.close();
        } catch (IOException e) {
            System.out.println("Exception File write failed: " + e.toString());
        }
    }


    // read transaction list and asset list from txt file
    private void readFileObject() throws IOException {
        try {
            InputStream inputStream1 = getApplicationContext().openFileInput(saveTransactionList);
            InputStream inputStream2 = getApplicationContext().openFileInput(saveAssetList);

            // read transaction datat list
            if (inputStream1 != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream1);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String readString = "";
                Double readOut = 0.0;
                Double readIn = 0.0;

                while ((readString = bufferedReader.readLine()) != null) {

//                    System.out.println("reading transaction list data: " + readString);
                    String[] dataSplit = readString.split("!/!/");
                    int transactionID = Integer.parseInt(dataSplit[0]);
                    String date = dataSplit[1];
                    String direction = dataSplit[2];
                    String category = dataSplit[3];
                    String asset = dataSplit[4];
                    String money = dataSplit[5];
                    String note = dataSplit[6];
                    int assetID = Integer.parseInt(dataSplit[7]);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
                    LocalDateTime formatDateTime = LocalDateTime.parse(date, formatter);

                    // make new TransactionData object here <<<<----
                    TransactionData transactionData = null;

                    transactionData = new TransactionData(transactionID,
                            formatDateTime, direction, category, asset, assetID, Double.parseDouble(money), note);

                    transactionList.add(transactionData);

                    Calendar c = Calendar.getInstance();
                    int currentYear = c.get(Calendar.YEAR);
                    int currentMonth = c.get(Calendar.MONTH) + 1;

                    // update net, out, in when new transaction added, only in the same month
                    if (transactionData.getDate().getMonthValue() == currentMonth && transactionData.getDate().getYear() == currentYear) {
                        if (direction.equals("EXPENSE")) {
                            readOut += Double.parseDouble(money);
                        } else if(direction.equals("INCOME")){
                            readIn += Double.parseDouble(money);
                        }
                    }

                }

                // update net, out, in when open app by reading file history, one by one when reading txt
                out = readOut;
                in = readIn;
                net = in - out;
                toolbarSumUpdater(transactionList);

                inputStream1.close();
            }

            // read asset data list
            if (inputStream2 != null) {

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream2);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String readString = "";

                while ((readString = bufferedReader.readLine()) != null) {
//                    System.out.println("reading asset list data: " + readString);
                    String[] dataSplit = readString.split("!/!/");
                    int id = Integer.parseInt(dataSplit[0]);
                    String name = dataSplit[1];
                    String amount = dataSplit[2];
                    String limit = dataSplit[3];
                    String type = dataSplit[4];

                    AssetData assetData = null;

                    assetData = new AssetData(id, name, Double.parseDouble(amount), Double.parseDouble(limit), type);

                    assetList.add(assetData);
                }

                inputStream2.close();
            }

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("file cant read");
        }
    }


    // handle menu xml
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    // handle toolbar menu icon click event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.toolbar_menu_asset:
                // need to use activity instead of fragment
                // edit fragment_asset and PickAssetFragment.java
                showAssetActivity();
                break;

            case R.id.toolbar_menu_chart:
                showChartActivity();
                break;

            case R.id.toolbar_menu_filter:
                showFilterDialogFragmenrt();
                break;
        }

        return true;
    }


    // fab button navigate to adding activity
    public void navToAddingActivity(View view) {
        Intent intent = new Intent(this, TransAddingActivity.class);
        intent.putExtra("ASSETLIST", (Serializable) assetList);
        startActivityForResult(intent, TEXT_REQUEST);
    }


    // receive data from adding/editing transaction activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {

                Bundle bundle = data.getBundleExtra(TransAddingActivity.EXTRA_REPLY);

                String fromActivity = bundle.getString("FROM");

                TransactionData newTransData = null;
                TransactionData oldTransData = null;

                // add new transaction
                if(fromActivity.equals("add_transaction")){
                    newTransData = (TransactionData) bundle.getSerializable("ADD_TRANSACTION_DATA");
                    transactionList.add(newTransData);
                }
                // edit selected transaction
                else if(fromActivity.equals("edit_transaction")){
                    newTransData = (TransactionData) bundle.getSerializable("EDIT_TRANSACTION_DATA");

                    for (int i = 0; i < transactionList.size(); i++) {
                        // transaction ID exist = editing transaction
                        if (transactionList.get(i).getTransactionID() == newTransData.getTransactionID()) {

                            oldTransData = transactionList.get(i);

                            int oldAssetID = oldTransData.getAssetID();
                            double oldMoney = oldTransData.getMoney();
                            String oldDirection = oldTransData.getDirection();

                            // reverse asset info
                            for (int j = 0; j < assetList.size(); j++) {

                                if (oldAssetID == assetList.get(j).getId()) {

                                    if (oldDirection.equals("EXPENSE")) {
                                        double newMoney = assetList.get(j).getAmount() + oldMoney;
                                        assetList.get(j).setAmount(newMoney);
                                    } else if (oldDirection.equals("INCOME")) {
                                        double newMoney = assetList.get(j).getAmount() - oldMoney;
                                        assetList.get(j).setAmount(newMoney);
                                    }
                                    break;
                                }

                            }
                            transactionList.set(i, newTransData);
                            break;
                        }
                    }
                }


                transactionList.sort(Comparator.comparing(TransactionData::getDate)); // sort by date (default option)
                Collections.reverse(transactionList); // reverse from now to past
                // Notify the adapter, that the data has changed.
                recyclerView_transaction.getAdapter().notifyDataSetChanged();
                toolbarSumUpdater(transactionList);


                // only update asset list when there is an asset in it
                if (newTransData.getAssetID() != 654321) {

                    for (int i = 0; i < assetList.size(); i++) {

                        if(newTransData.getAssetID() == assetList.get(i).getId()){
                            if(newTransData.getDirection().equals("EXPENSE")){

                                double assetOldAmount = assetList.get(i).getAmount();
                                double changeAmount = newTransData.getMoney();
                                double assetNewAmount = Double.parseDouble(String.format("%.2f", assetOldAmount - changeAmount));
                                assetList.get(i).setAmount(assetNewAmount);

                            }else if (newTransData.getDirection().equals("INCOME")){

                                double assetOldAmount = assetList.get(i).getAmount();
                                double changeAmount = newTransData.getMoney();;
                                double assetNewAmount = Double.parseDouble(String.format("%.2f", assetOldAmount + changeAmount));
                                assetList.get(i).setAmount(assetNewAmount);
                            }
                        }
                    }
                }
            }
        }
    }


    // handle side navigation click event
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.navi_book1:
                Toast.makeText(this, "Open Book 1", Toast.LENGTH_SHORT).show();

                writeFileObject(transactionList, assetList, getApplicationContext());
                transactionList.clear();
                assetList.clear();
                saveTransactionList = book1TransactionList;
                saveAssetList = book1AssetList;
                toolbarHome.setText("Book 1");

                try {
                    readFileObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                recyclerView_transaction.getAdapter().notifyDataSetChanged();
                drawerLayout.closeDrawers();

                break;

            case R.id.navi_book2:
                Toast.makeText(this, "Open Book 2", Toast.LENGTH_SHORT).show();

                writeFileObject(transactionList, assetList, getApplicationContext());
                transactionList.clear();
                assetList.clear();
                saveTransactionList = book2TransactionList;
                saveAssetList = book2AssetList;
                toolbarHome.setText("Book 2");

                try {
                    readFileObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                recyclerView_transaction.getAdapter().notifyDataSetChanged();
                drawerLayout.closeDrawers();

                break;

            case R.id.navi_add_book:
                Toast.makeText(this, "add book clicked", Toast.LENGTH_SHORT).show();
                break;


            case R.id.navi_asset:
                drawerLayout.closeDrawer((int) Gravity.LEFT);
                showAssetActivity();

                break;

            case R.id.navi_setting:
                Toast.makeText(this, "setting clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    // handle update toolbar textview sum
    public void toolbarSumUpdater(List<TransactionData> transactionDataList) {
        // update net, in, out (from the whole transaction list), in current month/year
        // sum up from transaction list
        out = 0.0;
        in = 0.0;
        net = 0.0;
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        for (int i = 0; i < transactionDataList.size(); i++) {
            if (transactionDataList.get(i).getDate().getMonthValue() == currentMonth && transactionDataList.get(i).getDate().getYear() == currentYear) {
                if (transactionDataList.get(i).direction.equals("EXPENSE")) {
                    out += transactionDataList.get(i).getMoney();
                } else if (transactionDataList.get(i).direction.equals("INCOME")) {
                    in += transactionDataList.get(i).getMoney();
                }
                net = in - out;
            }
        }
        toolbarNet.setText("$" + String.format("%.2f", net));
        toolbarOut.setText("$" + String.format("%.2f", out));
        toolbarIn.setText("$" + String.format("%.2f", in));
    }


    public void toolbarSumUpdater2(List<TransactionData> transactionDataList, int month, int year){
        out = 0.0;
        in = 0.0;
        net = 0.0;
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        for (int i = 0; i < transactionDataList.size(); i++) {
            if (transactionDataList.get(i).getDate().getMonthValue() == month && transactionDataList.get(i).getDate().getYear() == year) {
                if (transactionDataList.get(i).direction.equals("EXPENSE")) {
                    out += transactionDataList.get(i).getMoney();
                } else if (transactionDataList.get(i).direction.equals("INCOME")) {
                    in += transactionDataList.get(i).getMoney();
                }
                net = in - out;
            }
        }
        toolbarNet.setText("$" + String.format("%.2f", net));
        toolbarOut.setText("$" + String.format("%.2f", out));
        toolbarIn.setText("$" + String.format("%.2f", in));
    }


    // show [asset activity] toolbar button click
    // send (asset list) to [asset activity]
    public void showAssetActivity() {
        Intent intent = new Intent(this, AssetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("AssetList", (Serializable) assetList);
        bundle.putSerializable("TransactionList", (Serializable) transactionList);
//        intent.putExtra("ASSETLIST", (Serializable) assetList);
        intent.putExtras( bundle);
        startActivity(intent);
    }


    // show [month selection] fragment
    public void showMonthSelectionFragment(View view){
        FragmentManager fm = getSupportFragmentManager();
        MonthPickerFragment monthPickerFragment = new MonthPickerFragment();
        monthPickerFragment.show(fm, "monthSelection");
    }

    // process month/year from fragment
    public  void processMonthSelection(int monthSelection, int yearSelection){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthSelection-1);
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        toolBarDate.setText(month + " " + yearSelection);

        // set transaction list to time filter
        List<TransactionData> tempTransactionList = new ArrayList<>();

        for (int i = 0; i < transactionList.size(); i++) {
            if(transactionList.get(i).getDate().getMonthValue() == monthSelection && transactionList.get(i).getDate().getYear() == yearSelection){
                tempTransactionList.add(transactionList.get(i));
            }
        }

        toolbarSumUpdater2(tempTransactionList, monthSelection, yearSelection);

        transactionAdapter.setTransactionList(tempTransactionList);
        recyclerView_transaction.getAdapter().notifyDataSetChanged();
        resetFilterBtn.setVisibility(View.VISIBLE);
    }



    // show [filter fragment] toolbar button
    public void showFilterDialogFragmenrt() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("transaction_list", (Serializable) transactionList);
        bundle.putSerializable("asset_list", (Serializable) assetList);

        filterDialogFragment.setArguments(bundle);

        filterDialogFragment.show(fm, "dialog");
    }


    // set transaction filter
    public void processFilterSelection(List<String> expenseSelected, List<String> incomeSelected, List<String> otherSelected){


        List<TransactionData> tempTransactionList = new ArrayList<>();

        for (int i = 0; i < transactionList.size(); i++) {

            if(expenseSelected.contains(transactionList.get(i).getCategory())){
                tempTransactionList.add(transactionList.get(i));
            }

            if(incomeSelected.contains(transactionList.get(i).getCategory())){
                tempTransactionList.add(transactionList.get(i));
            }

            if(otherSelected.contains(transactionList.get(i).getDirection())){
                tempTransactionList.add(transactionList.get(i));
            }
        }


        toolbarSumUpdater(tempTransactionList);

        transactionAdapter.setTransactionList(tempTransactionList);
        recyclerView_transaction.getAdapter().notifyDataSetChanged();
        resetFilterBtn.setVisibility(View.VISIBLE);

    }


    // reset transaction list filter
    public void resetFilterSelection(View view){
        transactionAdapter.setTransactionList(transactionList);
        recyclerView_transaction.getAdapter().notifyDataSetChanged();
        resetFilterBtn.setVisibility(View.GONE);

        // reset month dropdown
        Calendar calendar = Calendar.getInstance();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year = calendar.get(Calendar.YEAR);
        toolBarDate.setText(month + " " + year);

        toolbarSumUpdater(transactionList);
    }


    // navigate to [chart activity] toolbar button
    public void showChartActivity() {
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("asset_list", (Serializable) assetList);
        intent.putExtra("transaction_list", (Serializable) transactionList);
        startActivity(intent);
    }

    // eventBus handler
    @Subscribe
    public void eventBusHandler(EventMessage eventMessage){

        String purpose = eventMessage.getPurpose();

        if(purpose.equals("delete_trans_from_transAdapter")){
            TransactionData transactionData = eventMessage.getTransactionData();
            double money = transactionData.getMoney();

            toolbarSumUpdater(transactionList);

            // update asset amount when (delete) a transaction
            // for [expense|income]
            if(transactionData.getDirection().equals("EXPENSE") || transactionData.getDirection().equals("INCOME")){
                int assetID = transactionData.getAssetID();
                for (int i = 0; i < assetList.size(); i++) {
                    if (assetList.get(i).getId() == assetID) {
                        double assetOldAmount = assetList.get(i).getAmount();
                        double assetNewAmount = 0;
                        if (transactionData.getDirection().equals("EXPENSE")) {
                            assetNewAmount = assetOldAmount + money;
                        } else if(transactionData.getDirection().equals("INCOME")) {
                            assetNewAmount = assetOldAmount - money;
                        }
                        assetList.get(i).setAmount(assetNewAmount);
                    }
                }
            }
            // for [transfer asset]
            else if(transactionData.getDirection().equals("ASSETTRANSFER")){
                int sourceID = transactionData.getAssetID();
                int targetID = Integer.parseInt(transactionData.getAsset());
                for (int i = 0; i < assetList.size(); i++) {
                    if(assetList.get(i).getId()==sourceID){
                        double assetOldAmount = assetList.get(i).getAmount();
                        double assetNewAmount = assetOldAmount + transactionData.getMoney();
                        assetList.get(i).setAmount(assetNewAmount);
                    }else if(assetList.get(i).getId()==targetID){
                        double assetOldAmount = assetList.get(i).getAmount();
                        double assetNewAmount = assetOldAmount - transactionData.getMoney();
                        assetList.get(i).setAmount(assetNewAmount);
                    }
                }
            }
            // for [edit asset]
            else if(transactionData.getDirection().equals("ASSETEDIT")){
                int transactionID = transactionData.getTransactionID();
                int assetID = transactionData.getAssetID();
                String assetMessage = transactionData.getAsset();
                String[] assetMessageSplit = assetMessage.split("/");
                String assetOldName = assetMessageSplit[0];
                String assetNewName = assetMessageSplit[1];
                String editMessage = assetMessageSplit[2];
                double assetOldBalance = Double.parseDouble(assetMessageSplit[3]);
                double assetNewBalance = Double.parseDouble(assetMessageSplit[4]);
                double assetOldLimit = Double.parseDouble(assetMessageSplit[5]);
                double assetNewLimit = Double.parseDouble(assetMessageSplit[6]);

                for (int i = 0; i < assetList.size(); i++) {
                    if(assetID==assetList.get(i).getId()){
                        assetList.get(i).setName(assetOldName);
                        assetList.get(i).setAmount(assetOldBalance);
                        assetList.get(i).setLimit(assetOldLimit);
                        break;
                    }
                }
            }
        }


        else if(purpose.equals("edit_trans_from_transAdapter")){
            TransactionData transactionData = eventMessage.getTransactionData();

            // send to transaction edit activity
            Intent intent = new Intent(this, TransEditingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putSerializable("TRANSACTION_OBJECT", transactionData);
            bundle.putSerializable("ASSET_LIST", (Serializable) assetList);
            intent.putExtras(bundle);
            startActivityForResult(intent, TEXT_REQUEST);
        }


        else if(purpose.equals("add_asset_from_assetActivity")){
            AssetData assetData = eventMessage.getAssetData();
            assetList.add(assetData);

            writeFileObject(transactionList, assetList, getApplicationContext());
        }


        else if(purpose.equals("delete_asset_from_assetAdapter")){
            assetList = eventMessage.getAssetList();

            writeFileObject(transactionList, assetList, getApplicationContext());
        }


        else if(purpose.equals("edit_asset_from_assetActivity_2")){

            TransactionData transactionData = eventMessage.getTransactionData();
            assetList = eventMessage.getAssetList();

            transactionList.add(transactionData);
            transactionList.sort(Comparator.comparing(TransactionData::getDate)); // sort by date (default option)
            Collections.reverse(transactionList); // reverse from now to past
            recyclerView_transaction.getAdapter().notifyDataSetChanged();

            writeFileObject(transactionList, assetList, getApplicationContext());
        }


        else if(purpose.equals("transfer_asset_from_assetActivity")){

            assetList = eventMessage.getAssetList();
            TransactionData transactionData = eventMessage.getTransactionData();

            transactionList.add(transactionData);
            // add new transaction to list
            transactionList.sort(Comparator.comparing(TransactionData::getDate)); // sort by date (default option)
            Collections.reverse(transactionList); // reverse from now to past
            recyclerView_transaction.getAdapter().notifyDataSetChanged();

            writeFileObject(transactionList, assetList, getApplicationContext());
        }

    }


}
