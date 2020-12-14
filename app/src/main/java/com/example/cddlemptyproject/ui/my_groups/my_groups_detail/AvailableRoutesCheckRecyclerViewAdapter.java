package com.example.cddlemptyproject.ui.my_groups.my_groups_detail;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.logic.data.model.RoutesAvailable;
import com.example.cddlemptyproject.logic.data.model.RoutesRegistered;

import java.util.List;

public class AvailableRoutesCheckRecyclerViewAdapter extends RecyclerView.Adapter<AvailableRoutesCheckRecyclerViewAdapter.ViewHolder> {

    private RoutesRegistered[] mData;
    private LayoutInflater mInflater;
    private AvailableRoutesCheckRecyclerViewAdapter.ItemClickListener mClickListener;
    private int selectedPosition = -1;

    // data is passed into the constructor
    AvailableRoutesCheckRecyclerViewAdapter(Context context, RoutesRegistered[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void changeDataSet(RoutesRegistered[] data){
        mData = null;
        mData = data;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    // inflates the row layout from xml when needed
    @Override
    public AvailableRoutesCheckRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.simple_rv_item_check, parent, false);
        return new AvailableRoutesCheckRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(AvailableRoutesCheckRecyclerViewAdapter.ViewHolder holder, int position) {
        RoutesRegistered rota = mData[position];

        holder.routeName.setText(rota.getNome());
        holder.checkbox.setOnClickListener( view -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
        if (selectedPosition==position){
            holder.checkbox.setChecked(true);
        }
        else {
            holder.checkbox.setChecked(false);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView routeName;
        CheckBox checkbox;


        ViewHolder(View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.routeName);
            checkbox = itemView.findViewById(R.id.check_box);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
//    RoutesRegistered getItem(int id) {
//        return mData.get(id);
//    }


    // allows clicks events to be caught
    void setClickListener(AvailableRoutesCheckRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}