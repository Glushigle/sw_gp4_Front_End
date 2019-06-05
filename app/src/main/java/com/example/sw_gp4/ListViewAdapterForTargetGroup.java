package com.example.sw_gp4;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListViewAdapterForTargetGroup extends BaseSwipeAdapter {

    private Context mContext;

    private int num_colors = 5;
    private int colors[] = {R.color.gp_1, R.color.gp_2, R.color.gp_3, R.color.gp_4, R.color.gp_5};

    //private ArrayList<String> group_names;

    public ListViewAdapterForTargetGroup(Context mContext
                                         //,ArrayList<String> group_names
    ) {
        this.mContext = mContext;
        //this.group_names = group_names;
    }

    public void resetData( ArrayList<String> group_names){
        //this.group_names = group_names;
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
                ((TargetGroup) mContext).OnDeleteClicked(view, position);
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        ImageButton color_button = (ImageButton) convertView.findViewById(R.id.group_color);
        TextView text_button = (TextView) convertView.findViewById(R.id.group_name);

        String username = TargetGroup.userNames.get(position);
        ((GradientDrawable) color_button.getBackground()).setColor(ColorConverter.fromName(username));
        text_button.setText(username);
    }

    @Override
    public int getCount() {
        return TargetGroup.userNames.size();
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
