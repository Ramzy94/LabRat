package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserAccount   {

    public static final int LECTURER = 1;
    public static final int STUDENT = 0;

    //Instance Variables
    private GoogleSignInAccount account;
    private int role;
    private String university_Number;


    //
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

    public void setRole(int role) {
        this.role = role;
    }

}
