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
    private String createAt;
    private String updateAt;

    public CallerModel(String nameContact, String phoneNumber,String createAt,String updateAt) {
        this.nameContact = nameContact;
        this.phoneNumber = phoneNumber;
        this.createAt = createAt;
        this.updateAt = updateAt;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
