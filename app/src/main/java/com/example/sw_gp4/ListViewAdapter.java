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

public class ListViewAdapter extends BaseSwipeAdapter {

    private Context mContext;

    // TODO: colors?
    private int num_colors = 5;
    private int colors[] = {R.color.gp_1, R.color.gp_2, R.color.gp_3, R.color.gp_4, R.color.gp_5};

    // TODO: populate group_names with post
    private int num_groups;
    private String group_names[];

    public ListViewAdapter(Context mContext, int num_groups, String[] group_names) {
        this.mContext = mContext;
        this.num_groups = num_groups;
        this.group_names = new String[num_groups];
        for(int i=0; i<num_groups; ++i) this.group_names[i] = group_names[i];
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, null);

        v.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "click delete", Toast.LENGTH_SHORT).show();

                // Todo: delete/leave group request
                // Confirmation
                // POST to database
                // Toast the returns; update if successful
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        ImageButton color_button = (ImageButton) convertView.findViewById(R.id.group_color);
        Button text_button = (Button) convertView.findViewById(R.id.group_text);

        ((GradientDrawable) color_button.getBackground()).setColor(
                ContextCompat.getColor(mContext,colors[position%num_colors]));
        text_button.setText(group_names[position]);

        // TODO: where do the buttons lead to?
    }

    @Override
    public int getCount() {
        return num_groups;
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
