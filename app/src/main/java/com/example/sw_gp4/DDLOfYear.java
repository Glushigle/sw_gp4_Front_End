package com.example.sw_gp4;

import java.util.List;

public class DDLOfYear implements Comparable {
    public int year;
    public List<DDLOfMonth> data;
    public DDLOfYear(int y, List<DDLOfMonth> d) {
        year = y;
        data = d;
    }
    public int compareTo(Object o) {
        DDLOfYear tmp = (DDLOfYear)o;
        return Integer.compare(this.year,tmp.year);
    }
}
