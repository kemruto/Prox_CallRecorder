package com.sonnguyen.callrecorder.datasource.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sonnguyen.callrecorder.datasource.model.CallerModel;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

import java.util.List;

@Dao
public interface RecordDAO {
    @Insert
    public void insertRecord(RecordModel recordModel);

    @Query("Select *From RECORD_MODEL ORDER BY id DESC")
    public List<RecordModel> listRecord();

    @Update
    public void updateRecord(RecordModel recordModel);

    @Delete
    public void deleteRecord(RecordModel recordModel);

    @Query("Select * FROM RECORD_MODEL WHERE phoneNumber = :phoneNumber")
    public List<RecordModel> searchPhone(String phoneNumber);

    @Query("Select *From "+"RECORD_MODEL"+" Where status = 1 ORDER BY id DESC ")
    public List<RecordModel> listIncomingCall ();

    @Query("Select *From "+"RECORD_MODEL"+" Where status = 0 ORDER BY id DESC")
    public List<RecordModel> listOutgoingCall ();

    @Query("Select *From "+"RECORD_MODEL"+" Where trimmed = 1  ORDER BY id DESC")
    public List<RecordModel> listTrimmedRecord ();

    @Query("Select *From "+"RECORD_MODEL"+" Where favourite = 1 ORDER BY id DESC")
    public List<RecordModel> listFavourite ();


    /** Caller Model */
    @Query("Select *From CALLER_MODEL ORDER BY id DESC")
    public List<CallerModel> listCaller();

    @Insert
    public void insertCaller(CallerModel callerModel);

    @Delete
    public void deleteCaller(List<CallerModel> listCallerModel);
 
}
