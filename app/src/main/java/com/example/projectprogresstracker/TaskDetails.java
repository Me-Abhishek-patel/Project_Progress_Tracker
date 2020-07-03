package com.example.projectprogresstracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprogresstracker.data.ProjectDbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_DESCRIPTION;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_TABLE_NAME;

public class TaskDetails extends AppCompatActivity {

    int mId;
    SQLiteDatabase writableTaskDb, readableTaskDb;
    ProjectDbHelper projectDbHelper;
    SeekBar seekBar;
    TextView tvTaskProgress, tvTaskName, tvTaskEndDate, tvTaskDesc, tvDaysLeft;
    SimpleDateFormat sdf, inputFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        projectDbHelper = new ProjectDbHelper(this);
        writableTaskDb = projectDbHelper.getWritableDatabase();
        readableTaskDb = projectDbHelper.getReadableDatabase();
        seekBar = findViewById(R.id.sb_task_details);

        tvTaskName = findViewById(R.id.tv_task_name_detail);
        tvTaskEndDate = findViewById(R.id.edt_task_end_date);
        tvTaskProgress = findViewById(R.id.tv_task_progress_detail);
        tvTaskDesc = findViewById(R.id.edt_task_description_detail);
        tvDaysLeft = findViewById(R.id.tv_task_days_left_detail);

        sdf = new SimpleDateFormat("yyyy-MM-dd");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd");


        /**
         * handling Seekbar
         */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTaskProgress.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        /**
         * update desc
         */
        tvTaskDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDesc();
            }
        });

        /**
         * end date update
         */
        tvTaskEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mEndYear = c.get(Calendar.YEAR);
                int mEndMonth = c.get(Calendar.MONTH);
                int mEndDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(TaskDetails.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String date = null;
                                try {
                                    date = sdf.format(inputFormat.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                ContentValues values = new ContentValues();
                                values.put(COLUMN_TASK_END_DATE, date);
                                // Which row to update, based on the title
                                String selection = TASK_ID + " LIKE ?";
                                String[] selectionArgs = {String.valueOf(mId)};
                                int count = writableTaskDb.update(
                                        TASK_TABLE_NAME,
                                        values,
                                        selection,
                                        selectionArgs);

                                queryTask();


                            }
                        }, mEndYear, mEndMonth, mEndDay);
                datePickerDialog.show();
            }
        });

        /**
         * retriving intents extras
         */
        Bundle extras = getIntent().getExtras();
        mId = extras.getInt("mId");

        queryTask();
    }


    /**
     * query task and update UI
     */
    public void queryTask() {
        String[] projection = {
                COLUMN_TASK_NAME, COLUMN_TASK_DESCRIPTION, COLUMN_TASK_END_DATE, COLUMN_TASK_PROGRESS
        };

        String selection = TASK_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(mId)};

        Cursor cursor = readableTaskDb.query(
                TASK_TABLE_NAME,   // The table to query
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


            String mTaskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME));
            String mTaskDesc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION));
            String mTaskEndDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_END_DATE));
            String mTaskProgress = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_PROGRESS));

            tvTaskName.setText(mTaskName);
            tvTaskDesc.setText(mTaskDesc);
            tvTaskEndDate.setText(mTaskEndDate);
            tvTaskProgress.setText(mTaskProgress + "%");

            tvDaysLeft.setText("Days Left : " + getDaysLeft(mTaskEndDate));

            cursor.close();
        }


    }


    /**
     * description update
     */
    public void updateDesc() {
        String currentDesc = tvTaskDesc.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(TaskDetails.this);
        LayoutInflater inflater = (LayoutInflater) TaskDetails.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.update_description_dialog, null);
        builder.setView(view);


        final Dialog dialogAddProject = builder.show();
        Button btnCancel = dialogAddProject.findViewById(R.id.btnCancelDesc);
        Button btnCreate = dialogAddProject.findViewById(R.id.btnUpdateDesc);
        final EditText edtUpdatetDesc = dialogAddProject.findViewById(R.id.edt_update_desc);
        edtUpdatetDesc.setText(currentDesc);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddProject.dismiss();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = edtUpdatetDesc.getText().toString();

                ContentValues values = new ContentValues();
                values.put(COLUMN_TASK_DESCRIPTION, desc);
                // Which row to update, based on the title
                String selection = TASK_ID + " LIKE ?";
                String[] selectionArgs = {String.valueOf(mId)};
                int count = writableTaskDb.update(
                        TASK_TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                queryTask();
                dialogAddProject.dismiss();

            }
        });


    }

    /**
     * method to calculate daysleft
     */
    public String getDaysLeft(String dateTill) {
        final Calendar c = Calendar.getInstance();
        int mStartYear = c.get(Calendar.YEAR);
        int mStartMonth = c.get(Calendar.MONTH) + 1;
        int mStartDay = c.get(Calendar.DAY_OF_MONTH);


        String mDate = mStartYear + "-" + mStartMonth + "-" + mStartDay;
        Date mdate = null;
        try {
            mdate = inputFormat.parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        try {
            date = inputFormat.parse(dateTill);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int diffInDays = (int) ((date.getTime() - mdate.getTime()) / (1000 * 60 * 60 * 24));

        return String.valueOf(diffInDays);
    }

    /**
     * back botton
     */
    public void back(View view) {
        finish();
    }
}