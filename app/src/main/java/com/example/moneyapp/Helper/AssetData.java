package com.example.moneyapp.Helper;

import java.io.Serializable;

public class AssetData implements Serializable {

    String name;
    double amount;
    double limit;
    String type;
    int id;

    public AssetData(int id, String name, double amount, double limit, String type){
        this.name = name;
        this.amount = amount;
        this.limit = limit;
        this.type = type;
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public double getAmount(){
        return amount;
    }

    public double getLimit() { return limit; }

    public String getType(){
        return type;
    }

    public int getId(){ return id; }

    public void setAmount(double newAmount){
        amount = newAmount;
    }

    public void setLimit(double newLimit){
        limit = newLimit;
    }

    public void setName(String newName){
        name = newName;
    }

}
