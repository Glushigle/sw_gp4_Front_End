package com.example.sw_gp4;

import java.util.List;

public class DDLOfDay implements Comparable {
    private int iday;
    public String day;
    public String week;
    public List<DDLText> data;
    public DDLOfDay(String dy, String wk, List<DDLText> dt) {
        day = dy;
        week = wk;
        data = dt;
        iday = Integer.parseInt(dy);
    }
    public int compareTo(Object o) {
        DDLOfDay tmp = (DDLOfDay) o;
        return Integer.compare(this.iday,tmp.iday);
        /*if (this.iday > tmp.iday)
            return 1;
        else if (this.iday < tmp.iday)
            return -1;
        else
            return 0;*/
    }
}
