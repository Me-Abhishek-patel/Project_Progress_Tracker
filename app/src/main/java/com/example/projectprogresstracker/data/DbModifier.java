package com.example.projectprogresstracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_TABLE_NAME;

public class DbModifier {
    Context context;
    SQLiteDatabase writableProjectDb, readableProjectDb;


    public DbModifier(Context context) {
        this.context = context;
    }

    //    writableProjectDb = projectDbHelper.getWritableDatabase();
//    readableProjectDb = projectDbHelper.getReadableDatabase();
    public void deleteTask(int id) {
        ProjectDbHelper projectDbHelper = new ProjectDbHelper(context);
        writableProjectDb = projectDbHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = TASK_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};


        int deletedRows = writableProjectDb.delete(TASK_TABLE_NAME, selection, selectionArgs);


    }
}
