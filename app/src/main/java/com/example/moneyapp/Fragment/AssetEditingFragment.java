package com.example.moneyapp.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneyapp.Activity.AssetActivity;
import com.example.moneyapp.Helper.AssetData;
import com.example.moneyapp.Helper.DecimalDigitsInputFilter;
import com.example.moneyapp.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class AssetEditingFragment extends DialogFragment {

    Button datePicker;
    EditText assetNameInput;
    TextView assetType;
    EditText assetAmountInput;
    LinearLayout creditCardLayout;
    EditText assetLimitInput;
    EditText assetNoteInput;
    Button okButton;
    Button cancelButton;

    AssetData assetData;
    String selectedDate;
    String assetNewName;
    Double assetNewAmount;
    Double assetNewLimit;
    String assetTypeStr;
    String assetNote;

    public static final int REQUEST_CODE = 11;

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setCanceledOnTouchOutside(false);

        View rootView = inflater.inflate(R.layout.fragment_asset_edit, container,
                false);

        return rootView;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        assetData = (AssetData) getArguments().getSerializable("asset_data");


        builder.setTitle("Edit Asset");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_asset_edit, null);
        builder.setView(view);


        datePicker = view.findViewById(R.id.dialogFragment_editAsset_datePickerButton);
        // create instance for calendar, set to date button as current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        selectedDate = year + "-" + month + "-" + day ;
        datePicker.setText(selectedDate);


        assetType = view.findViewById(R.id.dialogFragment_editAsset_assetType);
        assetType.setText(assetData.getType());
        assetType.setTextColor(Color.GRAY);
        assetTypeStr = assetData.getType();


        assetNameInput = view.findViewById(R.id.dialogFragment_editAsset_name);
        assetNameInput.setText(assetData.getName());
        assetNameInput.addTextChangedListener(watcher);


        assetAmountInput = view.findViewById(R.id.dialogFragment_editAsset_newAmount);
        assetAmountInput.setText(String.valueOf(assetData.getAmount()));
        assetAmountInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});
        assetAmountInput.addTextChangedListener(watcher);


        creditCardLayout = view.findViewById(R.id.dialogFragment_assetEdit_creditCardLayout);

        assetLimitInput = view.findViewById(R.id.dialogFragment_editAsset_newLimit);
        assetLimitInput.setText(String.valueOf(assetData.getLimit()));
        assetLimitInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});
        assetLimitInput.addTextChangedListener(watcher);

        assetNoteInput = view.findViewById(R.id.dialogFragment_editAsset_note);


        okButton = view.findViewById(R.id.dialogFragment_editAsset_ok);
//        okButton.setEnabled(false);
//        okButton.setTextColor(Color.GRAY);
        cancelButton = view.findViewById(R.id.dialogFragment_editAsset_cancel);


        if(!assetData.getType().equals("Credit Card")){
            creditCardLayout.setVisibility(View.GONE);
        }else{
            creditCardLayout.setVisibility(View.VISIBLE);
        }


        // handle [ok button] click
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //------------------------ asset info ------------------------
                int assetID = assetData.getId();
                assetNewName = assetNameInput.getText().toString();
                assetNewAmount = Double.parseDouble(assetAmountInput.getText().toString());
                assetNote = assetNoteInput.getText().toString();

                if(assetData.getType().equals("Credit Card")){
                    assetNewLimit = Double.parseDouble(assetLimitInput.getText().toString());
                }else{
                    assetNewLimit = assetData.getLimit();
                }

                String message = selectedDate  + " " + assetID + " " + assetNewName + " " + assetNewAmount + " " + assetNewLimit + " " + assetNote;

                //------------------------ transaction info ------------------------
                Date date= new Date();
                long time = date.getTime();
                int transactionID = Math.abs((int)time);

                // get current hour min second
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");
                String formattedDate = df.format(c.getTime());
                // formattedDate have current date/time
                selectedDate = selectedDate + "-" + formattedDate;
                String[] dateSplit = selectedDate.split("-");
                int year = Integer.parseInt(dateSplit[0]);
                int month = Integer.parseInt(dateSplit[1]);
                int day = Integer.parseInt(dateSplit[2]);
                int hour = Integer.parseInt(dateSplit[3]);
                int minute = Integer.parseInt(dateSplit[4]);
                int second = Integer.parseInt(dateSplit[5]);
                LocalDateTime formatDateTime = LocalDateTime.of(year, month, day, hour, minute, second);


                AssetActivity activity = (AssetActivity) getActivity();
                activity.processEditAssetDialogFragment(assetID, assetNewAmount, assetNewLimit, assetNewName, assetNote, transactionID, formatDateTime, assetTypeStr);

                Toast.makeText(getContext(), "Asset Saved", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });


        // handle [cancel button] click
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


        final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create the datePickerFragment
                DialogFragment datePickerFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                datePickerFragment.setTargetFragment(AssetEditingFragment.this, REQUEST_CODE);

                Bundle args = new Bundle();
                args.putString("SOURCE", "AssetEditDialog");
                datePickerFragment.setArguments(args);

                // show the datePicker
                datePickerFragment.show(fm, "datePicker");
            }
        });


        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            selectedDate = data.getStringExtra("SELECTDATE");
//            System.out.println("from dialog: " + selectedDate);
            datePicker.setText(selectedDate);
        }
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
            if(assetData.getType().equals("Credit Card")){
                if(assetAmountInput.getText().length()>0 && assetNameInput.getText().length()>0 && assetLimitInput.getText().length()>0){
                    okButton.setEnabled(true);
                    okButton.setTextColor(Color.parseColor("#ffca28"));
                }else{
                    okButton.setEnabled(false);
                    okButton.setTextColor(Color.GRAY);
                }
            }else{
                if(assetAmountInput.getText().length()>0 && assetNameInput.getText().length()>0){
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
