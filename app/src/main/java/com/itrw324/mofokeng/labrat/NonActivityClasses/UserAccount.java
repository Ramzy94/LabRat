package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserAccount   {

    public static final int LECTURER = 1;
    public static final int STUDENT = 0;

    public static final String LECTURER_ROLE = "Student";
    public static final String STUDENT_ROLE = "Student";

    //Instance Variables
    private GoogleSignInAccount account;
    private int role;
    private String university_Number;

    public UserAccount(GoogleSignInAccount account, String university_Number) {
        setAccount(account);
        setUniversity_Number(university_Number);
    }

    public boolean isAStudent()
    {
        if(this.role==LECTURER)
            return false;
        else return true;
    }

    public String getUniversity_Number() {
        return university_Number;
    }

    public void setUniversity_Number(String university_Number) {
        this.university_Number = university_Number;
    }

    public GoogleSignInAccount getAccount() {
        return account;
    }

    public void setAccount(GoogleSignInAccount account) {
        this.account = account;
    }

    public String getRole() {
        if(this.role == 0)
            return "Student";
        else
            return "Lecturer";
    }

    public void setRole(String role) {
        if (role.equalsIgnoreCase(STUDENT_ROLE))
            setRole(STUDENT);
        else
            setRole(LECTURER);
    }

    public void setRole(int role) {
        this.role = role;
    }

}
