package com.example.teamproject.Views;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teamproject.Checklist.FragmentChecklist;
import com.example.teamproject.Model.CheckListItem;
import com.example.teamproject.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ChecklistItemHolder> {
    private ArrayList<CheckListItem> dataSet;
    // When dataSet is static, it doesn't show items. Why?

    public class ChecklistItemHolder extends RecyclerView.ViewHolder{ // it was no need to be static at all...
        int itemId;
        TextView listItem;
        TextView itemDate;
        TextView itemPlace;
        ImageView imageView;
        boolean isDone;
        // See checklist_item.xml. There is two TextView for checklist item text and expiration date.
        // Boolean variable for marking checklist item's state between done and undone.
        Handler handler = new Handler(Looper.getMainLooper());

        ChecklistItemHolder(View v){
            super(v);
            listItem = v.findViewById(R.id.checklist_item);
            itemDate = v.findViewById(R.id.checklist_item_date);
            itemPlace = v.findViewById(R.id.checklist_item_place);
            imageView = v.findViewById(R.id.checklist_item_checkbox);
            // Link view to variables.

            final Runnable checked = new Runnable() {
                @Override
                public void run() {
                    listItem.setPaintFlags(listItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    listItem.setTextColor(Color.LTGRAY);
                    itemDate.setPaintFlags(itemDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    itemDate.setTextColor(Color.LTGRAY);
                    itemPlace.setPaintFlags(itemPlace.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    itemPlace.setTextColor(Color.LTGRAY);
                    imageView.setImageResource(R.drawable.checked);
                }
            };

            final Runnable unchecked = new Runnable() {
                @Override
                public void run() {
                    listItem.setPaintFlags(0);
                    listItem.setTextColor(Color.BLACK);
                    itemDate.setPaintFlags(0);
                    itemDate.setTextColor(Color.BLACK);
                    itemPlace.setPaintFlags(0);
                    itemPlace.setTextColor(Color.BLACK);
                    imageView.setImageResource(R.drawable.unchecked);
                }
            };


            Thread t = new Thread(){
                boolean prev = isDone;
                @Override
                public void run() {
                    while(!isInterrupted()){
                        if(prev == isDone)  continue;
                        prev = isDone;
                        if(isDone) handler.postAtFrontOfQueue(checked);
                        else handler.postAtFrontOfQueue(unchecked);
                    }
                }
            };
            t.start();

            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    isDone = !isDone;

                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        dataSet.get(pos).setDone(isDone);
                        int id = dataSet.get(pos).getId();
                        int doneInt = isDone ? 1 : 0;
                        String SQL = "UPDATE checklist SET done=? WHERE _id=?";
                        Object[] params = {doneInt, id};
                        FragmentChecklist.db.execSQL(SQL, params);
                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // TODO: edit or remove checklist item?
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        // TODO: implement here!
                    }
                    return false;
                }
            });
            // https://recipes4dev.tistory.com/168
        }
    }
    // Data structure set(ArrayList in FragmentChecklist).
    public CheckListAdapter(ArrayList<CheckListItem> newDataSet){
        dataSet = newDataSet;
    }

    @Override @NonNull
    public ChecklistItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checklist_item, parent, false);
        ChecklistItemHolder vh = new ChecklistItemHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ChecklistItemHolder holder, int position) {
        int id = dataSet.get(position).getId();
        String text = dataSet.get(position).getItemText();
        String date = dataSet.get(position).getItemDateString();
        String place = dataSet.get(position).getItemPlace();
        boolean done = dataSet.get(position).getDone();

        holder.itemId = id;
        holder.listItem.setText(text);
        holder.itemDate.setText(date);
        holder.itemPlace.setText(place);
        holder.isDone = done;
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
