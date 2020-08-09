package com.example.projectprogresstracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprogresstracker.data.ProjectDbHelper;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.ACTIVITY_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.ACTIVITY_TABLE_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_DESCRIPTION;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_PROGRESS;

public class Activity_details extends AppCompatActivity {
    int mId;
    SQLiteDatabase writableTaskDb, readableTaskDb;
    ProjectDbHelper projectDbHelper;
    SeekBar seekBar;
    TextView tvActivityName, tvActivityProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        projectDbHelper = new ProjectDbHelper(this);
        writableTaskDb = projectDbHelper.getWritableDatabase();
        readableTaskDb = projectDbHelper.getReadableDatabase();
        tvActivityName = findViewById(R.id.tv_activity_name_detail);
        tvActivityProgress = findViewById(R.id.tv_activity_progress_detail);
        seekBar = findViewById(R.id.sb_activity_details);
        /**
         * retriving intents extras
         */
        Bundle extras = getIntent().getExtras();
        mId = extras.getInt("mId");
        queryActivity();
    }

    /**
     * query task and update UI
     */
    public void queryActivity() {
        String[] projection = {
                COLUMN_ACTIVITY_NAME, COLUMN_ACTIVITY_DESCRIPTION, COLUMN_ACTIVITY_END_DATE, COLUMN_ACTIVITY_PROGRESS
        };

        String selection = ACTIVITY_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(mId)};

        Cursor cursor = readableTaskDb.query(
                ACTIVITY_TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        if (cursor.getCount() > 0)
            Toast.makeText(getApplicationContext(), "cursor length " + cursor.getCount(), Toast.LENGTH_SHORT).show();

        if (cursor != null && cursor.moveToFirst()) {


            String mTaskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_NAME));
            String mTaskDesc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_DESCRIPTION));
            String mTaskEndDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_END_DATE));
            String mTaskProgress = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_PROGRESS));

            tvActivityName.setText(mTaskName);
//            tvTaskDesc.setText(mTaskDesc);
//            tvTaskEndDate.setText(mTaskEndDate);
            tvActivityProgress.setText(mTaskProgress + "%");

//            tvDaysLeft.setText("Days Left : " + calcHelper.getDaysLeft(mTaskEndDate));
            seekBar.setProgress(Integer.parseInt(mTaskProgress));

            cursor.close();
        }

        /**
         * handling Seekbar
         */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int prog;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvActivityProgress.setText(progress + "%");
                prog = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateProgress(prog);

            }
        });


    }

    /**
     * method  to update progress
     */
    public void updateProgress(int progress) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_PROGRESS, progress);
        // Which row to update, based on the title
        String selection = ACTIVITY_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(mId)};
        int count = writableTaskDb.update(
                ACTIVITY_TABLE_NAME,
                values,
                selection,
                selectionArgs);

        queryActivity();
    }

    /**
     * back botton
     */
    public void back(View view) {
        finish();
    }
}

