package com.example.teamproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CalendarListAdapter extends RecyclerView.Adapter<CalendarListAdapter.MyViewHolder> {
    private ArrayList<String> dataSet;
    private CalendarListAdapter.RecyclerviewClick recyclerviewClick;



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView listItem;

        public MyViewHolder(View v) {
            super(v);
            listItem = v.findViewById(R.id.calendarlist_item);
        }
    }


    public CalendarListAdapter(ArrayList<String> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendarlist_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.listItem.setText(dataSet.get(position));
        holder.listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerviewClick != null) {
                    recyclerviewClick.onClickRecyclerviewClicked(position);
                }
            }
        });
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

    public interface RecyclerviewClick {
        void onClickRecyclerviewClicked(int position);
    }

    public void setRecyclerviewClick(RecyclerviewClick recyclerviewClick) {
        this.recyclerviewClick = recyclerviewClick;
    }

}
