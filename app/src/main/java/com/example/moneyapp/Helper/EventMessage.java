package com.example.moneyapp.Helper;

import java.util.ArrayList;
import java.util.List;

public class EventMessage {

    String purpose;
    List<TransactionData> transactionList = new ArrayList<>();
    List<AssetData> assetList = new ArrayList<>();
    TransactionData transactionData;
    AssetData assetData;


    public EventMessage(String purpose, TransactionData transactionData, AssetData assetData, List<TransactionData> transactionList, List<AssetData> assetList){
        this.purpose = purpose;
        this.transactionData = transactionData;
        this.assetData = assetData;
        this.transactionList = transactionList;
        this.assetList = assetList;
    }


    public String getPurpose(){
        return purpose;
    }

    public TransactionData getTransactionData(){
        return  transactionData;
    }

    public AssetData getAssetData(){
        return  assetData;
    }

    public List<TransactionData> getTransactionList(){
        return transactionList;
    }

    public List<AssetData> getAssetList(){
        return assetList;
    }






}
