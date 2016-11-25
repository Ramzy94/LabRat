package com.itrw324.mofokeng.labrat.NonActivityClasses;

/**
 * Created by Mofokeng on 08-Nov-16.
 */

public class Class {

    private String classID;
    private int class_Period;
    private String class_Time;
    private String day;
    private String venueID;
    private String module_Code;

    public Class()
    {

    }

    public Class(int class_Period, String venueID, String module_Code,String day) {
        this.setClass_Period(class_Period);
        this.setVenueID(venueID);
        this.setModule_Code(module_Code);
        this.setClass_Time(this.getClass_Period());
        this.setDay(day);
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getClass_Time() {
        return class_Time;
    }

    public void setClass_Time(int Period) {
        switch (Period)
        {
            case 0:
                class_Time ="08:00";break;
            case 1:
                class_Time ="09:30";break;
            case 2:
                class_Time ="11:00";break;
            case 3:
                class_Time ="12:30";break;
            case 4:
                class_Time ="14:00";break;
            case 5:
                class_Time ="15:30";break;
        }
    }

    public int getClass_Period() {
        return class_Period;
    }

    public void setClass_Period(int class_Period) {
        this.class_Period = class_Period;
        this.setClass_Time(this.getClass_Period());
    }

    public int getDaySpinnerValue()
    {
        if(getDay().equalsIgnoreCase("Monday"))
            return 0;
        else if(getDay().equalsIgnoreCase("Tuesday"))
            return 1;
        else if (getDay().equalsIgnoreCase("Wednesday"))
            return 2;
        else if (getDay().equalsIgnoreCase("Thursday"))
            return 3;
        else return 4;
    }

    public String getVenueID() {
        return venueID;
    }

    public void setVenueID(String venueID) {
        this.venueID = venueID;
    }

    public String getModule_Code() {
        return module_Code;
    }

    public void setModule_Code(String module_Code) {
        this.module_Code = module_Code;
    }
}