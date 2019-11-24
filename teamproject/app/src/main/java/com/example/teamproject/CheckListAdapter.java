package com.example.teamproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {
    private ArrayList<String> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView listItem;
        public MyViewHolder(View v){
            super(v);
            listItem = v.findViewById(R.id.checklist_item);
        }
    }

    public CheckListAdapter(ArrayList<String> dataSet){
        this.dataSet = dataSet;
    }

    @Override @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.listItem.setText(dataSet.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
