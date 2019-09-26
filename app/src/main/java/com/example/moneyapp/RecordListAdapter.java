package com.example.moneyapp;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;



public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.RecordViewHolder> {

    // hold record item
    private final LinkedList<String> mRecordList;

    private LayoutInflater mInflater;

    /*
    read from shared preference into a linkedlist<string>, full record, line by line
    read full record line into 2 parts, a date part, and a info part
    if no date exist, add a date, add info associate with this date
    if date exist, append to this date

    date 1 - info 1
    date 1 - info 2

    date 2 - info 3

    date 3 - null

    date 4 - info 4
    date 4 - info 5
    date 4 - info 6

    ------------------------------------

    date 1: info 1 + info 2
    date 2: info 3
    date 4: info 4 + info 5 + inf 6

    { date 1, info 1, info 2, date 2, info 3, date 4, info 4, info 5, info 6 }

     */



    public RecordListAdapter(Context context,
                             LinkedList<String> recordList) {
        mInflater = LayoutInflater.from(context);
        this.mRecordList = recordList;

    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.record_list,
                parent, false);

        return new RecordViewHolder(mItemView, this);
    }

    // ### assign textview to cards ###

    // assign textview with record_list passed in
    @Override
    public void onBindViewHolder(RecordViewHolder recordViewHolder, int position) {

        // set cardview with text
//        String mCurrent = mRecordList.get(position);
//        recordViewHolder.recordList.setText(mCurrent);

//        String[] arrOfCurrent = mCurrent.split("---");


        // ### want to put record on the same date into one card

        String mCurrent = mRecordList.get(position);

        String[] arrOfCurrent = mCurrent.split("---");

        System.out.println("position is: " + position + " , record is: " + mCurrent);


        recordViewHolder.recodCard_date.setText(arrOfCurrent[4]);
        recordViewHolder.recordList_record.setText(mCurrent);


        // want to put record on the same date into one card ###

    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView recordList_record;
        public CardView recordCard;

        public TextView recodCard_date;

        final RecordListAdapter rAdapter;


        public RecordViewHolder(View itemView, RecordListAdapter adapter) {
            super(itemView);

            recordList_record = itemView.findViewById(R.id.xml_recordList_record);
            recordCard = itemView.findViewById(R.id.xml_recordCard);

            recodCard_date = itemView.findViewById(R.id.xml_recordCard_date);


            this.rAdapter = adapter;

//             //normal click
//            itemView.setOnClickListener(this);

            // long click
            recordCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Get the position of the item that was clicked.
                    final int mPosition = getLayoutPosition();
                    // Use that to access the affected item in mWordList.
                    final String element = mRecordList.get(mPosition);

                    // make dialog for edit or delete
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                    alertDialogBuilder.setMessage("Modify Record: No." + mPosition);

                    alertDialogBuilder.setPositiveButton("Edit",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {

                                }
                            });
                    alertDialogBuilder.setNegativeButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    // Change the word in the mWordList.
                                    mRecordList.remove(mPosition);
                                    rAdapter.notifyDataSetChanged();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    // Notify the adapter, that the data has changed so it can
                    // update the RecyclerView to display the data.
                    rAdapter.notifyDataSetChanged();

                    return false;
                }
            });
        }


        // interact with item in record list, normal click
        @Override
        public void onClick(View v) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mWordList.
            String element = mRecordList.get(mPosition);

            // Change the word in the mWordList.
//            mRecordList.set(mPosition, "Clicked! " + element);
//            // Notify the adapter, that the data has changed so it can
//            // update the RecyclerView to display the data.
//            rAdapter.notifyDataSetChanged();

            Toast.makeText(v.getContext(), "you short clicked: " + element, Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            alertDialogBuilder.setMessage("Modify Record: No." + mPosition);

            alertDialogBuilder.setPositiveButton("Edit",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {

                        }
                    });
            alertDialogBuilder.setNegativeButton("Delete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {

                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
