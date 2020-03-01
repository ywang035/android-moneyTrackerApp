package com.example.moneyapp.Adapter;


import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneyapp.Helper.AssetData;
import com.example.moneyapp.Helper.EventMessage;
import com.example.moneyapp.Helper.TransactionData;
import com.example.moneyapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.CardViewHolder> {

    // hold record item
    private List<TransactionData> transactionList = new ArrayList<>();
    private List<AssetData> assetList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;


    public TransactionListAdapter(Context context,
                                  List<TransactionData> transactionList,
                                  List<AssetData> assetList
                                  ) {
        mInflater = LayoutInflater.from(context);
        this.transactionList = transactionList;
        this.assetList = assetList;
        this.context = context;
    }

    public void setTransactionList(List<TransactionData> transactionList){
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.recyclerview_cardview_transactiondata_list,
                parent, false);
        return new CardViewHolder(mItemView, this);
    }

    // assign textview with record_list passed in
    @Override
    public void onBindViewHolder(CardViewHolder recordViewHolder, int position) {

        TransactionData mData = transactionList.get(position);
        TransactionData mData_previous = null;
        if ( position > 0){
            mData_previous = transactionList.get(position-1);
        }

        // for [expense|income] transaction
        if(mData.getDirection().equals("EXPENSE") || mData.getDirection().equals("INCOME")){
            // set card text from current transaction
            recordViewHolder.cardView.setCardElevation(0);
            recordViewHolder.card_date.setText(mData.getDate().toLocalDate().toString());
            recordViewHolder.card_cate.setText("• " + mData.category);
            recordViewHolder.card_asset.setText(mData.getAsset());
            recordViewHolder.card_money.setText("$ "+mData.getMoney());

            if(mData.getNote().equals("empty note")){
                recordViewHolder.card_note.setText("");
            }else{
                recordViewHolder.card_note.setText(mData.getNote());
                System.out.println("note is " + mData.getNote() );
            }

            // put transaction with same date in one card (by removing margin)
            LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recordViewHolder.cardView.setLayoutParams(cardViewParams);
            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) recordViewHolder.cardView.getLayoutParams();
            if (mData_previous != null && mData_previous.getDate().toLocalDate().equals(mData.getDate().toLocalDate())){
                cardViewMarginParams.setMargins(10, -7, 10, 0);
                recordViewHolder.card_date.setVisibility(View.GONE);
            }else{
                cardViewMarginParams.setMargins(10, 40, 10, 0);
                recordViewHolder.card_date.setVisibility(View.VISIBLE);
            }
            // set text color for money (+/-)
            if (mData.getDirection().equals("EXPENSE")){
                recordViewHolder.card_money.setTextColor(Color.parseColor("#f4511e"));
                recordViewHolder.card_cate.setTextColor(Color.parseColor("#f4511e"));
            }else if(mData.getDirection().equals("INCOME")){
                recordViewHolder.card_money.setTextColor(Color.parseColor("#4caf50"));
                recordViewHolder.card_cate.setTextColor(Color.parseColor("#4caf50"));
            }
            recordViewHolder.card_cate.setGravity(Gravity.RIGHT);
        }

        // for [transfer asset] transaction
        else if(mData.getDirection().equals("ASSETTRANSFER")){
            // need both assets' name (source and target)
            String targetAsset = mData.getCategory();
            String sourceAsset = null;
            for (int i = 0; i < assetList.size(); i++) {
                if(assetList.get(i).getId()==mData.getAssetID()){
                    sourceAsset = assetList.get(i).getName();
                    break;
                }
            }
            recordViewHolder.cardView.setCardElevation(0);
            recordViewHolder.card_date.setText(mData.getDate().toLocalDate().toString());
            recordViewHolder.card_cate.setText("• " +  "Transfer Asset");
            recordViewHolder.card_asset.setText(sourceAsset + "\t> > >\t" + targetAsset);
            recordViewHolder.card_money.setText("$ "+mData.getMoney());

            if(mData.getNote().equals("empty note")){
                recordViewHolder.card_note.setText("");
            }else{
                recordViewHolder.card_note.setText(mData.getNote());
                System.out.println("note is " + mData.getNote() );
            }

            // put transaction with same date in one card (by removing margin)
            LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recordViewHolder.cardView.setLayoutParams(cardViewParams);
            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) recordViewHolder.cardView.getLayoutParams();
            if (mData_previous != null && mData_previous.getDate().toLocalDate().equals(mData.getDate().toLocalDate())){
                cardViewMarginParams.setMargins(10, -7, 10, 0);
                recordViewHolder.card_date.setVisibility(View.GONE);
            }else{
                cardViewMarginParams.setMargins(10, 40, 10, 0);
                recordViewHolder.card_date.setVisibility(View.VISIBLE);
            }
            if (mData.getDirection().equals("ASSETTRANSFER")){
                recordViewHolder.card_cate.setTextColor(Color.GRAY);
                recordViewHolder.card_money.setTextColor(Color.GRAY);
            }
            recordViewHolder.card_cate.setGravity(Gravity.RIGHT);

        }

        // for [edit asset] transaction
        else if(mData.getDirection().equals("ASSETEDIT")){

            recordViewHolder.cardView.setCardElevation(0);
            recordViewHolder.card_date.setText(mData.getDate().toLocalDate().toString());

            String assetMessage = mData.getAsset();
            String[] assetMessageSplit = assetMessage.split("/");
            String assetNewName = assetMessageSplit[1];
            String editMessage = assetMessageSplit[2];

            recordViewHolder.card_cate.setText("• Edit Asset: " + assetNewName);
            recordViewHolder.card_asset.setText(editMessage);
            recordViewHolder.card_money.setText("");

            if(mData.getNote().equals("empty note")){
                recordViewHolder.card_note.setText("");
            }else{
                recordViewHolder.card_note.setText(mData.getNote());
            }

            // put transaction with same date in one card (by removing margin)
            LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recordViewHolder.cardView.setLayoutParams(cardViewParams);
            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) recordViewHolder.cardView.getLayoutParams();
            if (mData_previous != null && mData_previous.getDate().toLocalDate().equals(mData.getDate().toLocalDate())){
                cardViewMarginParams.setMargins(10, -7, 10, 0);
                recordViewHolder.card_date.setVisibility(View.GONE);
            }else{
                cardViewMarginParams.setMargins(10, 40, 10, 0);
                recordViewHolder.card_date.setVisibility(View.VISIBLE);
            }

            if (mData.getDirection().equals("ASSETEDIT")){
                recordViewHolder.card_cate.setTextColor(Color.GRAY);
                recordViewHolder.card_money.setTextColor(Color.GRAY);
            }
            recordViewHolder.card_cate.setGravity(Gravity.RIGHT);
        }

        recordViewHolder.cardView.requestLayout();

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }


    // sub class
    class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TransactionListAdapter rAdapter;
        public LinearLayout linearLayout_vertical;
        public LinearLayout linearLayou_horizontal1;
        public LinearLayout linearLayou_horizontal2;
        public LinearLayout linearLayou_horizontal3;
        public CardView cardView;
        public TextView card_date;
        public TextView card_money;
        public TextView card_cate;
        public TextView card_asset;
        public TextView card_note;
        public View card_divder;



        public CardViewHolder(View itemView, TransactionListAdapter adapter) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView_transactiondata);
            linearLayout_vertical = itemView.findViewById(R.id.linear_vertical);
            linearLayou_horizontal1 = itemView.findViewById(R.id.linear_horizontal1);
            linearLayou_horizontal2 = itemView.findViewById(R.id.linear_horizontal2);
            linearLayou_horizontal3 = itemView.findViewById(R.id.linear_horizontal3);

            card_date = itemView.findViewById(R.id.cardView_data_date);
            card_money = itemView.findViewById(R.id.cardView_data_money);
            card_cate = itemView.findViewById(R.id.cardView_data_category);
            card_asset = itemView.findViewById(R.id.cardView_data_asset);
            card_note = itemView.findViewById(R.id.cardView_data_note);

            this.rAdapter = adapter;

