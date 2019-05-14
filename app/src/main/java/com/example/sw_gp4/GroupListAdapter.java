package com.example.sw_gp4;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.ArrayList;

public class GroupListAdapter extends BaseSwipeAdapter {

    private Context mContext;

    // TODO: populate group_names with post
    private ArrayList<String> group_names;
    private ArrayList<Integer> group_colors;

    public GroupListAdapter(Context mContext, ArrayList<Integer> group_colors, ArrayList<String> group_names) {
        this.mContext = mContext;
        resetData(group_colors,group_names);
    }

    public void resetData( ArrayList<Integer> group_colors, ArrayList<String> group_names){
        this.group_names = group_names;
        this.group_colors = group_colors;
        notifyDataSetChanged();
        closeAllItems();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, null);

        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();
                ((GroupList) mContext).OnDeleteClicked(view, position);
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        ImageButton color_button = (ImageButton) convertView.findViewById(R.id.group_color);
        Button text_button = (Button) convertView.findViewById(R.id.group_text);

        ((GradientDrawable) color_button.getBackground()).setColor(
                ContextCompat.getColor(mContext,group_colors.get(position%5)));//防止颜色越界
        text_button.setText(group_names.get(position));

        // TODO: click button -> show group DDL
    }

    @Override
    public int getCount() {
        return group_names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
