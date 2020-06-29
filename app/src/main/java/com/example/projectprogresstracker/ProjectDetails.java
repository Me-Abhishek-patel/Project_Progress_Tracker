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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprogresstracker.data.ProjectContract;
import com.example.projectprogresstracker.data.ProjectDbHelper;
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
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TABLE_NAME;

public class ProjectDetails extends AppCompatActivity {
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
    String mProjectName, mProjectDescription, mProjectStartDate, mProjectEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);


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
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd");


        taskListView.setAdapter(mTaskAdapter);

        mTaskAdapter.add(new TaskModel(1, "my task 1", 56));
        mTaskAdapter.add(new TaskModel(1, "mytask 2", 23));
        mTaskAdapter.add(new TaskModel(1, "mytask 3", 16));
        mTaskAdapter.add(new TaskModel(1, "mytask 4", 100));
        mTaskAdapter.add(new TaskModel(1, "mytask 5", 85));
        mTaskAdapter.add(new TaskModel(1, "mytask 6", 76));
        mTaskAdapter.add(new TaskModel(1, "mytask 7", 98));
        mTaskAdapter.add(new TaskModel(1, "mytask 8", 44));
        mTaskAdapter.add(new TaskModel(1, "mytask 9", 66));
        mTaskAdapter.add(new TaskModel(1, "mytask 10", 36));


        /**
         * retriving intents extras
         */
        Bundle extras = getIntent().getExtras();
        mId = extras.getInt("mId");
        queryProject();


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
     * method to calculate target
     *
     * @param startDate
     * @param endDate
     * @return
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
     *
     * @param dateTill
     * @return
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
     *
     * @param view
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

}