package com.example.moneyapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneyapp.Helper.AssetData;
import com.example.moneyapp.Helper.EventMessage;
import com.example.moneyapp.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.CardViewHolder> {

    // hold record item
    private List<AssetData> assetList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;


    public AssetListAdapter(Context context,
                                  List<AssetData> recordList
    ) {
        mInflater = LayoutInflater.from(context);
        this.assetList = recordList;
        this.context = context;
    }

    public void setAssetList(List<AssetData> recordList){
        this.assetList = recordList;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.recyclerview_cardview_assetdata_list,
                parent, false);
        return new CardViewHolder(mItemView, this);
    }


    // assign textview with list passed in
    @Override
    public void onBindViewHolder(CardViewHolder cardviewHolder, int i) {

        AssetData mData = assetList.get(i);
        cardviewHolder.assetName.setText(mData.getName());

        if(mData.getType().equals("Credit Card")){
            cardviewHolder.assetLimit.setText(Double.toString(mData.getLimit()));
            cardviewHolder.assetAmount.setText(Double.toString(mData.getAmount()));
        }else{
            cardviewHolder.assetAmount.setText(Double.toString(mData.getAmount()));
        }
        cardviewHolder.assetType.setText(mData.getType());
    }


    @Override
    public int getItemCount() {
        return assetList.size();
    }


    // sub class
    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final AssetListAdapter rAdapter;
        public TextView assetName;
        public TextView assetAmount;
        public TextView assetLimit;
        public TextView assetType;
        public CardView cardView;
        public LinearLayout linearLayout;


        public CardViewHolder(View itemView, AssetListAdapter assetListAdapter) {
            super(itemView);

            assetName = itemView.findViewById(R.id.assetdata_name);
            assetAmount = itemView.findViewById(R.id.assetdata_amount);
            assetLimit = itemView.findViewById(R.id.assetdata_limit);
            assetType = itemView.findViewById(R.id.assetdata_type);
            cardView = itemView.findViewById(R.id.cardView_assetdata);
            linearLayout = itemView.findViewById(R.id.cardView_assetdata_linearlayout);

            this.rAdapter = assetListAdapter;

            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    // Get the position of the item that was clicked.
                    final int mPosition = getLayoutPosition();
                    // Use that to access the affected item in asset data list.
                    final AssetData element = assetList.get(mPosition);

                    // make dialog for edit or delete
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                    alertDialogBuilder.setTitle("Selected Asset");

                    alertDialogBuilder.setMessage(
                                    "Asset ID:" + "\n" + element.getId() +
                                    "\n\nAsset Name:" + "\n" + element.getId() +
                                    "\n\nAsset Type:" + "\n" + element.getType() +
                                    "\n\nAsset Amount:" + "\n" + element.getAmount() +
                                    "\n\nAsset Limit: " + "\n" + element.getLimit());

                    alertDialogBuilder.setPositiveButton("Edit",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {

                                    AssetData assetData = assetList.get(mPosition);
                                    EventBus.getDefault().post(new EventMessage("edit_asset_from_assetAdapter_1", null, assetData, null, null));

                                }
                            });

                    alertDialogBuilder.setNegativeButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {


                                    assetList.remove(mPosition); // delete the transaction
                                    rAdapter.notifyDataSetChanged();

                                    Toast.makeText(itemView.getContext(), "Asset Deleted", Toast.LENGTH_SHORT).show();

                                    EventBus.getDefault().post(new EventMessage("delete_asset_from_assetAdapter", null, null, null, assetList));
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return false;
                }


            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
