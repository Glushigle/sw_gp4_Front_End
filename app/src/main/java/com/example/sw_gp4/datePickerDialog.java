package com.example.sw_gp4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

import java.lang.reflect.Field;

public class datePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener {
    private DatePicker datepicker;
    private dateSetListener setlistener;
    public datePickerDialog(Context context, int theme, dateSetListener listener, int year, int month) {
        super(context,theme);
        Log.d("picker","datePickerDialog");
        setlistener = listener;
        setButton(BUTTON_POSITIVE,"确定",this);
        setButton(BUTTON_NEGATIVE,"取消",this);
        setIcon(0);
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_picker_dialog,null);
        setView(view);
        datepicker = view.findViewById(R.id.datepicker);
        datepicker.init(year,month,1,this);
        hideDay(datepicker);
    }
    private void hideDay(DatePicker dp) {
        Log.d("picker","hideDay");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day","id","android");
                if (daySpinnerId != 0) {
                    View daySpinner = dp.findViewById(daySpinnerId);
                    if (daySpinner != null)
                        daySpinner.setVisibility(View.GONE);
                }
            }
            else {
                Field[] datePickerFields = dp.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || "mDayPicker".equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(dp);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View)dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onClick(DialogInterface dialog, int which) {
        Log.d("picker","onClick");
        if (which == BUTTON_POSITIVE)
            notifyDateSet();
    }
    private void notifyDateSet() {
        Log.d("picker","notifyDateSet");
        if (setlistener != null) {
            datepicker.clearFocus();
            setlistener.onDateSet(datepicker,datepicker.getYear(),datepicker.getMonth());
        }
    }
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        Log.d("picker","onDateChanged");
        if (view.getId() == R.id.datepicker)
            datepicker.init(year,month,day,this);
    }
    public DatePicker getDatepicker() {
        Log.d("picker","getDatePicker");
        return datepicker;
    }
    public void updateStartDate(int year, int month) {
        Log.d("picker","updateStartDate");
        datepicker.updateDate(year,month,1);
    }
    protected void onStop() {
        super.onStop();
        Log.d("picker","onStop");
    }
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        Log.d("picker","onSave");
        state.putInt("startYear",datepicker.getYear());
        state.putInt("startMonth",datepicker.getMonth());
        //
        return state;
    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("picker","onRestore");
        int year = savedInstanceState.getInt("startYear");
        int month = savedInstanceState.getInt("startMonth");
        //
        datepicker.init(year,month,1,this);
    }
    public interface dateSetListener {
        void onDateSet(DatePicker dp, int year, int month);
    }
}
