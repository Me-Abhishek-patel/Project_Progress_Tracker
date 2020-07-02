package com.example.projectprogresstracker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprogresstracker.data.ProjectContract;
import com.example.projectprogresstracker.data.ProjectDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.expandablelayout.ExpandableLayout;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_DESCRIPTION;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_PROJECT_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_PROJECT_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_START_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_TASK_PROJECT_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TABLE_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_ID;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TASK_TABLE_NAME;

public class ProjectDetails extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ExpandableLayout expandablelayout2;
    ImageView expandCollapseArrow2;
    TextView edtStartDate, edtEndDate, tvProjectName, tvDaysLeft, tvProjectTarget, tvProjectDescription;
    int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    SQLiteDatabase writableProjectDb, readableProjectDb;
    ProjectDbHelper projectDbHelper;
    int mId;
    SmoothBottomBar filterSmoothBottomBar;
    ListView taskListView;
    ArrayList<TaskModel> taskArrayList;
    TaskAdapter mTaskAdapter;
    SimpleDateFormat sdf, inputFormat;
    FloatingActionButton fabAddTask;
    String mProjectName, mProjectDescription, mProjectStartDate, mProjectEndDate;
    Calendar calender;

    @Override
    protected void onPostResume() {
        queryAllTasks();
        queryProject();
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        fabAddTask = findViewById(R.id.fab_add_task);
        tvProjectName = findViewById(R.id.tv_project_name_detail);
        edtEndDate = findViewById(R.id.edt_end_date);
        edtStartDate = findViewById(R.id.edt_start_date);
        expandablelayout2 = findViewById(R.id.expandable2);
        expandCollapseArrow2 = findViewById(R.id.expand_collapse_arrow2);
        projectDbHelper = new ProjectDbHelper(this);
        writableProjectDb = projectDbHelper.getWritableDatabase();
        readableProjectDb = projectDbHelper.getReadableDatabase();
        tvDaysLeft = findViewById(R.id.tv_project_days_left_detail);
        tvProjectTarget = findViewById(R.id.tv_project_target_detail);
        tvProjectDescription = findViewById(R.id.edt_project_description_detail);
        filterSmoothBottomBar = findViewById(R.id.project_detail_filterAppbar);
        taskListView = findViewById(R.id.task_listView);
        taskArrayList = new ArrayList<>();
        mTaskAdapter = new TaskAdapter(this, taskArrayList);
        calender = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd");


        taskListView.setAdapter(mTaskAdapter);


/**
 * handling fab for adding task
 */
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProjectDetails.this);
                LayoutInflater inflater = (LayoutInflater) ProjectDetails.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.add_task_dialog, null);
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
                        addTask(edtAddTaskName.getText().toString());
                        dialogAddProject.dismiss();
                    }
                });
            }
        });


        /**
         * expand view
         */
        if (!expandablelayout2.isExpanded()) {
            expandablelayout2.expand();
            expandCollapseArrow2.animate().rotation(0).start();
        }

        /**
         * retriving intents extras
         */
        Bundle extras = getIntent().getExtras();
        mId = extras.getInt("mId");
        queryProject();
        queryAllTasks();


        /**
         * onclick for updating desc
         */
        tvProjectDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDesc();
            }
        });


        /**
         * start date update
         */
        edtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mStartYear = c.get(Calendar.YEAR);
                mStartMonth = c.get(Calendar.MONTH);
                mStartDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ProjectDetails.this,
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
                                values.put(COLUMN_START_DATE, date);
                                // Which row to update, based on the title
                                String selection = ProjectContract.ProjectEntry._ID + " LIKE ?";
                                String[] selectionArgs = {String.valueOf(mId)};
                                int count = writableProjectDb.update(
                                        TABLE_NAME,
                                        values,
                                        selection,
                                        selectionArgs);

                                queryProject();


                            }
                        }, mStartYear, mStartMonth, mStartDay);

                datePickerDialog.show();
            }
        });

        /**
         * end date update
         */
        edtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mEndYear = c.get(Calendar.YEAR);
                mEndMonth = c.get(Calendar.MONTH);
                mEndDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ProjectDetails.this,
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
                                values.put(COLUMN_END_DATE, date);
                                // Which row to update, based on the title
                                String selection = ProjectContract.ProjectEntry._ID + " LIKE ?";
                                String[] selectionArgs = {String.valueOf(mId)};
                                int count = writableProjectDb.update(
                                        TABLE_NAME,
                                        values,
                                        selection,
                                        selectionArgs);

                                queryProject();


                            }
                        }, mEndYear, mEndMonth, mEndDay);
                datePickerDialog.show();
            }
        });


        filterSmoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public boolean onItemSelect(int i) {
                switch (i) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "all projects clicked", Toast.LENGTH_SHORT).show();
                        collapse();
                        return true;
                    case 1:
                        Toast.makeText(getApplicationContext(), "completed clicked", Toast.LENGTH_SHORT).show();
                        collapse();
                        return true;
                    case 2:
                        Toast.makeText(getApplicationContext(), "pending clicked", Toast.LENGTH_SHORT).show();
                        collapse();
                        return true;
                    default:
                        return false;
                }
            }


        });


        /**
         * on item long press
         */
        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {

                final int mId = taskArrayList.get(position).getmTaskId();
                // Creating the AlertDialog with a custom xml layout (you can still use the default Android version)
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProjectDetails.this);
                LayoutInflater inflater = (LayoutInflater) ProjectDetails.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDelete = inflater.inflate(R.layout.delete_task_dialog, null);
                builder.setView(viewDelete);


                final Dialog dialogDeleteTask = builder.show();
                Button btnCancelDelete = dialogDeleteTask.findViewById(R.id.btnCancelDelete);
                Button btnDelete = dialogDeleteTask.findViewById(R.id.btnDelete);

                btnCancelDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogDeleteTask.dismiss();
                    }
                });
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Delete Operation
                        deleteTask(mId);
                        Toast.makeText(getApplicationContext(), "Deleted project: " + mId, Toast.LENGTH_SHORT).show();
                        dialogDeleteTask.dismiss();
                    }
                });
                return true;
            }
        });
    }


    /**
     * method to query project detail
     */
    public void queryProject() {
        String[] projection = {
                COLUMN_PROJECT_NAME, COLUMN_START_DATE, COLUMN_END_DATE, COLUMN_DESCRIPTION, COLUMN_PROJECT_PROGRESS
        };
        String selection = ProjectContract.ProjectEntry._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(mId)};

        Cursor cursor = readableProjectDb.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        cursor.moveToNext();
        mProjectName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROJECT_NAME));
        mProjectStartDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE));
        mProjectEndDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE));
        mProjectDescription = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
        tvProjectDescription.setText(mProjectDescription);
        tvProjectName.setText(mProjectName);
        edtStartDate.setText(mProjectStartDate);
        edtEndDate.setText(mProjectEndDate);

        tvDaysLeft.setText("Days Left : " + getDaysLeft(mProjectEndDate));
        tvProjectTarget.setText("Target : " + getTarget(mProjectStartDate, mProjectEndDate) + "% /day");

        cursor.close();
    }


    /**
     * method to expand and collapse dashoard
     *
     * @param view
     */
    public void expand_collapse(View view) {

        if (expandablelayout2.isExpanded()) {
            expandablelayout2.collapse();
            expandCollapseArrow2.animate().rotation(180).start();
        } else {
            expandCollapseArrow2.animate().rotation(0).start();
            expandablelayout2.expand();
        }
    }

    public void collapse() {
        if (expandablelayout2.isExpanded()) {
            expandablelayout2.collapse();
            expandCollapseArrow2.animate().rotation(180).start();
        }
    }

    /**
     * add new task
     */
    public void addTask(String projectName) {
        int mStartYear = calender.get(Calendar.YEAR);
        int mStartMonth = calender.get(Calendar.MONTH) + 1;
        int mStartDay = calender.get(Calendar.DAY_OF_MONTH);

        String mStartDate = mStartYear + "-" + mStartMonth + "-" + mStartDay;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TASK_NAME, projectName);
        cv.put(COLUMN_TASK_END_DATE, mStartDate);
        cv.put(COLUMN_TASK_PROJECT_ID, mId);
        Log.i("mStartDate: ", mStartDate);
        if (writableProjectDb.insert(TASK_TABLE_NAME, null, cv) == -1) {
            Toast.makeText(getApplicationContext(), "Project can not be added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), projectName + " Added", Toast.LENGTH_SHORT).show();
        }
        queryAllTasks();

    }

    /**
     * Query all project
     */
    public void queryAllTasks() {
        String[] projection = {
                COLUMN_TASK_NAME, TASK_ID, COLUMN_TASK_PROJECT_ID
        };
        String selection = COLUMN_TASK_PROJECT_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(mId)};

        Cursor cursor = readableProjectDb.query(
                TASK_TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        taskArrayList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(TASK_ID));
            String taskName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME));

            taskArrayList.add(new TaskModel(id, taskName, 98));
            mTaskAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }


    /**
     * method to calculate target
     */
    public String getTarget(String startDate, String endDate) {
        Date mdate = null;
        try {
            mdate = inputFormat.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = null;
        try {
            date = inputFormat.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int diffInDays = (int) ((date.getTime() - mdate.getTime()) / (1000 * 60 * 60 * 24));

        double target = 100.00 / (Double.valueOf(diffInDays));
        DecimalFormat df = new DecimalFormat("0.00");


        return df.format(target);


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


    /**
     * description update
     */

    public void updateDesc() {
        String currentDesc = tvProjectDescription.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(ProjectDetails.this);
        LayoutInflater inflater = (LayoutInflater) ProjectDetails.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                values.put(COLUMN_DESCRIPTION, desc);
                // Which row to update, based on the title
                String selection = ProjectContract.ProjectEntry._ID + " LIKE ?";
                String[] selectionArgs = {String.valueOf(mId)};
                int count = writableProjectDb.update(
                        TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                queryProject();
                dialogAddProject.dismiss();

            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int mId = taskArrayList.get(position).getmTaskId();
        Toast.makeText(getApplicationContext(), "" + mId, Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(ProjectDetails.this, ProjectDetails.class);
        myIntent.putExtra("mId", mId); //Optional parameters
        ProjectDetails.this.startActivity(myIntent);
    }

    public void deleteTask(int id) {
        // Define 'where' part of query.
        String selection = TASK_ID + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};
// Issue SQL statement.
        queryAllTasks();

        int deletedRows = writableProjectDb.delete(TASK_TABLE_NAME, selection, selectionArgs);

        queryAllTasks();
    }
}