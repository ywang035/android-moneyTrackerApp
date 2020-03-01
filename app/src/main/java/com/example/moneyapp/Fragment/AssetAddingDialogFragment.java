package com.example.moneyapp.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneyapp.Activity.AssetActivity;
import com.example.moneyapp.Helper.AssetData;
import com.example.moneyapp.Helper.DecimalDigitsInputFilter;
import com.example.moneyapp.R;

import java.util.Date;


public class AssetAddingDialogFragment extends DialogFragment {

    EditText assetName;
    EditText assetAmount;
    EditText assetLimit;
    TextView assetLimitLabel;
    RadioButton assetType;
    Button okButton;

    String name;
    double amount;
    double limit;
    String type;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setCanceledOnTouchOutside(false);

        View rootView = inflater.inflate(R.layout.fragment_asset_add, container,
                false);

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Add a New Asset");

        // Edited: Overriding onCreateView is not necessary in your case
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_asset_add, null);
        builder.setView(view);

        assetName = view.findViewById(R.id.dialogFragment_addAsset_name);
        assetAmount = view.findViewById(R.id.dialogFragment_addAsset_amount);
        assetLimit = view.findViewById(R.id.dialogFragment_addAsset_limit);
        assetLimitLabel = view.findViewById(R.id.dialogFragment_addAsset_limit_label);

        assetLimitLabel.setVisibility(View.GONE);
        assetLimit.setVisibility(View.GONE);

        assetAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});

        // default radio button text
        RadioGroup radioGroup = view.findViewById(R.id.dialogFragment_addAsset_radio);
        assetType = view.findViewById(radioGroup.getCheckedRadioButtonId());
        type = assetType.getText().toString();


        okButton = view.findViewById(R.id.dialogFragment_addAsset_ok);
        Button cancelButton = view.findViewById(R.id.dialogFragment_addAsset_cancel);
        okButton.setEnabled(false);
        okButton.setTextColor(Color.GRAY);

        assetName.addTextChangedListener(watcher);
        assetAmount.addTextChangedListener(watcher);

        // handle radio group of asset type (when click other than default)
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id)
            {

                assetType = view.findViewById(id);
                type = assetType.getText().toString();
                if(type.equals("Credit Card")){
                    assetLimit.setVisibility(View.VISIBLE);
                    assetLimitLabel.setVisibility(View.VISIBLE);
                    assetLimit.setFocusable(true);
                    assetLimit.setFocusableInTouchMode(true);
                    assetLimit.addTextChangedListener(watcher);
                    assetLimit.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});
                }else{
                    assetLimit.setVisibility(View.GONE);
                    assetLimitLabel.setVisibility(View.GONE);
                    assetLimit.setFocusable(false);
                    assetLimit.setFocusableInTouchMode(false);
                }

            }});

        // handle ok button click
        okButton.setOnClickListener(new View.OnClickListener() {

            Date date= new Date();
            long time = date.getTime();
            int assetID = Math.abs((int)time);

            @Override
            public void onClick(View v) {

//                System.out.println("adding new asset from fragment: " + name + "---" + amount + "---" + limit + "---" + type + "---" + assetID);
                AssetActivity activity = (AssetActivity) getActivity();
                AssetData assetData = null;
                assetData = new AssetData(assetID, name, amount, limit, type);
                activity.processAddAssetDialogFragment(assetData);
                dismiss();
                Toast.makeText(getContext(), "Asset Added", Toast.LENGTH_SHORT).show();

            }
        });

        // handle cancel button click
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }

    // handle if edittext input is empty
    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if(type.equals("Credit Card")){
                if(assetName.getText().toString().length()>0 && assetAmount.getText().toString().length()>0
                        && assetLimit.getText().toString().length()>0){
                    name = assetName.getText().toString();
                    amount = Double.parseDouble(assetAmount.getText().toString());
                    if(amount!=0){
                        amount =  -1*amount;
                    }
                    limit = Double.parseDouble(assetLimit.getText().toString());
                    okButton.setEnabled(true);
                    okButton.setTextColor(Color.parseColor("#ffca28"));
                }else{
                    okButton.setEnabled(false);
                    okButton.setTextColor(Color.GRAY);
                }
            }else{
                if(assetName.getText().toString().length()>0 && assetAmount.getText().toString().length()>0){
                    name = assetName.getText().toString();
                    amount = Double.parseDouble(assetAmount.getText().toString());
                    limit = 0;
                    okButton.setEnabled(true);
                    okButton.setTextColor(Color.parseColor("#ffca28"));
                }else{
                    okButton.setEnabled(false);
                    okButton.setTextColor(Color.GRAY);
                }
            }

        }
    };


}
