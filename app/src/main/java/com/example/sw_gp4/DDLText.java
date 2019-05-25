package com.example.sw_gp4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DDLText implements Comparable {
    private Date dtime;
    public String ddl_id;
    public String ddl_time;
    public String ddl_title;
    public String ddl_description;
    public String ddl_status;
    public DDLText(String id, String time, String status, String title) {
        ddl_id = id;
        ddl_time = time;
        ddl_title = title;
        ddl_status = status;
        ddl_description = null;
        dtime = strToDate(time);
    }
    public DDLText(String id, String time, String status, String title, String description) {
        ddl_id = id;
        ddl_time = time;
        ddl_title = title;
        ddl_description = description;
        ddl_status = status;
        dtime = strToDate(time);
    }
    public void copyFrom(DDLText ddl) {
        ddl_title = ddl.ddl_title;
        ddl_time = ddl.ddl_time;
        ddl_description = ddl.ddl_description;
        ddl_status = ddl.ddl_status;
        dtime = strToDate(ddl_time);
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
