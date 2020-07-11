package com.example.projectprogresstracker.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.ACTIVITY_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.ACTIVITY_TABLE_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_DESCRIPTION;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_TASK_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_BUDGET;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_DESCRIPTION;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_END_POINT;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_PROJECT_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_PROJECT_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_START_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_START_POINT;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_DESCRIPTION;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_PROJECT_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_TABLE_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry._ID;

public class ProjectDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "project.db";

    /**
     * create table statements
     */
    String SQL_CREATE_PROJECT_TABLE = "CREATE TABLE " + ProjectContract.ProjectEntry.TABLE_NAME + " ( "
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PROJECT_NAME + " TEXT NOT NULL, "
            + COLUMN_START_DATE + " TEXT, "
            + COLUMN_END_DATE + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_BUDGET + " INTEGER DEFAULT 0, "
            + COLUMN_PROJECT_PROGRESS + " INTEGER DEFAULT 0, "
            + COLUMN_START_POINT + " INTEGER DEFAULT 0, "
            + COLUMN_END_POINT + " INTEGER DEFAULT 100); ";


    String SQL_CREATE_TASK_TABLE = "CREATE TABLE " + TASK_TABLE_NAME + " ( "
            + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TASK_NAME + " TEXT NOT NULL, "
            + COLUMN_TASK_END_DATE + " TEXT, "
            + COLUMN_TASK_DESCRIPTION + " TEXT, "
            + COLUMN_TASK_PROGRESS + " INTEGER DEFAULT 0, "
            + COLUMN_TASK_PROJECT_ID + " INTEGER); ";

    String SQL_CREATE_ACTIVITY_TABLE = "CREATE TABLE " + ACTIVITY_TABLE_NAME + " ( "
            + ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ACTIVITY_NAME + " TEXT NOT NULL, "
            + COLUMN_ACTIVITY_END_DATE + " TEXT, "
            + COLUMN_ACTIVITY_DESCRIPTION + " TEXT, "
            + COLUMN_ACTIVITY_PROGRESS + " INTEGER DEFAULT 0, "
            + COLUMN_ACTIVITY_TASK_ID + " INTEGER); ";

    public ProjectDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public ProjectDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_PROJECT_TABLE);
        db.execSQL(SQL_CREATE_TASK_TABLE);
        db.execSQL(SQL_CREATE_ACTIVITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
