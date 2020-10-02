package com.example.projectprogresstracker.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.projectprogresstracker.Entity.Project;
import com.example.projectprogresstracker.Entity.Task;

@Database(entities = {Task.class, Project.class}, version = 1)
public abstract class TaskDataBase extends RoomDatabase {
    private static TaskDataBase taskDataBase;

    public static synchronized TaskDataBase getTaskDataBase(Context context){
        if(taskDataBase == null){
            taskDataBase = Room.databaseBuilder(context, TaskDataBase.class, "Task_Database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return taskDataBase;
    }

    public abstract TaskDao taskDao();
}
