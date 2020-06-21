package com.example.projectprogresstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectprogresstracker.data.ProjectContract;
import com.example.projectprogresstracker.data.ProjectDbHelper;
import com.skydoves.expandablelayout.ExpandableLayout;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_DESCRIPTION;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_PROJECT_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_PROJECT_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_START_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TABLE_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry._ID;

public class ProjectDetails extends AppCompatActivity {
    ExpandableLayout expandablelayout2;
    ImageView expandCollapseArrow2;
    TextView edtStartDate, edtEndDate, tvProjectName, tvDaysLeft, tvProjectTarget;
    int mStartYear, mStartMonth, mStartDay, mEndYear, mEndMonth, mEndDay;
    SQLiteDatabase writableProjectDb, readableProjectDb;
    ProjectDbHelper projectDbHelper;
    int mId;
    SimpleDateFormat sdf, inputFormat;
    String mProjectName, mProjectDescription, mProjectStartDate, mProjectEndDate, getmProjectTarget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        tvProjectName = (TextView) findViewById(R.id.tv_project_name_detail);
        edtEndDate = (TextView) findViewById(R.id.edt_end_date);
        edtStartDate = (TextView) findViewById(R.id.edt_start_date);
        expandablelayout2 = (ExpandableLayout) findViewById(R.id.expandable2);
        expandCollapseArrow2 = (ImageView) findViewById(R.id.expand_collapse_arrow2);
        projectDbHelper = new ProjectDbHelper(this);
        writableProjectDb = projectDbHelper.getWritableDatabase();
        readableProjectDb = projectDbHelper.getReadableDatabase();
        tvDaysLeft = (TextView) findViewById(R.id.tv_project_days_left_detail);
        tvProjectTarget = (TextView) findViewById(R.id.tv_project_target_detail);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd");

        /**
         * retriving intents extras
         */
        Bundle extras = getIntent().getExtras();
        mId = (int) extras.getInt("mId");
        queryProject();


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

                                edtStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);


                            }
                        }, mStartYear, mStartMonth, mStartDay);

                datePickerDialog.show();
            }
        });
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
                                edtEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                            }
                        }, mEndYear, mEndMonth, mEndDay);
                datePickerDialog.show();
            }
        });
    }


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
        tvProjectName.setText(mProjectName);
        edtStartDate.setText(mProjectStartDate);
        edtEndDate.setText(mProjectEndDate);

        tvDaysLeft.setText("Days Left : " + getDaysLeft(mProjectEndDate));
        tvProjectTarget.setText("Target : " + getTarget(mProjectStartDate, mProjectEndDate) + "%/day");

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


        return String.valueOf(df.format(target));


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

    public void back(View view) {
        finish();
    }


}