package com.example.moneyapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moneyapp.Helper.AssetData;
import com.example.moneyapp.R;
import com.example.moneyapp.Helper.TransactionData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private List<TransactionData> transactionList = new ArrayList<>();
    private List<AssetData> assetList = new ArrayList<>();

    private ProgressBar expenseRestaurantBar;
    private ProgressBar expenseGroceryBar;
    private ProgressBar expenseTransportBar;
    private ProgressBar expenseCashBar;
    private ProgressBar expenseEntertainBar;
    private ProgressBar expenseInvestBar;
    private ProgressBar expenseHousingBar;
    private ProgressBar expenseSpecialBar;

    private LinearLayout expenseRestaurantLayout;
    private LinearLayout expenseGroceryLayout;
    private LinearLayout expenseTransportLayout;
    private LinearLayout expenseCashLayout;
    private LinearLayout expenseEntertainLayout;
    private LinearLayout expenseInvestLayout;
    private LinearLayout expenseHousingLayout;
    private LinearLayout expenseSpecialLayout;

    private TextView expenseRestaurantNum;
    private TextView expenseGroceryNum;
    private TextView expenseTransportNum;
    private TextView expenseCashNum;
    private TextView expenseEntertainNum;
    private TextView expenseInvestNum;
    private TextView expenseHousingNum;
    private TextView expenseSpecialNum;
    private TextView expenseTotalNum;


    double expenseRestaurantSum = 0;
    double expenseGrocerySum = 0;
    double expenseTransportSum = 0;
    double expenseCashSum = 0;
    double expenseEntertainSum = 0;
    double expenseInvestSum = 0;
    double expenseHousingSum = 0;
    double expenseSpecialSum = 0;
    double totalExpense = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();
        assetList = (List<AssetData>) intent.getSerializableExtra("asset_list");
        transactionList = (List<TransactionData>) intent.getSerializableExtra("transaction_list");
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;


        expenseRestaurantBar = findViewById(R.id.expense_restaurant_bar);
        expenseGroceryBar = findViewById(R.id.expense_grocery_bar);
        expenseTransportBar = findViewById(R.id.expense_transport_bar);
        expenseCashBar = findViewById(R.id.expense_cash_bar);
        expenseEntertainBar = findViewById(R.id.expense_entertain_bar);
        expenseInvestBar = findViewById(R.id.expense_invest_bar);
        expenseHousingBar = findViewById(R.id.expense_housing_bar);
        expenseSpecialBar = findViewById(R.id.expense_special_bar);

        expenseRestaurantNum = findViewById(R.id.expense_restaurant_num);
        expenseGroceryNum = findViewById(R.id.expense_grocery_num);
        expenseTransportNum = findViewById(R.id.expense_transport_num);
        expenseCashNum = findViewById(R.id.expense_cash_num);
        expenseEntertainNum = findViewById(R.id.expense_entertain_num);
        expenseInvestNum = findViewById(R.id.expense_invest_num);
        expenseHousingNum = findViewById(R.id.expense_housing_num);
        expenseSpecialNum = findViewById(R.id.expense_special_num);
        expenseTotalNum = findViewById(R.id.expense_total);

        expenseRestaurantLayout = findViewById(R.id.expense_restaurant_layout);
        expenseGroceryLayout = findViewById(R.id.expense_grocery_layout);
        expenseTransportLayout = findViewById(R.id.expense_transport_layout);
        expenseCashLayout = findViewById(R.id.expense_cash_layout);
        expenseEntertainLayout = findViewById(R.id.expense_entertain_layout);
        expenseInvestLayout = findViewById(R.id.expense_invest_layout);
        expenseHousingLayout = findViewById(R.id.expense_housing_layout);
        expenseSpecialLayout = findViewById(R.id.expense_special_layout);

        sumProcessor(currentYear, currentMonth);

        expenseBarSetter();


    }

    private void expenseBarSetter() {

//        System.out.println("restaurant: " + expenseRestaurantSum);
//        System.out.println("grocery: " + expenseGrocerySum);
//        System.out.println("total: " + totalExpense);

        expenseTotalNum.setText("Total Expense: " + String.valueOf(totalExpense));

        if(expenseRestaurantSum>0){
            expenseRestaurantLayout.setVisibility(View.VISIBLE);
            expenseRestaurantBar.setProgress((int) Math.round(expenseRestaurantSum/totalExpense*100));
            expenseRestaurantNum.setText(String.valueOf(expenseRestaurantSum));
        }

        if(expenseGrocerySum>0){
            expenseGroceryLayout.setVisibility(View.VISIBLE);
            expenseGroceryBar.setProgress((int) Math.round(expenseGrocerySum/totalExpense*100));
            expenseGroceryNum.setText(String.valueOf(expenseGrocerySum));
        }

        if(expenseTransportSum>0){
            expenseTransportLayout.setVisibility(View.VISIBLE);
            expenseTransportBar.setProgress((int) Math.round(expenseTransportSum/totalExpense*100));
            expenseTransportNum.setText(String.valueOf(expenseTransportSum));
        }

        if(expenseCashSum>0){
            expenseCashLayout.setVisibility(View.VISIBLE);
            expenseCashBar.setProgress((int) Math.round(expenseCashSum/totalExpense*100));
            expenseCashNum.setText(String.valueOf(expenseCashSum));
        }

        if(expenseEntertainSum>0){
            expenseEntertainLayout.setVisibility(View.VISIBLE);
            expenseEntertainBar.setProgress((int) Math.round(expenseEntertainSum/totalExpense*100));
            expenseEntertainNum.setText(String.valueOf(expenseEntertainSum));
        }

        if(expenseInvestSum>0){
            expenseInvestLayout.setVisibility(View.VISIBLE);
            expenseInvestBar.setProgress((int) Math.round(expenseInvestSum/totalExpense*100));
            expenseInvestNum.setText(String.valueOf(expenseInvestSum));
        }

        if(expenseHousingSum>0){
            expenseHousingLayout.setVisibility(View.VISIBLE);
            expenseHousingBar.setProgress((int) Math.round(expenseHousingSum/totalExpense*100));
            expenseHousingNum.setText(String.valueOf(expenseHousingSum));
        }

        if(expenseSpecialSum>0){
            expenseSpecialLayout.setVisibility(View.VISIBLE);
            expenseSpecialBar.setProgress((int) Math.round(expenseSpecialSum/totalExpense*100));
            expenseSpecialNum.setText(String.valueOf(expenseSpecialSum));
        }

    }


    public void sumProcessor(int year, int month){

        for (int i = 0; i < transactionList.size(); i++) {

            if (transactionList.get(i).getDate().getMonthValue() == month && transactionList.get(i).getDate().getYear() == year) {{

                if(transactionList.get(i).getDirection().equals("EXPENSE")){

                    switch(transactionList.get(i).getCategory()){
                        case "Restaurant":
                            expenseRestaurantSum += transactionList.get(i).getMoney();
                            totalExpense += transactionList.get(i).getMoney();
                            break;

                        case "Grocery":
                            expenseGrocerySum += transactionList.get(i).getMoney();
                            totalExpense += transactionList.get(i).getMoney();
                            break;

                        case "Transport":
                            expenseTransportSum += transactionList.get(i).getMoney();
                            totalExpense += transactionList.get(i).getMoney();
                            break;

                        case "Cash":
                            expenseCashSum += transactionList.get(i).getMoney();
                            totalExpense += transactionList.get(i).getMoney();
                            break;

                        case "Entertain":
                            expenseEntertainSum += transactionList.get(i).getMoney();
                            totalExpense += transactionList.get(i).getMoney();
                            break;

                        case "Invest":
                            expenseInvestSum += transactionList.get(i).getMoney();
                            totalExpense += transactionList.get(i).getMoney();
                            break;

                        case "Housing":
                            expenseHousingSum += transactionList.get(i).getMoney();
                            totalExpense += transactionList.get(i).getMoney();
                            break;

                        case "Special":
                            expenseSpecialSum += transactionList.get(i).getMoney();
                            totalExpense += transactionList.get(i).getMoney();
                            break;
                    }
                }

            }}




        }

    }
}
