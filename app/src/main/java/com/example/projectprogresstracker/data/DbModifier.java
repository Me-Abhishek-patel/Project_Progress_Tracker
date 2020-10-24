package com.example.projectprogresstracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.ACTIVITY_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.ACTIVITY_TABLE_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_TABLE_NAME;

public class DbModifier {
    Context context;
    SQLiteDatabase writableProjectDb, readableProjectDb;


    public DbModifier(Context context) {
        this.context = context;
        ProjectDbHelper projectDbHelper = new ProjectDbHelper(context);
        writableProjectDb = projectDbHelper.getWritableDatabase();
    }

    //    writableProjectDb = projectDbHelper.getWritableDatabase();
//    readableProjectDb = projectDbHelper.getReadableDatabase();
    public void deleteTask(int id) {

        // Define 'where' part of query.
        String selection = TASK_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};


        int deletedRows = writableProjectDb.delete(TASK_TABLE_NAME, selection, selectionArgs);


    }

    public void setTaskProgress(int mId, int progress) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_PROGRESS, progress);
        // Which row to update, based on the title
        String selection = TASK_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(mId)};
        int count = writableProjectDb.update(
                TASK_TABLE_NAME,
                values,
                selection,
                selectionArgs);


    }

    public void setActivityProgress(int mId, int progress) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_PROGRESS, progress);
        // Which row to update, based on the title
        String selection = ACTIVITY_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(mId)};
        int count = writableProjectDb.update(
                ACTIVITY_TABLE_NAME,
                values,
                selection,
                selectionArgs);


    }
}
