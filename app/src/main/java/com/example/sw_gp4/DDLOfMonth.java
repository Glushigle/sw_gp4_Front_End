package com.example.sw_gp4;

import java.util.List;

public class DDLOfMonth implements Comparable {
    public int year;
    public int month;
    public List<DDLOfDay> data;
    public DDLOfMonth(int y, int m, List<DDLOfDay> d) {
        year = y;
        month = m;
        data = d;
    }
    public int compareTo(Object o) {
        DDLOfMonth tmp = (DDLOfMonth) o;
        int r = Integer.compare(this.year,tmp.year);
        if (r == 0)
            return Integer.compare(this.month,tmp.month);
        else
            return r;
        /*if (this.year < tmp.year)
            return -1;
        else if (this.year > tmp.year)
            return 1;
        else {
            if (this.month < tmp.month)
                return -1;
            else if (this.month > tmp.month)
                return 1;
            else
                return 0;
        }*/
    }
}
