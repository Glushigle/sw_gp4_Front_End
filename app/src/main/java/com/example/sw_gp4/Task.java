package com.example.sw_gp4;

public class Task {
    public String id;
    public String title;
    public int[] finish_time; //{2019,5,20,13,0,0}
    public boolean status;
    public String info;
    public int color;
    public Task(String id, String  title, String finish_time, boolean status, String info){
        this.id = id;
        this.title = title;
        this.finish_time = timeParser(finish_time);
        this.status = status;
        this.info = info;
        this.color = ColorConverter.fromId(id);
    }
    private int[] timeParser(String finish_time){
        String [] dt = finish_time.split(" ");  //"2019-05-20 13:00:00"
        String[] d = dt[0].split("-");          //"2019-05-20"
        String[] t = dt[1].split(":");          //"13:00:00"
        int[] rtn = {Integer.valueOf(d[0]),Integer.valueOf(d[1]),Integer.valueOf(d[2]),
                Integer.valueOf(t[0]),Integer.valueOf(t[1]),Integer.valueOf(t[2])};
        return rtn;
    }
}
