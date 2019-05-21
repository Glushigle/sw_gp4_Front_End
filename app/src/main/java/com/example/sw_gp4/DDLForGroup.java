package com.example.sw_gp4;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DDLForGroup implements Comparable {
    public String time;
    public String title;
    public String description;
    public DDLForGroup(String tm, String tt) {
        time = tm;
        title = tt;
        description = null;
    }
    public DDLForGroup(String tm, String tt, String d) {
        time = tm;
        title = tt;
        description = d;
    }
    public int compareTo(Object o) {
        DDLForGroup tmp = (DDLForGroup)o;
        return this.time.compareTo(tmp.time);
    }
}
