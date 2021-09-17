package com.sonnguyen.callrecorder.datasource.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sonnguyen.callrecorder.datasource.model.CallerModel;
import com.sonnguyen.callrecorder.datasource.model.RecordModel;

@Database(entities = {RecordModel.class, CallerModel.class},version = 2)
public abstract class RecordDatabase extends RoomDatabase {
    private static RecordDatabase instance;

    public static RecordDatabase getInstance(Context context) {
        if(instance==null){
            instance = Room.databaseBuilder(context, RecordDatabase.class, "RECORD_DATABASE")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract RecordDAO recordDAO();

}
