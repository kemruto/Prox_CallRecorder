package com.sonnguyen.callrecorder.datasource.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName =  "CALLER_MODEL")
public class CallerModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nameContact;
    private String phoneNumber;
    private String date;

    public CallerModel(String nameContact, String phoneNumber,String date) {
        this.nameContact = nameContact;
        this.phoneNumber = phoneNumber;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameContact() {
        return nameContact;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
