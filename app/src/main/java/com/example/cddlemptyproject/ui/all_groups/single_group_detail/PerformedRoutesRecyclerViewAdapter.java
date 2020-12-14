package com.example.cddlemptyproject.ui.all_groups.single_group_detail;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.logic.data.model.Group;
import com.example.cddlemptyproject.logic.data.model.RoutesPerformed;

import java.util.List;




public class PerformedRoutesRecyclerViewAdapter extends RecyclerView.Adapter<PerformedRoutesRecyclerViewAdapter.ViewHolder> {

    private List<RoutesPerformed> mData;
    private LayoutInflater mInflater;
    private PerformedRoutesRecyclerViewAdapter.ItemClickListener mClickListener;


    // data is passed into the constructor
    PerformedRoutesRecyclerViewAdapter(Context context, List<RoutesPerformed> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void changeDataSet(List<RoutesPerformed> data){
        mData.clear();
        mData = data;
        notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @Override
    public PerformedRoutesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.simple_rv_item2, parent, false);
        return new PerformedRoutesRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(PerformedRoutesRecyclerViewAdapter.ViewHolder holder, int position) {
        RoutesPerformed rota = mData.get(position);

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
    RoutesPerformed getItem(int id) {
        return mData.get(id);
    }


    // allows clicks events to be caught
    void setClickListener(PerformedRoutesRecyclerViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}