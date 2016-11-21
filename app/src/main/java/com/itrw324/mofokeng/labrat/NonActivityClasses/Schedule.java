package com.itrw324.mofokeng.labrat.NonActivityClasses;

/**
 * Created by Mofokeng on 04-Nov-16.
 */

public class Schedule {

    private int scheduleID;
    private String userEmail;
    private int classID;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getScheduleID() {

        return scheduleID;
    }

    public void setScheduleID(int scheduleID) {
        this.scheduleID = scheduleID;
    }

    public Schedule(int scheduleID, String userEmail, int classID) {

        this.scheduleID = scheduleID;
        this.userEmail = userEmail;
        this.classID = classID;
    }
}
