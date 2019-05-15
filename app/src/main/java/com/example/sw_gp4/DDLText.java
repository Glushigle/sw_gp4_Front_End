package com.example.sw_gp4;

public class DDLText {
    public String title;
    public String description;
    public DDLText(String t) {
        title = t;
        description = null;
    }
    public DDLText(String t, String d) {
        title = t;
        description = d;
    }
}
