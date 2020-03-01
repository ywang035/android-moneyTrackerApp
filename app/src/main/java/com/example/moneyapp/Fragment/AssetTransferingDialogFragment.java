package com.example.moneyapp.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneyapp.Activity.AssetActivity;
import com.example.moneyapp.Helper.AssetData;
import com.example.moneyapp.Helper.DecimalDigitsInputFilter;
import com.example.moneyapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AssetTransferingDialogFragment extends DialogFragment {

    Button okButton;
    Spinner assetSpinner1;
    Spinner assetSpinner2;
    String sourceAsset;
    String targetAsset;
    int sourceAssetID;
    int targetAssetID;
    double amount;
    EditText amountInput;
    EditText noteInput;
    Button datePicker;


    public static final int REQUEST_CODE = 11;

    private List<AssetData> assetList = new ArrayList<>();
    String selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setCanceledOnTouchOutside(false);

        View rootView = inflater.inflate(R.layout.fragment_asset_transfer, container,
                false);

        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        assetList = (List<AssetData>) getArguments().getSerializable("AssetList");

        builder.setTitle("Transfer Asset");

        // Edited: Overriding onCreateView is not necessary in your case
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_asset_transfer, null);
        builder.setView(view);

        amountInput = view.findViewById(R.id.dialogFragment_transferAsset_amount);
        amountInput.setFocusableInTouchMode(false);

        noteInput = view.findViewById(R.id.dialogFragment_transferAsset_note);

        datePicker = view.findViewById(R.id.asset_transfer_datePickerButton);
        // create instance for calendar, set to date button as current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        selectedDate = year + "-" + month + "-" + day ;
        datePicker.setText(selectedDate);

        // initiate asset spinner option list
        List<String> list = new ArrayList<String>();
        list.add("Select an asset ...");
        for (int i = 0; i < assetList.size(); i++) {
            String assetInfo = "\t\t\t" + assetList.get(i).getName() + "\t\t\t\t\t" + assetList.get(i).getType() + "\t\t\t\t\t" + assetList.get(i).getAmount() + "\t\t\t";
            list.add(assetInfo);
        }

        // populate asset spinner 1
        assetSpinner1 = view.findViewById(R.id.asset_transfer_spinner_from);
        final int[] s1_position = new int[1];
        final int[] s2_position = new int[1];
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else if(s2_position[0] == position){
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else if(s2_position[0] == position){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);

                }
                return view;
            }
        };

        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assetSpinner1.setAdapter(dataAdapter1);

        // handle asset spinner 1 selection
        assetSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s1_position[0] =position;
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    sourceAsset = selectedItemText;
                    sourceAssetID = assetList.get(position-1).getId();
                    assetSpinner2.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // populate asset spinner 2
        assetSpinner2 = view.findViewById(R.id.asset_transfer_spinner_to);
        assetSpinner2.setEnabled(false);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else if(s1_position[0] == position){
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else if(s1_position[0] == position){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);

                }
                return view;
            }
        };

        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assetSpinner2.setAdapter(dataAdapter2);


        // handle asset spinner 2 selection
        assetSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s2_position[0] =position;
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    targetAsset = selectedItemText;
                    targetAssetID = assetList.get(position-1).getId();
                    amountInput.setFocusableInTouchMode(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        amountInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});

        amountInput.addTextChangedListener(watcher);



        // populate ok/cancel button
        okButton = view.findViewById(R.id.dialogFragment_transferAsset_ok);
        Button cancelButton = view.findViewById(R.id.dialogFragment_transferAsset_cancel);

        okButton.setEnabled(false);
        okButton.setTextColor(Color.GRAY);

        // handle [ok button] click
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String noteMessage = noteInput.getText().toString();
                if(noteInput.getText().length()<1){
                    noteMessage = "empty note";
                }

                Date date= new Date();
                long time = date.getTime();
                int transactionID = Math.abs((int)time);

                amount = Double.parseDouble(amountInput.getText().toString());
                AssetActivity activity = (AssetActivity) getActivity();

                // get current hour min second
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");
                String formattedDate = df.format(c.getTime());
                // formattedDate have current date/time
                selectedDate = selectedDate + "-" + formattedDate;
                activity.processTransferAssetDialogFragment(transactionID, selectedDate, sourceAssetID, targetAssetID, amount, noteMessage);
                dismiss();
                Toast.makeText(getContext(), "Transfer Saved", Toast.LENGTH_SHORT).show();

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
                datePickerFragment.setTargetFragment(AssetTransferingDialogFragment.this, REQUEST_CODE);

                Bundle args = new Bundle();
                args.putString("SOURCE", "AssetTransDialog");
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
            System.out.println("from dialog: " + selectedDate);
            // set the value of the pick date button
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
            if(amountInput.getText().length()>0 && sourceAsset!=null && targetAsset!=null){
                okButton.setEnabled(true);
                okButton.setTextColor(Color.parseColor("#ffca28"));
            }else{
                okButton.setEnabled(false);
                okButton.setTextColor(Color.GRAY);
            }
        }
    };


//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

}
