package com.example.moneyapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneyapp.Helper.AssetData;
import com.example.moneyapp.Fragment.DatePickerFragment;
import com.example.moneyapp.Helper.DecimalDigitsInputFilter;
import com.example.moneyapp.R;
import com.example.moneyapp.Adapter.TransEditingPagerAdapter;
import com.example.moneyapp.Helper.TransactionData;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransEditingActivity extends AppCompatActivity {

    protected static final String EXTRA_REPLY = "com.example.moneyapp.extra.REPLY";

    private List<AssetData> assetDataList = new ArrayList<AssetData>();
    private TransactionData transactionData;

    private EditText moneyInput;
    private EditText noteInput;
    private Button button_date;
    private Button assetInputButton;
    private BottomSheetDialog assetBottomSheet;
    private String dateMessage;
    private String categoryMessage;
    private String moneyMessage;
    private String noteMessage;
    private String assetMessage;
    private String directionMessage;
    private int assetIndexMessage;
    private int assetID;

    private String oldDateStr;
    private String newDateStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // receive intent from previous activity
        Intent intent = getIntent();
        assetDataList = (List<AssetData>) intent.getSerializableExtra("ASSET_LIST");
        transactionData = (TransactionData) intent.getSerializableExtra("TRANSACTION_OBJECT");


        // process default note message
        noteInput = findViewById(R.id.editText_noteInput);
        noteMessage = transactionData.getNote();

        // process defalut date message
        int transactionYear = transactionData.getDate().getYear();
        int transactionMonth = transactionData.getDate().getMonthValue();
        int transactionDate = transactionData.getDate().getDayOfMonth();
        // date button assign to defult date
        button_date = findViewById(R.id.button_dateInput);
//        String selectedDate = transactionYear + "-" + transactionMonth + "-" + transactionDate;
        oldDateStr = transactionData.getDate().toLocalDate().toString();
        newDateStr = transactionData.getDate().toLocalDate().toString();
        button_date.setText(oldDateStr);

        // assign money input to defult value
        moneyMessage = String.valueOf(transactionData.getMoney());
        moneyInput = findViewById(R.id.editText_dollarInput);
        moneyInput.setText(moneyMessage);
        // format dollar input with 2 decimal point
        moneyInput.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(12, 2)});
        // set right align on dollar input
        moneyInput.setGravity(Gravity.RIGHT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // set function to moneyInput edittext function
        moneyInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            // with actionDone
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                // check if input is not empty
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (moneyInput.getText().toString().length() > 0) {
//                        sendToMain(v);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        return true;
                    } else {
                        moneyInput.requestFocus();
                    }
                }
                // not hide keyboard
                return true;
            }
        });



        // process defult asset message
        // set asset button with defult asset name
        assetInputButton = findViewById(R.id.button_assetInput);
        assetMessage = transactionData.getAsset();
        assetInputButton.setText(transactionData.getAsset());
        assetID = transactionData.getAssetID();

        for (int i = 0; i < assetDataList.size(); i++) {
            if(assetDataList.get(i).getId()==assetID){
                assetIndexMessage = i;
            }
        }


        // process direction message
        directionMessage = transactionData.getDirection();
        // process category message
        categoryMessage = transactionData.getCategory();

        noteMessage = transactionData.getNote();
        if(noteMessage.equals("empty note")){
            noteInput.setHint("note of purchase");
        }else{
            noteInput.setText(noteMessage);
        }



        // Create an instance of the tab layout from the view.
        final TabLayout categoryTabLayout = findViewById(R.id.tab_layout);
        // Set the text for each tab.
        categoryTabLayout.addTab(categoryTabLayout.newTab().setText(R.string.adding_tab_label1));
        categoryTabLayout.addTab(categoryTabLayout.newTab().setText(R.string.adding_tab_label2));
        // Set the tabs to fill the entire layout.
        categoryTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // use TransAddingPagerAdapter to manage page view in fragments
        // each page is represented by its own fragment
        final ViewPager viewPager = findViewById(R.id.pager);
        final TransEditingPagerAdapter editingPagerAdapter = new TransEditingPagerAdapter(getSupportFragmentManager(), categoryTabLayout.getTabCount(), categoryMessage);
        viewPager.setAdapter(editingPagerAdapter);
        // setting a listener for tab clicks and setting expense/income message
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(categoryTabLayout));
        // set tab based on default direction message
        if(directionMessage.equals("EXPENSE")){
            viewPager.setCurrentItem(0);
            // select tab action, will reset category
            categoryTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        directionMessage = "EXPENSE";
                        categoryMessage = transactionData.getCategory();
                        editingPagerAdapter.setter(categoryMessage);
                        // reset fragment 1
                        viewPager.setAdapter(editingPagerAdapter);
//                    categoryMessage = "Restaurant";


                    } else if (tab.getPosition() == 1) {
                        directionMessage = "INCOME";
                        categoryMessage = "Salary";
                        editingPagerAdapter.setter(categoryMessage);
                        // reset fragment 2
                        viewPager.setAdapter(editingPagerAdapter);
//                    categoryMessage = "Salary";
                    }
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

        }else if(directionMessage.equals("INCOME")){
            viewPager.setCurrentItem(1);
            // select tab action, will reset category
            categoryTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        directionMessage = "EXPENSE";
                        categoryMessage = "Restaurant";
                        editingPagerAdapter.setter(categoryMessage);
                        // reset fragment 1
                        viewPager.setAdapter(editingPagerAdapter);
//                    categoryMessage = "Restaurant";


                    } else if (tab.getPosition() == 1) {
                        directionMessage = "INCOME";
                        categoryMessage = transactionData.getCategory();
                        editingPagerAdapter.setter(categoryMessage);
                        // reset fragment 2
                        viewPager.setAdapter(editingPagerAdapter);
//                    categoryMessage = "Salary";
                    }
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }
    }



    // process category picker fragment selection
    public void processCategory(String message) {
        categoryMessage = message;
    }

    // show date picker fragment, assigned to date button and dialog with pre-selected date
    public void showDatePicker(View view) {

        String sourceDateText= button_date.getText().toString();
        String[] date_split = sourceDateText.split("-");
        int sourceYear = Integer.parseInt(date_split[0]);
        int sourceMonth = Integer.parseInt(date_split[1]);
        int sourceDate = Integer.parseInt(date_split[2]);

        DialogFragment newFragment = new DatePickerFragment();

        Bundle args = new Bundle();
        args.putString("SOURCE", "TransEditActivity");
        args.putInt("YEAR", sourceYear);
        args.putInt("MONTH", sourceMonth);
        args.putInt("DATE", sourceDate);
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // process chosen date from user
    public void processDatePickerResult(int year, int month, int day) {
        String month_string = Integer.toString(month);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);

        LocalDate selectedDate = LocalDate.of(year, month, day);

//        dateMessage = (year_string + "-" + month_string +
//                "-" + day_string);

        newDateStr = selectedDate.toString();

        button_date.setText(newDateStr);
    }

    // create and process bottom sheet and selection
    public void showBottomSheet(View view) {

        try {

            // inflate bottom sheet
            View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_pick_asset, null);

            assetBottomSheet = new BottomSheetDialog(getLayoutInflater().getContext());

            LinearLayout linearLayoutMain = sheetView.findViewById(R.id.asset_bottom_sheet_linearlayout1);

            ArrayList<LinearLayout> linearLayoutsList = new ArrayList<>();
            ArrayList<TextView> textviewList = new ArrayList<>();
            ArrayList<View> dividerList = new ArrayList<>();

            for (int i = 0; i < assetDataList.size(); i++) {

                LinearLayout linearLayoutSub = new LinearLayout(this);
                LinearLayout linearLayoutTop = new LinearLayout(this);
                LinearLayout linearLayoutBottom = new LinearLayout(this);
                linearLayoutSub.setOrientation(LinearLayout.VERTICAL);
                linearLayoutSub.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayoutSub.setPadding(40, 0, 40, 0);
                linearLayoutTop.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutBottom.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutBottom.setPadding(0, 0, 0, 20);

                TextView nameText = new TextView(this);
                TextView amountText = new TextView(this);
                TextView limitText = new TextView(this);
                TextView typeText = new TextView(this);

                nameText.setText(assetDataList.get(i).getName());
                nameText.setTextSize(17);

                amountText.setText(Double.toString(assetDataList.get(i).getAmount()));
                amountText.setTextSize(17);
                amountText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                amountText.setGravity(Gravity.END);
                typeText.setText(assetDataList.get(i).getType());

                if (assetDataList.get(i).getType().equals("Credit Card")) {
                    amountText.setText(Double.toString(assetDataList.get(i).getAmount()));
                    limitText.setText(Double.toString(assetDataList.get(i).getLimit()));
                    limitText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    limitText.setGravity(Gravity.END);
                }

                int index = i;

                View divider = new View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2));
                divider.setBackgroundColor(Color.parseColor("#E5E5E5"));

                linearLayoutSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        assetMessage = nameText.getText().toString();
                        assetIndexMessage = index;
                        assetID = assetDataList.get(index).getId();
                        assetInputButton.setText(assetMessage);
                        assetBottomSheet.dismiss();
                    }
                });

                // keep element in list
                linearLayoutsList.add(linearLayoutSub);
                linearLayoutsList.add(linearLayoutTop);
                linearLayoutsList.add(linearLayoutBottom);

                textviewList.add(nameText);
                textviewList.add(amountText);
                textviewList.add(limitText);
                textviewList.add(typeText);

                dividerList.add(divider);

                linearLayoutSub.addView(linearLayoutTop);
                linearLayoutSub.addView(linearLayoutBottom);
                linearLayoutSub.addView(divider);

                linearLayoutTop.addView(nameText);
                linearLayoutTop.addView(amountText);
                linearLayoutBottom.addView(typeText);
                linearLayoutBottom.addView(limitText);

                linearLayoutMain.addView(linearLayoutSub);
            }

            assetBottomSheet.setContentView(sheetView);
            assetBottomSheet.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // sending edit transaction back to main
    public void sendToMain(View view) {

        moneyMessage = moneyInput.getText().toString();
        noteMessage = noteInput.getText().toString();

        if (moneyInput.length() > 0) {
            if (categoryMessage == null) {
                categoryMessage = "Restaurant";
            }
            if (noteMessage.length() < 1) {
                noteMessage = "empty note";
            }
            if(assetMessage.equals("Asset")){
                assetIndexMessage = 123456;
            }
            String transactionID = String.valueOf(transactionData.getTransactionID());

            // get current hour min second
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss");
            String formattedDate = df.format(c.getTime());
            // formattedDate have current date/time


            if(oldDateStr.equals(newDateStr)){
                int hour = transactionData.getDate().getHour();
                int minute = transactionData.getDate().getMinute();
                int second = transactionData.getDate().getSecond();
                dateMessage = oldDateStr + "-" + hour + "-" + minute + "-" + second;
            }else{
                dateMessage = newDateStr + "-" + formattedDate;
            }

            String[] dateSplit = dateMessage.split("-");
            int year = Integer.parseInt(dateSplit[0]);
            int month = Integer.parseInt(dateSplit[1]);
            int day = Integer.parseInt(dateSplit[2]);
            int hour = Integer.parseInt(dateSplit[3]);
            int minute = Integer.parseInt(dateSplit[4]);
            int second = Integer.parseInt(dateSplit[5]);
            LocalDateTime formatDateTime = LocalDateTime.of(year, month, day, hour, minute, second);

            double moneyNum = Double.parseDouble(moneyMessage);

            // message returned to main activity
            TransactionData newtTansactionData = new TransactionData(Integer.parseInt(transactionID), formatDateTime, directionMessage, categoryMessage, assetMessage, assetID, moneyNum, noteMessage);

            Intent replyIntent = new Intent();

            Bundle bundle = new Bundle();
            bundle.putString("FROM", "edit_transaction");
            bundle.putSerializable("EDIT_TRANSACTION_DATA", newtTansactionData);

            replyIntent.putExtra(EXTRA_REPLY, bundle);
            setResult(RESULT_OK, replyIntent);
            finish();
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
    }


}
