package com.example.cddlemptyproject.ui.all_groups;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.logic.data.model.Group;

import java.util.List;

public class AllGroupsRecyclerViewAdapter extends RecyclerView.Adapter<AllGroupsRecyclerViewAdapter.ViewHolder> {

    private List<Group> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;


    // data is passed into the constructor
    AllGroupsRecyclerViewAdapter(Context context, List<Group> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void changeDataSet(List<Group> data){
        mData.clear();
        mData = data;
        notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.rv_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Group grupo = mData.get(position);

        holder.groupName.setText(grupo.getGroupName());
        holder.groupLeader.setText("Líder: ".concat(grupo.getGroupLeader()));
        holder.groupUuid.setText("UUID: ".concat(grupo.getResourceUuid()));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView groupName;
        TextView groupLeader;
        TextView groupUuid;


        ViewHolder(View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.tvGroupName);
            groupLeader = itemView.findViewById(R.id.tvGroupLeader);
            groupUuid = itemView.findViewById(R.id.tvGroupUuid);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Group getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}