//             //normal click handling
//            itemView.setOnClickListener(this);

            // long click handling
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Get the position of the item that was clicked.
                    final int mPosition = getLayoutPosition();
                    // Use that to access the affected item in mWordList.
                    final TransactionData transactionData = transactionList.get(mPosition);

                    // make dialog for edit or delete
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setTitle("Selected Transaction");
                    alertDialogBuilder.setMessage(
                            "Transaction ID: " + "\n" + transactionData.getTransactionID()
                    +"\n\nDate:" + "\n" + transactionData.getDate()
                    +"\n\nDirection:" + "\n" + transactionData.getDirection()
                    +"\n\nAsset:" + "\n" + transactionData.getAsset()
                    +"\n\nAsset ID:" + "\n" + transactionData.getAssetID()
                    +"\n\nCategory:" + "\n" + transactionData.getCategory()
                    +"\n\nMoney:" + "\n" + transactionData.getMoney()
                    +"\n\nNote:" + "\n" + transactionData.getNote());

                    alertDialogBuilder.setNegativeButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {

                                    transactionList.remove(mPosition); // delete the transaction
                                    rAdapter.notifyDataSetChanged();
                                    Toast.makeText(itemView.getContext(), "Deleted", Toast.LENGTH_SHORT).show();

                                    EventBus.getDefault().post(new EventMessage("delete_trans_from_transAdapter",transactionData,null, null,null));

                                }
                            });

                    if(transactionData.getDirection().equals("EXPENSE") || transactionData.getDirection().equals("INCOME")){
                        alertDialogBuilder.setPositiveButton("Edit",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {

                                        EventBus.getDefault().post(new EventMessage("edit_trans_from_transAdapter", transactionData, null, null, null));


                                    }
                                });
                    }

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
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
            TransactionData element = transactionList.get(mPosition);

            // Change the word in the mWordList.
//            dataList.set(mPosition, "Clicked! " + element);
//            // Notify the adapter, that the data has changed so it can
//            // update the RecyclerView to display the data.
//            rAdapter.notifyDataSetChanged();

            Toast.makeText(v.getContext(), "you short clicked: " + element, Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            alertDialogBuilder.setMessage("Modify Transaction: No." + mPosition);

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


