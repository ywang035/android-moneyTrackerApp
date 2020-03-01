package com.example.moneyapp.Helper;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionData implements Serializable {

    public LocalDateTime date;
    public String asset;
    public String note;
    public String direction;
    public String category;
    public double money;
    int assetID;
    int transactionID;

    public TransactionData(int transactionID, LocalDateTime date, String direction, String category, String asset, int assetID, double money, String note){
        this.transactionID = transactionID;
        this.date = date;
        this.asset = asset;
        this.money = money;
        this.note = note;
        this.direction = direction;
        this.category = category;
        this.assetID = assetID;
    }

    public int getTransactionID(){
        return transactionID;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public String getAsset(){
        return asset;
    }

    public double getMoney(){
        return money;
    }

    public String getNote(){
        return  note;
    }

    public String getCategory(){
        return category;
    }

    public String getDirection(){
        return direction;
    }

    public int getAssetID() { return assetID; }

    public void setDate(LocalDate date){
        date = date;
    }

    public void setAsset(String asset){
        asset = asset;
    }

    public void setMoney(double money){
        money = money;
    }

    public void setNote(String note){
        note = note;
    }

    public void setDirection(String direction){
        direction = direction;
    }

    public void setCategory(String category){
        category = category;
    }

    public void setAssetID(int assetID){
        assetID = assetID;
    }

}
