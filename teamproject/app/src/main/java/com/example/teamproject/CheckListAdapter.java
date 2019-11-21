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
        TextView listitem;
        public MyViewHolder(View v){
            super(v);
            listitem = (TextView)v.findViewById(R.id.checklist_item);
        }
    }

    public CheckListAdapter(ArrayList<String> dataSet){
        this.dataSet = dataSet;
    }

    @Override @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklistitem, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.listitem.setText(dataSet.get(position));
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
