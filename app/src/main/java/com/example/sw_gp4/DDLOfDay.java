package com.example.sw_gp4;

import java.util.ArrayList;
import java.util.List;

public class DDLOfDay {
    public String day;
    public String week;
    public List<DDLText> data;
    public DDLOfDay(String dy, String wk) {
        day = dy;
        week = wk;
        data = new ArrayList<>();
    }
}
