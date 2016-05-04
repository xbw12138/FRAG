package com.example.frag_model;

/**
 * Created by LeftGod on 16/4/24.
 */
public class MyThing
{
    private int Start_month;
    private int Start_day;
    private int Start_hour;
    private int Start_min;
    private int End_month;
    private int End_day;
    private int End_hour;
    private int End_min;
    private String Description;
    private String Space;
    public MyThing()
    {
        Start_month=0;
        Start_day=0;
        Start_hour=0;
        Start_min=0;
        End_month=0;
        End_day=0;
        End_hour=0;
        End_min=0;
        Description="";
        Space="";
    }
    public void setStart_month(int a){Start_month=a;}
    public void setStart_day(int a){Start_day=a;}
    public void setStart_hour(int a){Start_hour=a;}
    public void setStart_min(int a){Start_min=a;}
    public void setEnd_month(int a){End_month=a;}
    public void setEnd_day(int a){End_day=a;}
    public void setEnd_hour(int a){End_hour=a;}
    public void setEnd_min(int a){End_min=a;}
    public void setDescription(String a){Description=a;}
    public void setSpace(String a){Space=a;}

    public int getStart_month(){return Start_month;}
    public int getStart_day(){return Start_day;}
    public int getStart_hour(){return Start_hour;}
    public int getStart_min(){return Start_min;}
    public int getEnd_month(){return End_month;}
    public int getEnd_day(){return End_day;}
    public int getEnd_hour(){return End_hour;}
    public int getEnd_min(){return End_min;}
    public String getDescription(){return Description;}
    public String getSpace(){return Space;}
}
