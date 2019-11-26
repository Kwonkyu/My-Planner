package com.example.teamproject;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {
    private ArrayList<CheckListItem> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView listItem;
        TextView itemDate;
        boolean isDone = false;
        // See checklist_item.xml. There is two TextView for checklist item text and expiration date.
        // Boolean variable for marking checklist item's state between done and undone.
        public MyViewHolder(View v){
            super(v);
            listItem = v.findViewById(R.id.checklist_item);
            itemDate = v.findViewById(R.id.checklist_item_date);
            // Link view to variables.
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    isDone = !isDone;
                    if(isDone) view.setBackgroundColor(Color.LTGRAY);
                    else view.setBackgroundColor(Color.TRANSPARENT);
                    // Change background's color between LightGray(marked) and Transparent(unmarked).
                }
            });
        }
    }
    // Data structure set(ArrayList in FragmentChecklist).
    public CheckListAdapter(ArrayList<CheckListItem> dataSet){
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
        String text = dataSet.get(position).getItemText();
        String date = dataSet.get(position).getItemDateString();
        holder.listItem.setText(text);
        holder.itemDate.setText(date);
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
