package com.example.sw_gp4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DDLText implements Comparable {
    private Date dtime;
    public String time;
    public String title;
    public String description;
    public DDLText(String tm, String tt) {
        time = tm;
        title = tt;
        description = null;
        dtime = strToDate(tm);
    }
    public DDLText(String tm, String tt, String d) {
        time = tm;
        title = tt;
        description = d;
        dtime = strToDate(tm);
    }
    private Date strToDate(String t) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            return format.parse(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public int compareTo(Object o) {
        DDLText tmp = (DDLText)o;
        return this.dtime.compareTo(tmp.dtime);
    }
}
