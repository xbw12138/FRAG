package com.example.frag_model;

import java.util.Calendar;

/**
 * Created by LeftGod on 16/4/24.
 */
public class MyTime
{
    public static final String[] arr_month=new String[]{
            "一月",
            "二月",
            "三月",
            "四月",
            "五月",
            "六月",
            "七月",
            "八月",
            "九月",
            "十月",
            "十一月",
            "十二月"
    };

    public static final String[] arr_day31=new String[]{
            " 1"," 2"," 3"," 4"," 5"," 6"," 7"," 8"," 9","10","11","12","13","14","15","16",
            "17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"
    };

    public static final String[] arr_day30=new String[]{
            " 1"," 2"," 3"," 4"," 5"," 6"," 7"," 8"," 9","10","11","12","13","14","15","16",
            "17","18","19","20","21","22","23","24","25","26","27","28","29","30"
    };

    public static final String[] arr_day29=new String[]{
            " 1"," 2"," 3"," 4"," 5"," 6"," 7"," 8"," 9","10","11","12","13","14","15","16",
            "17","18","19","20","21","22","23","24","25","26","27","28","29"
    };

    public static final String[] arr_day28=new String[]{
            " 1"," 2"," 3"," 4"," 5"," 6"," 7"," 8"," 9","10","11","12","13","14","15","16",
            "17","18","19","20","21","22","23","24","25","26","27","28"
    };

    public static final String[] arr_hour=new String[]{
            " 0"," 1"," 2"," 3"," 4"," 5"," 6"," 7"," 8"," 9","10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23"
    };

    public static final String[] arr_min=new String[]{
            " 0"," 1"," 2"," 3"," 4"," 5"," 6"," 7"," 8"," 9","10","11","12","13","14","15",
            "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31",
            "32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47",
            "48","49","50","51","52","53","54","55","56","57",
            "58","59"
    };

    public static boolean Is_laep()
    {
        int year=getSystemTimeYear();
        if(year%4==0&&year%100!=0||year%400==0) return true;
        else return false;
    }
    public static String[] Judge_Month(String mm)
    {
        if(mm.equals("一月")||mm.equals("三月")||mm.equals("五月")||mm.equals("七月")||mm.equals("八月")||mm.equals("十月")||mm.equals("十二月"))
        {
            return arr_day31;
        }
        else if(mm.equals("四月")||mm.equals("六月")||mm.equals("九月")||mm.equals("十一月"))
        {
            return arr_day30;
        }
        else if(mm.equals("二月")&&Is_laep()==true)
        {
            return arr_day29;
        }
        else if(mm.equals("二月")&&Is_laep()==false)
        {
            return arr_day28;
        }
        String[] wrong={"0"};
        return wrong;
    }

    public static String[] Judge_Month(int mm)
    {
        if(mm==1||mm==3||mm==5||mm==7||mm==8||mm==10||mm==12)
        {
            return arr_day31;
        }
        else if(mm==4||mm==6||mm==9||mm==11)
        {
            return arr_day30;
        }
        else if(mm==2&&Is_laep()==true)
        {
            return arr_day29;
        }
        else if(mm==2&&Is_laep()==false)
        {
            return arr_day28;
        }
        String[] wrong={"0"};
        return wrong;
    }

    public static int getSystemTimeYear()//获取当前系统时间年份
    {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return year;
    }

    public static int getSystemTimeMonth()
    {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        return month;
    }

    public static int getSystemTimeDay()
    {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public static int getSystemTimeHour()
    {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    public static int getSystemTimeMin()//获取当前系统时间年份
    {
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        return minute;
    }
}
