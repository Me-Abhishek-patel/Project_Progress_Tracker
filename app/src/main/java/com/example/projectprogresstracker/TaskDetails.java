package com.example.projectprogresstracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
import com.skydoves.progressview.ProgressView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.ACTIVITY_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.ACTIVITY_TABLE_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_ACTIVITY_TASK_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_DESCRIPTION;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_TABLE_NAME;

public class TaskDetails extends AppCompatActivity implements AdapterView.OnItemClickListener {

    int mId;
    SQLiteDatabase writableTaskDb, readableTaskDb;
    ProjectDbHelper projectDbHelper;
    SeekBar seekBar;
    ListView taskListView;
    ArrayList<TaskModel> activityArrayList;
    TaskAdapter mTaskAdapter;
    TextView tvTaskProgress, tvTaskName, tvTaskEndDate, tvTaskDesc, tvDaysLeft;
    SimpleDateFormat sdf, inputFormat;
    CalcHelper calcHelper;
    Calendar calender;
    FloatingActionButton fabAddActivity;
    ProgressView taskProgressView;

    @Override
    protected void onPostResume() {
        queryAllActivity();
        queryTask();
        super.onPostResume();
    }

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
        calender = Calendar.getInstance();
        taskProgressView = findViewById(R.id.pv_task_progress_detail);
        tvTaskName = findViewById(R.id.tv_task_name_detail);
        tvTaskEndDate = findViewById(R.id.edt_task_end_date);
        tvTaskProgress = findViewById(R.id.tv_task_progress_detail);
        tvTaskDesc = findViewById(R.id.edt_task_description_detail);
        tvDaysLeft = findViewById(R.id.tv_task_days_left_detail);
        taskListView = findViewById(R.id.activity_listView);
        activityArrayList = new ArrayList<>();
        taskListView.setOnItemClickListener(this);
        mTaskAdapter = new TaskAdapter(this, activityArrayList);
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
                        activityArrayList.add(new TaskModel(1, edtAddTaskName.getText().toString(), 35));
                        mTaskAdapter.notifyDataSetChanged();
                        addActivity(edtAddTaskName.getText().toString());
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
        queryAllActivity();
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
            taskProgressView.setProgress(Integer.parseInt(mTaskProgress));

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

    public void addActivity(String activityName) {
        int mStartYear = calender.get(Calendar.YEAR);
        int mStartMonth = calender.get(Calendar.MONTH) + 1;
        int mStartDay = calender.get(Calendar.DAY_OF_MONTH);

        String mStartDate = mStartYear + "-" + mStartMonth + "-" + mStartDay;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ACTIVITY_NAME, activityName);
        cv.put(COLUMN_ACTIVITY_END_DATE, mStartDate);
        cv.put(COLUMN_ACTIVITY_TASK_ID, mId);

        if (writableTaskDb.insert(ACTIVITY_TABLE_NAME, null, cv) == -1) {
            Toast.makeText(getApplicationContext(), "activity can not be added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), activityName + " Added", Toast.LENGTH_SHORT).show();
        }
        queryAllActivity();

    }

    /**
     * Query all project
     */
    public void queryAllActivity() {
        String[] projection = {
                COLUMN_ACTIVITY_NAME, ACTIVITY_ID, COLUMN_ACTIVITY_TASK_ID, COLUMN_ACTIVITY_PROGRESS
        };
        String selection = COLUMN_ACTIVITY_TASK_ID + " LIKE ?";
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
        activityArrayList.clear();
        int sumProgress = 0;
        if (cursor.getCount() <= 0) {
            seekBar.setVisibility(View.VISIBLE);
            taskProgressView.setVisibility(View.GONE);
        } else {
            seekBar.setVisibility(View.GONE);
            taskProgressView.setVisibility(View.VISIBLE);
        }

        while (cursor.moveToNext()) {

            String taskProgres = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_PROGRESS));
            sumProgress += Integer.parseInt(taskProgres);
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ACTIVITY_ID));
            String taskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACTIVITY_NAME));
            activityArrayList.add(new TaskModel(id, taskName, Integer.parseInt(taskProgres)));

        }
        if (cursor.getCount() != 0)
            updateProgress(sumProgress / cursor.getCount());


        mTaskAdapter.notifyDataSetChanged();
        cursor.close();

    }

    /**
     * back botton
     */
    public void back(View view) {
        finish();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int mId = activityArrayList.get(position).getmTaskId();
        Toast.makeText(getApplicationContext(), "" + mId, Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(TaskDetails.this, Activity_details.class);
        myIntent.putExtra("mId", mId); //Optional parameters
        TaskDetails.this.startActivity(myIntent);
    }

}