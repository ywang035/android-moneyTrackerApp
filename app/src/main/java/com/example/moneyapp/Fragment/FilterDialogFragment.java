package com.example.moneyapp.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.moneyapp.Activity.MainActivity;
import com.example.moneyapp.Helper.AssetData;
import com.example.moneyapp.R;
import com.example.moneyapp.Helper.TransactionData;

import java.util.ArrayList;
import java.util.List;

import static com.example.moneyapp.Helper.Constant.TOAST_OK;

public class FilterDialogFragment extends DialogFragment {

    private List<TransactionData> transactionList = new ArrayList<>();
    private List<AssetData> assetList = new ArrayList<>();
    private List<String> expenseSelected = new ArrayList<>();
    private List<String> incomeSelected = new ArrayList<>();
    private List<String> otherSelected = new ArrayList<>();


    private CheckBox
            checkBoxExpenseRestaurant, checkBoxExpenseGrocery, checkBoxExpenseTransport, checkBoxExpenseCash,
            checkBoxExpenseEntertain, checkBoxExpenseInvest, checkBoxExpenseHousing, checkBoxExpenseSpecial;

    private CheckBox
            checkBoxIncomeSalary, checkBoxIncomeInvest, checkBoxIncomeCash, checkBoxIncomeSplit, checkBoxIncomeSpecial;

    private CheckBox
            checkBoxAssetTransfer, checkBoxAssetEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setCanceledOnTouchOutside(false);

        View rootView = inflater.inflate(R.layout.fragment_transaction_filter, container,
                false);

        transactionList = (List<TransactionData>) getArguments().getSerializable("transaction_list");
        assetList = (List<AssetData>) getArguments().getSerializable("asset_list");

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Transaction Filter");

        // Edited: Overriding onCreateView is not necessary in your case
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_transaction_filter, null);

        checkBoxExpenseRestaurant = view.findViewById(R.id.transaction_filter_checkbox_expense_restaurant);
        checkBoxExpenseGrocery = view.findViewById(R.id.transaction_filter_checkbox_expense_grocery);
        checkBoxExpenseTransport = view.findViewById(R.id.transaction_filter_checkbox_expense_transport);
        checkBoxExpenseCash = view.findViewById(R.id.transaction_filter_checkbox_expense_cash);
        checkBoxExpenseEntertain = view.findViewById(R.id.transaction_filter_checkbox_expense_entertain);
        checkBoxExpenseInvest = view.findViewById(R.id.transaction_filter_checkbox_expense_invest);
        checkBoxExpenseHousing = view.findViewById(R.id.transaction_filter_checkbox_expense_housing);
        checkBoxExpenseSpecial = view.findViewById(R.id.transaction_filter_checkbox_expense_special);

        checkBoxIncomeSalary = view.findViewById(R.id.transaction_filter_checkbox_income_salary);
        checkBoxIncomeInvest = view.findViewById(R.id.transaction_filter_checkbox_income_invest);
        checkBoxIncomeCash = view.findViewById(R.id.transaction_filter_checkbox_income_cash);
        checkBoxIncomeSplit = view.findViewById(R.id.transaction_filter_checkbox_income_split);
        checkBoxIncomeSpecial = view.findViewById(R.id.transaction_filter_checkbox_income_special);

        checkBoxAssetTransfer = view.findViewById(R.id.transaction_filter_checkbox_assetTransfer);
        checkBoxAssetEdit = view.findViewById(R.id.transaction_filter_checkbox_assetEdit);


        builder.setView(view).setPositiveButton(TOAST_OK,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                okButton(view);
                Toast.makeText(getContext(), "Filter Applied", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }



    public void okButton(View view){

        // ----- expense -----
        if(checkBoxExpenseRestaurant.isChecked()){
            expenseSelected.add((String) checkBoxExpenseRestaurant.getText());
        }

        if(checkBoxExpenseGrocery.isChecked()){
            expenseSelected.add((String) checkBoxExpenseGrocery.getText());
        }

        if(checkBoxExpenseTransport.isChecked()){
            expenseSelected.add((String) checkBoxExpenseTransport.getText());
        }

        if(checkBoxExpenseCash.isChecked()){
            expenseSelected.add((String) checkBoxExpenseCash.getText());
        }

        if(checkBoxExpenseEntertain.isChecked()){
            expenseSelected.add((String) checkBoxExpenseEntertain.getText());
        }

        if(checkBoxExpenseInvest.isChecked()){
            expenseSelected.add((String) checkBoxExpenseInvest.getText());
        }

        if(checkBoxExpenseHousing.isChecked()){
            expenseSelected.add((String) checkBoxExpenseHousing.getText());
        }

        if(checkBoxExpenseSpecial.isChecked()){
            expenseSelected.add((String) checkBoxExpenseSpecial.getText());
        }

        // ----- income -----
        if(checkBoxIncomeSalary.isChecked()){
            incomeSelected.add((String) checkBoxIncomeSalary.getText());
        }

        if(checkBoxIncomeInvest.isChecked()){
            incomeSelected.add((String) checkBoxIncomeInvest.getText());
        }

        if(checkBoxIncomeCash.isChecked()){
            incomeSelected.add((String) checkBoxIncomeCash.getText());
        }

        if(checkBoxIncomeSplit.isChecked()){
            incomeSelected.add((String) checkBoxIncomeSplit.getText());
        }

        if(checkBoxIncomeSpecial.isChecked()){
            incomeSelected.add((String) checkBoxIncomeSpecial.getText());
        }

        // ----- other -----
        if(checkBoxAssetTransfer.isChecked()){
            otherSelected.add("ASSETTRANSFER");
        }

        if(checkBoxAssetEdit.isChecked()){
            otherSelected.add("ASSETEDIT");
        }


        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.processFilterSelection(expenseSelected, incomeSelected, otherSelected);
        dismiss();
    }

}
