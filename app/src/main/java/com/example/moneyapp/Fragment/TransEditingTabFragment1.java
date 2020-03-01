package com.example.moneyapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moneyapp.Activity.TransEditingActivity;
import com.example.moneyapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransEditingTabFragment1 extends Fragment implements View.OnClickListener {


    private String categoryMessage;

    private TextView textView_restaurant;
    private TextView textView_grocery;
    private TextView textView_transport;
    private TextView textView_cash;
    private TextView textView_entertain;
    private TextView textView_invest;
    private TextView textView_housing;
    private TextView textView_special;

    public TransEditingTabFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate fragment
        View view = inflater.inflate(R.layout.fragment_transaction_add_tab1, container, false);
        String defaultCategory= getArguments().getString("default_category");

        // define textview  element
        textView_restaurant = view.findViewById(R.id.textView_restaurant);
        textView_grocery = view.findViewById(R.id.textView_grocery);
        textView_transport = view.findViewById(R.id.textView_transport);
        textView_cash = view.findViewById(R.id.textView_cash);
        textView_entertain = view.findViewById(R.id.textView_entertain);
        textView_invest = view.findViewById(R.id.textView_invest);
        textView_housing = view.findViewById(R.id.textView_housing);
        textView_special = view.findViewById(R.id.textView_special);

        textView_restaurant.setClickable(true);
        textView_grocery.setClickable(true);
        textView_transport.setClickable(true);
        textView_cash.setClickable(true);
        textView_entertain.setClickable(true);
        textView_invest.setClickable(true);
        textView_housing.setClickable(true);
        textView_special.setClickable(true);

        textView_restaurant.setOnClickListener(this);
        textView_grocery.setOnClickListener(this);
        textView_transport.setOnClickListener(this);
        textView_cash.setOnClickListener(this);
        textView_entertain.setOnClickListener(this);
        textView_invest.setOnClickListener(this);
        textView_housing.setOnClickListener(this);
        textView_special.setOnClickListener(this);

        setCategoryIcon(defaultCategory);

        return view;
    }

    @Override
    public void onClick(View v) {

        final TransEditingActivity activity = (TransEditingActivity)getActivity();

        switch (v.getId()){

            case R.id.textView_restaurant:

                categoryMessage = textView_restaurant.getText().toString();
                textView_restaurant.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_restaurant_yellow, 0, 0);
                textView_grocery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_grocery_gray, 0, 0);
                textView_transport.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_transport_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);

                textView_entertain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_entertain_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_housing.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_housing_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);

                activity.processCategory(categoryMessage);

                break;

            case R.id.textView_grocery:

                categoryMessage = textView_grocery.getText().toString();
                textView_restaurant.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_restaurant_gray, 0, 0);
                textView_grocery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_grocery_yellow, 0, 0);
                textView_transport.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_transport_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);

                textView_entertain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_entertain_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_housing.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_housing_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);
                activity.processCategory(categoryMessage);

                break;

            case R.id.textView_transport:

                categoryMessage = textView_transport.getText().toString();
                textView_restaurant.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_restaurant_gray, 0, 0);
                textView_grocery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_grocery_gray, 0, 0);
                textView_transport.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_transport_yellow, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);

                textView_entertain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_entertain_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_housing.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_housing_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);
                activity.processCategory(categoryMessage);

                break;

            case R.id.textView_cash:

                categoryMessage = textView_cash.getText().toString();
                textView_restaurant.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_restaurant_gray, 0, 0);
                textView_grocery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_grocery_gray, 0, 0);
                textView_transport.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_transport_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_yellow, 0, 0);

                textView_entertain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_entertain_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_housing.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_housing_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);
                activity.processCategory(categoryMessage);

                break;

            case R.id.textView_entertain:

                categoryMessage = textView_entertain.getText().toString();
                textView_restaurant.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_restaurant_gray, 0, 0);
                textView_grocery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_grocery_gray, 0, 0);
                textView_transport.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_transport_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);

                textView_entertain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_entertain_yellow, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_housing.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_housing_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);
                activity.processCategory(categoryMessage);

                break;

            case R.id.textView_invest:

                categoryMessage = textView_invest.getText().toString();
                textView_restaurant.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_restaurant_gray, 0, 0);
                textView_grocery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_grocery_gray, 0, 0);
                textView_transport.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_transport_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);

                textView_entertain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_entertain_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_yellow, 0, 0);
                textView_housing.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_housing_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);
                activity.processCategory(categoryMessage);

                break;

            case R.id.textView_housing:

                categoryMessage = textView_housing.getText().toString();
                textView_restaurant.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_restaurant_gray, 0, 0);
                textView_grocery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_grocery_gray, 0, 0);
                textView_transport.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_transport_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);

                textView_entertain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_entertain_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_housing.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_housing_yellow, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);
                activity.processCategory(categoryMessage);

                break;

            case R.id.textView_special:

                categoryMessage = textView_special.getText().toString();
                textView_restaurant.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_restaurant_gray, 0, 0);
                textView_grocery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_grocery_gray, 0, 0);
                textView_transport.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_transport_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);

                textView_entertain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_entertain_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_housing.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_housing_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_yellow, 0, 0);
                activity.processCategory(categoryMessage);

                break;
        }

    }


    public void setCategoryIcon(String defaultCategory){
        switch (defaultCategory){
            case "Restaurant":
                textView_restaurant.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_restaurant_yellow, 0, 0);
                break;

            case "Grocery":
                textView_grocery.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_grocery_yellow, 0, 0);
                break;

            case "Transport":
                textView_transport.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_transport_yellow, 0, 0);
                break;

            case "Cash":
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_yellow, 0, 0);
                break;

            case "Entertain":
                textView_entertain.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_entertain_yellow, 0, 0);
                break;

            case "Invest":
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_yellow, 0, 0);
                break;

            case "Housing":
                textView_housing.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_housing_yellow, 0, 0);
                break;

            case "Special":
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_yellow, 0, 0);
                break;
        }
    }
}
