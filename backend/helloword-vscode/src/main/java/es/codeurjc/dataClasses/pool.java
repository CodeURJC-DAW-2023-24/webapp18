package es.codeurjc.dataClasses;

import java.util.ArrayList;

public class pool {
    public String name;
    public ArrayList<messege> messeges;
    public int scheduleStart;
    public int scheduleEnd;
    public int afor;
    public String description;
    public String pic;

    public pool(String n, int start, int end, int afor, String desc, String pic){
        this.name = n;
        this.afor = afor;
        this.scheduleStart = start;
        this.scheduleEnd = end;
        this.pic = pic;
        this.description = desc;
        this.messeges  = new ArrayList<messege>();
    }
}
