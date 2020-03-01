package com.example.moneyapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.moneyapp.Fragment.AssetAddingDialogFragment;
import com.example.moneyapp.Helper.AssetData;
import com.example.moneyapp.Fragment.AssetEditingFragment;
import com.example.moneyapp.Adapter.AssetListAdapter;
import com.example.moneyapp.Fragment.AssetTransferingDialogFragment;
import com.example.moneyapp.Helper.EventMessage;
import com.example.moneyapp.R;
import com.example.moneyapp.Helper.TransactionData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssetActivity extends AppCompatActivity {


    private List<AssetData> assetList = new ArrayList<>();
    private List<TransactionData> transactionList = new ArrayList<>();
    RecyclerView recyclerView_assetList;
    TextView net;
    TextView debit;
    TextView credit;
    AssetListAdapter rAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        net = findViewById(R.id.asset_activity_net_text);
        debit = findViewById(R.id.asset_activity_debit_text);
        credit = findViewById(R.id.asset_activity_credit_text);


        // receive asset data list from main (when asset activity is open)
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assetList = (List<AssetData>) bundle.getSerializable("AssetList");
        transactionList = (List<TransactionData>) bundle.getSerializable("TransactionList");

        updateAssetSummary();

        // inflate recyclerview in asset activity
        recyclerView_assetList = findViewById(R.id.recyclerview_assetdata);
        rAdapter = new AssetListAdapter(this, assetList);
        recyclerView_assetList.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_assetList.setAdapter(rAdapter);

        EventBus.getDefault().register(this);

    }


    // button - [show add asset dialog fragment]
    public void showAddAssetDialogFragment(View view){
        FragmentManager fm = getSupportFragmentManager();
        AssetAddingDialogFragment addAssetDialogFragment  = new AssetAddingDialogFragment();
        addAssetDialogFragment.show(fm, "dialog");
    }

    // handle message from dialog fragment add asset
    public void processAddAssetDialogFragment(AssetData assetData){
        // message is in AssetData object
        assetList.add(assetData);
        rAdapter.setAssetList(assetList);

        // update recyclerview UI when new asset data received, from add asset dialog fragment
        recyclerView_assetList.getAdapter().notifyDataSetChanged();   // put new added transaction to same date card
        recyclerView_assetList.smoothScrollToPosition(0); // Scroll to the bottom.

        EventBus.getDefault().post(new EventMessage("add_asset_from_assetActivity", null, assetData, null, null));

        updateAssetSummary();
    }


    // button - [show transfer asset dialog]
    public void showTransferAssetDialogFragment(View view){

        Bundle bundle = new Bundle();

        bundle.putSerializable("AssetList", (Serializable) assetList);

        FragmentManager fm = getSupportFragmentManager();
        AssetTransferingDialogFragment addAssetDialogFragment  = new AssetTransferingDialogFragment();

        // set Fragmentclass Arguments
        addAssetDialogFragment.setArguments(bundle);

        addAssetDialogFragment.show(fm, "dialog");
    }


    // receive message from [asset transfer dialog fragment]
    public void processTransferAssetDialogFragment(int transactionID, String dateStr, int sourceID, int targetID, double amount, String note){

        double oldSourceAssetAmount;
        double oldTargetAssetAmount;

        String targetAsset = "target asset";
        String sourceAsset = "source asset";

        String[] dateSplit = dateStr.split("-");
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);
        int hour = Integer.parseInt(dateSplit[3]);
        int minute = Integer.parseInt(dateSplit[4]);
        int second = Integer.parseInt(dateSplit[5]);


        LocalDateTime formatDateTime = LocalDateTime.of(year, month, day, hour, minute, second);

        // update (asset data) after transfer
        for (int i = 0; i < assetList.size(); i++) {
            if(assetList.get(i).getId()==sourceID){
                oldSourceAssetAmount = assetList.get(i).getAmount();
                assetList.get(i).setAmount(oldSourceAssetAmount-amount);
                sourceAsset = assetList.get(i).getName();
            }else if(assetList.get(i).getId()==targetID){
                oldTargetAssetAmount = assetList.get(i).getAmount();
                assetList.get(i).setAmount(oldTargetAssetAmount+amount);
                targetAsset = assetList.get(i).getName();
            }
        }

        // make new (transaction data)
        TransactionData transactionData = new TransactionData(transactionID, formatDateTime, "ASSETTRANSFER", targetAsset, String.valueOf(targetID), sourceID, amount, note);


        EventBus.getDefault().post(new EventMessage("transfer_asset_from_assetActivity", transactionData, null, null, assetList));


