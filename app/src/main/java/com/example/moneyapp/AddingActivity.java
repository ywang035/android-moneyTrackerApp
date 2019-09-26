package com.example.moneyapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class AddingActivity extends AppCompatActivity {

    protected static final String EXTRA_REPLY = "com.example.moneyapp.extra.REPLY";

    private EditText dollarInput;
    private EditText noteInput;

    private String dateMessage;
    private String categoryMessage;

    private Button button_date;

    private BottomSheetDialog assetBottomSheet;
    private String dollarMessage;
    private String noteMessage;
    private String assetMessage;
    private String directionMessage = "EXPENSE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);


        // create instance for calendar, set to date button as current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        // date button assign current date
        button_date = findViewById(R.id.xml_button_dateInput);
        String currentDate = month + "/" + day + "/" + year;
        button_date.setText(currentDate);
        dateMessage = currentDate;

        // get edit text input, dollar and note
        dollarInput = findViewById(R.id.xml_editText_dollarInput);
        noteInput = findViewById(R.id.xml_editText_noteInput);

        // format dollar input with 2 decimal point
        dollarInput.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(12,2)});

        // set right align on dollar input
        dollarInput.setGravity(Gravity.RIGHT);
        // set focus on dollar input
        dollarInput.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // set function to dollarInput edittext function
        dollarInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            // with actionDone
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                // check if input is not empty
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (dollarInput.getText().toString().length() > 0) {
                        sendToMain1(v);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        return true;
                    } else {
                        dollarInput.requestFocus();
                    }
                }
                // not hide keyboard
                return true;
            }
        });


        // Create an instance of the tab layout from the view.
        final TabLayout addingtabLayout = findViewById(R.id.tab_layout);
        // Set the text for each tab.
        addingtabLayout.addTab(addingtabLayout.newTab().setText(R.string.adding_tab_label1));
        addingtabLayout.addTab(addingtabLayout.newTab().setText(R.string.adding_tab_label2));
        // Set the tabs to fill the entire layout.
        addingtabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // use AddingPagerAdapter to manage page view in fragments
        // each page is represented by its own fragment
        final ViewPager viewPager = findViewById(R.id.pager);
        final AddingPagerAdapter addingPagerAdapter = new AddingPagerAdapter(getSupportFragmentManager(), addingtabLayout.getTabCount());
        viewPager.setAdapter(addingPagerAdapter);


        // setting a listener for tab clicks and setting expense/income message
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(addingtabLayout));
        addingtabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        if(tab.getPosition()==0){
                            directionMessage = "EXPENSE";

                            // reset fragment 1
                            viewPager.setAdapter(addingPagerAdapter);
                            categoryMessage = "FOOD";


                        }else if(tab.getPosition() == 1){
                            directionMessage = "INCOME";

                            // reset fragment 2
                            viewPager.setAdapter(addingPagerAdapter);
                            categoryMessage = "CASH";

                        }
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) { }
                });
    }


    // show date picker fragment, assigned to date button
    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // process chosen date from user
    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month + 1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        dateMessage = (month_string +
                "/" + day_string + "/" + year_string);

        button_date.setText(dateMessage);

//        Toast.makeText(this, "Date: " + dateMessage,
//                Toast.LENGTH_SHORT).show();
    }

    // sending, used to be from confirm button
    public void sendToMain1(View view) {

        dollarMessage = dollarInput.getText().toString();
        Number dollarNum = null;
        try {
            dollarNum = NumberFormat.getInstance(Locale.US).parse(dollarMessage);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        if(dollarNum != null){
//            System.out.println("num is:" + dollarNum);
//        }else{
//            System.out.println("test 2 num is: " + dollarNum);
//        }

        noteMessage = noteInput.getText().toString();

        if (dollarInput.length()>0){

            if (categoryMessage == null) {
                categoryMessage = "FOOD";
            }
            if (noteMessage.length() < 1) {
                noteMessage = "empty note";
            }
            if (assetMessage == null) {
                assetMessage = "CARD 1";
            }

            String mainMessage = directionMessage + "---" +  dollarNum + "---" + assetMessage + "---" + noteMessage + "---" + dateMessage + "---" + categoryMessage;

            Intent replyIntent = new Intent();
            replyIntent.putExtra(EXTRA_REPLY, mainMessage);
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    // sending from number pad
    private void sendToMain2() {
        dollarMessage = dollarInput.getText().toString();
        Number dollarNum = null;
        try {
            dollarNum = NumberFormat.getInstance(Locale.US).parse(dollarMessage);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(dollarNum != null){
            System.out.println("num is:" + dollarNum);
        }else{
            System.out.println("test 2 num is: " + dollarNum);
        }

        noteMessage = noteInput.getText().toString();


        if (categoryMessage == null) {
            categoryMessage = "FOOD";
        }
        if (noteMessage.length() < 1) {
            noteMessage = "empty note";
        }
        if (assetMessage == null) {
            assetMessage = "card 1";
        }

        String mainMessage = directionMessage + " -- " +  dollarMessage + " -- " + assetMessage + " -- " + noteMessage + " -- " + dateMessage + " -- " + categoryMessage;

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, mainMessage);
        setResult(RESULT_OK, replyIntent);
        finish();
    }


    // process date picker fragment selection
    public void processCategory(String message) {
        categoryMessage = message;
    }

    // create and process bottom sheet and selection
    public void showBottomSheet(View view) {
        try {
            // inflate bottom sheet
            View sheetView = getLayoutInflater().inflate(R.layout.asset_bottom_sheet, null);
            assetBottomSheet = new BottomSheetDialog(this);
            assetBottomSheet.setContentView(sheetView);
            assetBottomSheet.show();

            // Remove default white color background
            FrameLayout bottomSheet = (FrameLayout) assetBottomSheet.findViewById(R.id.design_bottom_sheet);
            final BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheet.setBackground(null);

            LinearLayout tab1 = (LinearLayout) sheetView.findViewById(R.id.xml_bottomSheet_tab1);
            LinearLayout tab2 = (LinearLayout) sheetView.findViewById(R.id.xml_bottomSheet_tab2);

            tab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    assetMessage = "CARD 1";

                    Button btnTxt = findViewById(R.id.xml_button_assetInput);
                    btnTxt.setText(assetMessage);

                    assetBottomSheet.dismiss();
                }
            });

            tab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    assetMessage = "CARD 2";

                    Button btnTxt = findViewById(R.id.xml_button_assetInput);
                    btnTxt.setText(assetMessage);

                    assetBottomSheet.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
