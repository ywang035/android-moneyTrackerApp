package com.example.moneyapp.Fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.moneyapp.Activity.MainActivity;
import com.example.moneyapp.R;

import java.util.Calendar;

import static com.example.moneyapp.Helper.Constant.TOAST_OK;

public class MonthPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    NumberPicker monthPicker;
    NumberPicker yearPicker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setCanceledOnTouchOutside(false);

        View rootView = inflater.inflate(R.layout.fragment_month_picker, container, false);


        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Month Selection");

        // Edited: Overriding onCreateView is not necessary in your case
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_month_picker, null);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        monthPicker = view.findViewById(R.id.monthPicker);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(month);

        yearPicker = view.findViewById(R.id.yearPicker);

        yearPicker.setMinValue(1900);
        yearPicker.setMaxValue(2100);
        yearPicker.setValue(year);


        NumberPicker.OnValueChangeListener onValueChangeListener =
                new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    }
                };

        builder.setView(view).setPositiveButton(TOAST_OK,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                okButton(view);
                Toast.makeText(getContext(), "Month Selected", Toast.LENGTH_SHORT).show();
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

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.processMonthSelection(monthPicker.getValue(), yearPicker.getValue());
        dismiss();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}