//        System.out.println("from asset activity: " + transactionData.getTransactionID() + " "
//        + transactionData.getDate() + " " + transactionData.getDirection() + " "
//        + transactionData.getCategory() + " " + transactionData.getAsset() + " "
//        + transactionData.getAssetID() + " " + transactionData.getMoney() + " "
//        + transactionData.getNote());

        recyclerView_assetList.getAdapter().notifyDataSetChanged();
        updateAssetSummary();
    }


    // receive messages from [asset edit fragment]
    public void processEditAssetDialogFragment(int assetID,
                                               double assetNewBalance, double assetNewLimit, String assetNewName,
                                               String assetEditNote, int transactionID, LocalDateTime formatedTime, String assetType
                                               ){

        String assetOldName = "";
        double assetOldBalance = 0;
        double assetOldLimit = 0;

        for (int i = 0; i < assetList.size(); i++) {
            if(assetList.get(i).getId()==assetID){

                assetOldName = assetList.get(i).getName();
                assetList.get(i).setName(assetNewName);

                if(assetList.get(i).getType().equals("Credit Card")){
                    assetOldBalance = assetList.get(i).getAmount();
                    assetList.get(i).setAmount(assetNewBalance);
                    assetOldLimit = assetList.get(i).getLimit();
                    assetList.get(i).setLimit(assetNewLimit);
                }else{
                    assetOldBalance = assetList.get(i).getAmount();
                    assetList.get(i).setAmount(assetNewBalance);
                    assetOldLimit = assetList.get(i).getLimit();
                    assetList.get(i).setLimit(assetNewLimit);
                }
                break;
            }

        }

        String editMessage = "";

        if(assetOldBalance!=assetNewBalance){
            editMessage = "Balance: " + assetOldBalance + "\t>>>\t" + assetNewBalance;
        }

        String numberMessage = assetOldBalance + "/" + assetNewBalance + "/" + assetOldLimit + "/" + assetNewLimit;
        String assetMessage = assetOldName + "/" + assetNewName + "/" + editMessage + "/" + numberMessage;

        TransactionData transactionData = new TransactionData(transactionID, formatedTime, "ASSETEDIT", "Edit Asset", assetMessage, assetID, assetNewBalance, assetEditNote);

        EventBus.getDefault().post(new EventMessage("edit_asset_from_assetActivity_2", transactionData, null, null, assetList));

        recyclerView_assetList.getAdapter().notifyDataSetChanged();
        updateAssetSummary();

    }


    // handle asset summary textview
    public void updateAssetSummary(){

        double netMoney = 0;
        double debitMoney = 0;
        double creditMoney = 0;

        for (int i = 0; i < assetList.size(); i++) {

            if(assetList.get(i).getType().equals("Credit Card")){
                creditMoney += assetList.get(i).getAmount();
            }else{
                debitMoney += assetList.get(i).getAmount();
            }
        }

        netMoney = debitMoney + creditMoney;

        net.setText("$" + String.format("%.2f", netMoney));
        debit.setText("$" + String.format("%.2f", debitMoney));
        credit.setText("$" + String.format("%.2f", creditMoney));
    }


    @Subscribe
    public void eventBusHandler(EventMessage eventMessage) {
        String purpose = eventMessage.getPurpose();

        if(purpose.equals("delete_asset_from_assetAdapter")){
            updateAssetSummary();
        }


        else if(purpose.equals("edit_asset_from_assetAdapter_1")){

            // get asset info
            AssetData assetData = eventMessage.getAssetData();

            // need to get [edit asset] info, send it to fragment
            FragmentManager fm = getSupportFragmentManager();
            AssetEditingFragment editFragment  = new AssetEditingFragment();
            Bundle arguments = new Bundle();

            arguments.putSerializable("asset_data", assetData);

            editFragment.setArguments(arguments);
            editFragment.show(fm, "dialog");

            updateAssetSummary();
        }
    }

}
