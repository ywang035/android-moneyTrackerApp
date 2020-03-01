package com.example.moneyapp.Fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import com.example.moneyapp.Activity.TransAddingActivity;
import com.example.moneyapp.Activity.TransEditingActivity;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment  extends DialogFragment
        implements DatePickerDialog.OnDateSetListener{



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);



        if(getArguments()!=null){

            if(getArguments().getString("SOURCE").equals("TransEditActivity")){
                year = getArguments().getInt("YEAR");
                month = getArguments().getInt("MONTH")-1;
                day = getArguments().getInt("DATE");
            }
            else if(getArguments().getString("SOURCE").equals("AssetTransDialog")){
            }
        }



        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker,
                          int year, int month, int day) {
        month += 1;

        if(getArguments()==null){
            TransAddingActivity activity = (TransAddingActivity) getActivity();
            activity.processDatePickerResult(year, month, day);
        }

        else if(getArguments().getString("SOURCE").equals("TransEditActivity")){
            TransEditingActivity activity = (TransEditingActivity) getActivity();
            activity.processDatePickerResult(year, month, day);

        }
        else if(getArguments().getString("SOURCE").equals("AssetTransDialog") ||
                getArguments().getString("SOURCE").equals("AssetEditDialog") ){

            String selectedDate = year + "-" + month +"-" + day;

            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_OK,
                    new Intent().putExtra("SELECTDATE", selectedDate)
            );
        }

    }

}
