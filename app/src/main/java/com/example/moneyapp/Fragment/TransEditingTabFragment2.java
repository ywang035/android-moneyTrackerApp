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
public class TransEditingTabFragment2 extends Fragment implements View.OnClickListener {

    private TextView textView_salary;
    private TextView textView_invest;
    private TextView textView_cash;
    private TextView textView_split;
    private TextView textView_special;

    public TransEditingTabFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate fragment
        View view = inflater.inflate(R.layout.fragment_transaction_add_tab2, container, false);
        String defaultCategory= getArguments().getString("default_category");

        // define textview xml element
        textView_salary = view.findViewById(R.id.textView_salary);
        textView_invest = view.findViewById(R.id.textView_invest);
        textView_cash = view.findViewById(R.id.textView_cash);
        textView_split = view.findViewById(R.id.textView_split);
        textView_special = view.findViewById(R.id.textView_special);

        textView_salary.setClickable(true);
        textView_invest.setClickable(true);
        textView_cash.setClickable(true);
        textView_split.setClickable(true);
        textView_special.setClickable(true);

        textView_salary.setOnClickListener(this);
        textView_invest.setOnClickListener(this);
        textView_cash.setOnClickListener(this);
        textView_split.setOnClickListener(this);
        textView_special.setOnClickListener(this);

        setCategoryIcon(defaultCategory);

        return view;
    }


    @Override
    public void onClick(View v) {

        final TransEditingActivity activity = (TransEditingActivity)getActivity();


        switch (v.getId()){

            case R.id.textView_salary:

                String categoryMessage = textView_salary.getText().toString();
                textView_salary.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_salary_yellow, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);
                textView_split.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_split_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);
                activity.processCategory(categoryMessage);

                break;

            case R.id.textView_invest:

                categoryMessage = textView_invest.getText().toString();
                textView_salary.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_salary_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_yellow, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);
                textView_split.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_split_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);
                activity.processCategory(categoryMessage);

                break;

            case R.id.textView_cash:

                categoryMessage = textView_cash.getText().toString();
                textView_salary.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_salary_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_yellow, 0, 0);
                textView_split.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_split_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);
                activity.processCategory(categoryMessage);

                break;

            case R.id.textView_split:

                categoryMessage = textView_split.getText().toString();
                textView_salary.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_salary_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);
                textView_split.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_split_yellow, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_gray, 0, 0);
                activity.processCategory(categoryMessage);
                break;

            case R.id.textView_special:

                categoryMessage = textView_invest.getText().toString();
                textView_salary.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_salary_gray, 0, 0);
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_gray, 0, 0);
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_gray, 0, 0);
                textView_split.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_split_gray, 0, 0);
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_yellow, 0, 0);
                activity.processCategory(categoryMessage);
                break;
        }
    }

    public void setCategoryIcon(String defaultCategory){
        switch (defaultCategory){
            case "Salary":
                textView_salary.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_salary_yellow, 0, 0);
                break;

            case "Invest":
                textView_invest.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_invest_yellow, 0, 0);
                break;

            case "Cash":
                textView_cash.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_cash_yellow, 0, 0);
                break;

            case "Split":
                textView_split.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_split_yellow, 0, 0);
                break;

            case "Special":
                textView_special.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_special_yellow, 0, 0);
                break;
        }
    }

}
