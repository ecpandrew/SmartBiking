package com.example.cddlemptyproject.ui.all_groups.single_group_detail;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.logic.data.model.Group;
import com.example.cddlemptyproject.logic.data.model.RoutesAvailable;

import java.util.List;

public class AvailableRoutesRecyclerViewAdapter extends RecyclerView.Adapter<AvailableRoutesRecyclerViewAdapter.ViewHolder> {

    private List<RoutesAvailable> mData;
    private LayoutInflater mInflater;
    private AvailableRoutesRecyclerViewAdapter.ItemClickListener mClickListener;


    // data is passed into the constructor
    AvailableRoutesRecyclerViewAdapter(Context context, List<RoutesAvailable> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void changeDataSet(List<RoutesAvailable> data){
        mData.clear();
        mData = data;
        notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @Override
    public AvailableRoutesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.simple_rv_item, parent, false);
        return new AvailableRoutesRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(AvailableRoutesRecyclerViewAdapter.ViewHolder holder, int position) {
        RoutesAvailable rota = mData.get(position);

        holder.routeName.setText(rota.getRouteName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView routeName;



        ViewHolder(View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.routeName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    RoutesAvailable getItem(int id) {
        return mData.get(id);
    }


    // allows clicks events to be caught
    void setClickListener(AvailableRoutesRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}