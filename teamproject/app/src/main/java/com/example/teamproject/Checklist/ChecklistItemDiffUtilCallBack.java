package com.example.teamproject.Checklist;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.teamproject.Model.CheckListItem;

import java.util.ArrayList;

public class ChecklistItemDiffUtilCallBack extends DiffUtil.Callback {

    private final ArrayList<CheckListItem> oldList;
    private final ArrayList<CheckListItem> newList;

    public ChecklistItemDiffUtilCallBack(ArrayList<CheckListItem> oldList, ArrayList<CheckListItem> newList){
        this.oldList = oldList;
        this.newList = newList;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition) == newList.get(newItemPosition);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
