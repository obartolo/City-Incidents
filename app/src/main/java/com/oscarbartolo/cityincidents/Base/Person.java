package com.oscarbartolo.cityincidents.Base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Oscar on 03/05/2015.
 */
public class Person {
    private int id;
    private String email;
    private String pass;

    public Person(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}
