package com.example.teamproject.Views;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamproject.Model.CheckListItem;
import com.example.teamproject.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {
    private ArrayList<CheckListItem> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView listItem;
        TextView itemDate;
        TextView itemPlace;
        ImageView imageView;
        boolean isDone;
        // See checklist_item.xml. There is two TextView for checklist item text and expiration date.
        // Boolean variable for marking checklist item's state between done and undone.

        public MyViewHolder(View v){
            super(v);
            listItem = v.findViewById(R.id.checklist_item);
            itemDate = v.findViewById(R.id.checklist_item_date);
            itemPlace = v.findViewById(R.id.checklist_item_place);
            imageView = v.findViewById(R.id.checklist_item_checkbox);
            // Link view to variables.

            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    isDone = !isDone;
                    // TODO: how to change dataSet's isDone variable? Need to send broadcast?
                    if(isDone) {
                        listItem.setPaintFlags(listItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        listItem.setTextColor(Color.LTGRAY);
                        itemDate.setPaintFlags(itemDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        itemDate.setTextColor(Color.LTGRAY);
                        itemPlace.setPaintFlags(itemPlace.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        itemPlace.setTextColor(Color.LTGRAY);
                        imageView.setImageResource(R.drawable.checked);
                    }
                    else{
                        listItem.setPaintFlags(0);
                        listItem.setTextColor(Color.BLACK);
                        itemDate.setPaintFlags(0);
                        itemDate.setTextColor(Color.BLACK);
                        itemPlace.setPaintFlags(0);
                        itemPlace.setTextColor(Color.BLACK);
                        imageView.setImageResource(R.drawable.unchecked);
                    }
                    // Change background's color between LightGray(marked) and Transparent(unmarked).
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // TODO: edit or remove checklist item?
                    Toast.makeText(view.getContext(), "long click", Toast.LENGTH_SHORT).show();
                    return false;
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
        String place = dataSet.get(position).getItemPlace();
        Boolean isDone = dataSet.get(position).getDone();

        holder.listItem.setText(text);
        holder.itemDate.setText(date);
        holder.itemPlace.setText(place);
        holder.isDone = isDone;
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
