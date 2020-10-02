package com.example.projectprogresstracker.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.projectprogresstracker.Entity.Project;
import com.example.projectprogresstracker.Entity.Task;
import com.example.projectprogresstracker.TypeConverter.Converter;

@Database(entities = {Project.class, Task.class}, version = 1)
@TypeConverters(Converter.class)
public abstract class ProjectDataBase extends RoomDatabase {
    private static ProjectDataBase instance;
    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();

    private RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        }
    };

    public static synchronized ProjectDataBase getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(context, ProjectDataBase.class, "Project_DataBase")
            .fallbackToDestructiveMigration()
            .build();
        }
        return instance;
    }




}
