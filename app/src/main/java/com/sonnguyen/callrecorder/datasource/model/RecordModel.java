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

    private String phoneNumber;
    private int status;
    private String date;
    private String time;
    private int favourite;
    private int trimmed;
    private String path;
    private String note;

    public RecordModel(String phoneNumber, int status, String date,String time, int favourite, int trimmed,String path,String note) {
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.date = date;
        this.time = time;
        this.favourite = favourite;
        this.trimmed = trimmed;
        this.path = path;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
