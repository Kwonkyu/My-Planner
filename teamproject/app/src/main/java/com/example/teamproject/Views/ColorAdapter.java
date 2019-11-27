package com.example.teamproject.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamproject.R;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    public int selectedItem = 0;
    private ArrayList<String> itemList;
    private Context context;

    public ColorAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // context 와 parent.getContext() 는 같다.
        View view = LayoutInflater.from(context)
                .inflate(R.layout.color_recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.radioButton.setChecked(position == selectedItem);
        String item = itemList.get(position);
        StateListDrawable drawable = new StateListDrawable();

        GradientDrawable circle = (GradientDrawable) ContextCompat.getDrawable(context,R.drawable.circle);
        BitmapDrawable check = (BitmapDrawable) ContextCompat.getDrawable(context,R.drawable.check);
        circle.setColor(Color.parseColor(item));
        circle.setStroke((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, context.getResources().getDisplayMetrics()), Color.parseColor(item));
        Drawable[] layer = {circle, check};
        LayerDrawable checkedDrawable = new LayerDrawable(layer);
        checkedDrawable.setLayerInset(1,10,10,10,10);
        drawable.addState(new int[] { -android.R.attr.state_checked}, circle);
        drawable.addState(new int[] { android.R.attr.state_checked }, checkedDrawable);
        holder.radioButton.setBackground(drawable);

        holder.radioButton.setTag(item);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.item_radio);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItem = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });

        }
    }
}
