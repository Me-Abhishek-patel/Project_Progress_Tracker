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
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprogresstracker.data.ProjectDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    ListView taskListView;
    ArrayList<TaskModel> taskArrayList;
    TaskAdapter mTaskAdapter;
    TextView tvTaskProgress, tvTaskName, tvTaskEndDate, tvTaskDesc, tvDaysLeft;
    SimpleDateFormat sdf, inputFormat;
    CalcHelper calcHelper;
    FloatingActionButton fabAddActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        fabAddActivity = findViewById(R.id.fab_add_activity);
        projectDbHelper = new ProjectDbHelper(this);
        writableTaskDb = projectDbHelper.getWritableDatabase();
        readableTaskDb = projectDbHelper.getReadableDatabase();
        seekBar = findViewById(R.id.sb_task_details);
        calcHelper = new CalcHelper();

        tvTaskName = findViewById(R.id.tv_task_name_detail);
        tvTaskEndDate = findViewById(R.id.edt_task_end_date);
        tvTaskProgress = findViewById(R.id.tv_task_progress_detail);
        tvTaskDesc = findViewById(R.id.edt_task_description_detail);
        tvDaysLeft = findViewById(R.id.tv_task_days_left_detail);
        taskListView = findViewById(R.id.activity_listView);
        taskArrayList = new ArrayList<>();
        mTaskAdapter = new TaskAdapter(this, taskArrayList);
        taskListView.setAdapter(mTaskAdapter);


/**
 * handling fab for adding task
 */
        fabAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TaskDetails.this);
                LayoutInflater inflater = (LayoutInflater) TaskDetails.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.add_activity_dialog, null);
                builder.setView(view);


                final Dialog dialogAddProject = builder.show();
                Button btnCancel = dialogAddProject.findViewById(R.id.btnCancel_task);
                Button btnCreate = dialogAddProject.findViewById(R.id.btn_add_task);
                final EditText edtAddTaskName = dialogAddProject.findViewById(R.id.edt_add_task_name);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogAddProject.dismiss();
                    }
                });
                btnCreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), edtAddTaskName.getText().toString(), Toast.LENGTH_SHORT).show();
                        taskArrayList.add(new TaskModel(1, edtAddTaskName.getText().toString(), 35));
                        mTaskAdapter.notifyDataSetChanged();
//                        addTask(edtAddTaskName.getText().toString());
                        dialogAddProject.dismiss();
                    }
                });
            }
        });


        /**
         * handling Seekbar
         */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int prog;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTaskProgress.setText(progress + "%");
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

            tvDaysLeft.setText("Days Left : " + calcHelper.getDaysLeft(mTaskEndDate));
            seekBar.setProgress(Integer.parseInt(mTaskProgress));

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
     * method  to update progress
     */
    public void updateProgress(int progress) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_PROGRESS, progress);
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


    /**
     * back botton
     */
    public void back(View view) {
        finish();
    }


}