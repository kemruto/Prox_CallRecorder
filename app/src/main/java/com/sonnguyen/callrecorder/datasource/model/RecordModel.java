package com.sonnguyen.callrecorder.datasource.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/*
* Lưu thông tin của 1 bản ghi âm sau 1 cuộc gọi
* */
@Entity(tableName = "RECORD_MODEL")
public class RecordModel implements Serializable {
    @PrimaryKey (autoGenerate = true)
    private int id;

    private String phoneContact;
    private String phoneNumber;
    private int status;
    private String date;
    private int favourite;
    private int trimmed;
    private String path;
    private String note;
    private String createAt;
    private String updateAt;

    public RecordModel(String phoneContact,String phoneNumber, int status, String date, int favourite, int trimmed,String path,String note,String createAt,String updateAt) {
        this.phoneContact = phoneContact;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.date = date;
        this.favourite = favourite;
        this.trimmed = trimmed;
        this.path = path;
        this.note = note;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneContact() {
        return phoneContact;
    }

    public void setPhoneContact(String phoneContact) {
        this.phoneContact = phoneContact;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getTrimmed() {
        return trimmed;
    }

    public void setTrimmed(int trimmed) {
        this.trimmed = trimmed;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
