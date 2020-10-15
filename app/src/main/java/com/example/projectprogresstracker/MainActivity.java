package com.example.projectprogresstracker;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.projectprogresstracker.data.ProjectDbHelper;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.skydoves.expandablelayout.ExpandableLayout;
import com.skydoves.progressview.ProgressView;

import java.util.ArrayList;
import java.util.Calendar;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_PROJECT_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_PROJECT_PROGRESS;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_START_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.TABLE_NAME;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry._ID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * instance variables declaration
     */
    BottomAppBar bottomAppBar;
    ExpandableLayout expandablelayout;
    ImageView expandCollapseArrow;
    ListView projectListView;
    ProjectAdapter mProjectAdapter;
    FloatingActionButton fabAddProject;
    SmoothBottomBar filterSmoothBottomBar;
    ArrayList<ProjectModel> projectArrayList;
    SQLiteDatabase writableProjectDb, readableProjectDb;
    ProjectDbHelper projectDbHelper;
    TextView allProjectCount, completedProjectCount, pendingProjectCount;
    ProgressView pvAllProjects, pvCompletedProjects, pvPendingProject;
    SharedPreferences sharedPreferences;

    EditText edtddProject, edtAddProjectName;
    Button btnCreate, btnCancel, btnCancelDelete, btnDelete;

    private final int FILTER_ALL_PROJECTS = 1;
    private final int FILTER_COMPLETED_PROJECTS = 2;
    private final int FILTER_PENDING_PROJECTS = 3;
    private int getFilter = FILTER_ALL_PROJECTS;
    Calendar calender;

    @Override
    protected void onPostResume() {
        queryAllProject();
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * initialisation of variables
         */
        expandablelayout = findViewById(R.id.expandable);
        expandCollapseArrow = findViewById(R.id.expand_collapse_arrow);
        bottomAppBar = findViewById(R.id.bottomAppbar);
        projectListView = findViewById(R.id.project_listView);
        fabAddProject = findViewById(R.id.fab_add_project);
        filterSmoothBottomBar = findViewById(R.id.filterAppbar);
        projectDbHelper = new ProjectDbHelper(this);
        writableProjectDb = projectDbHelper.getWritableDatabase();
        readableProjectDb = projectDbHelper.getReadableDatabase();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        projectListView.setOnItemClickListener(this);
        projectArrayList = new ArrayList<>();
        mProjectAdapter = new ProjectAdapter(this, projectArrayList);
        calender = Calendar.getInstance();
        allProjectCount = findViewById(R.id.all_project_count);
        completedProjectCount = findViewById(R.id.completed_project_count);
        pendingProjectCount = findViewById(R.id.pending_project_count);
        pvAllProjects = findViewById(R.id.progressView1);
        pvCompletedProjects = findViewById(R.id.progressView2);
        pvPendingProject = findViewById(R.id.progressView3);


        projectListView.setAdapter(mProjectAdapter);
        projectListView.setEmptyView(findViewById(R.id.emptyListView));

        /**
         * filter handeling
         */
        filterSmoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public boolean onItemSelect(int i) {
                switch (i) {
                    case 0:

                        collapse();
                        getFilter = FILTER_ALL_PROJECTS;
                        queryAllProject();
                        return true;
                    case 1:

                        collapse();
                        getFilter = FILTER_COMPLETED_PROJECTS;
                        queryAllProject();
                        return true;
                    case 2:

                        collapse();
                        getFilter = FILTER_PENDING_PROJECTS;
                        queryAllProject();
                        return true;
                    default:
                        getFilter = FILTER_ALL_PROJECTS;
                        queryAllProject();


                        return false;
                }
            }


        });


        /**
         * bottomAppBar click handling
         */
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuDashboard:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.dashboard) + " " + getResources().getString(R.string.clicked), Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menuTasks:
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.task) + " " + getResources().getString(R.string.clicked), Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menuSettings:
                        Intent openSettingIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(openSettingIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        queryAllProject();


        /**
         * fab onClick method
         */
        {
            fabAddProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Creating the AlertDialog with a custom xml layout (you can still use the default Android version)
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.add_project_dialog, null);
                    builder.setView(view);


                    final Dialog dialogAddProject = builder.show();
                    btnCancel = dialogAddProject.findViewById(R.id.btnCancel);
                    btnCreate = dialogAddProject.findViewById(R.id.btnCreate);
                    edtAddProjectName = dialogAddProject.findViewById(R.id.edt_add_project_name);
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogAddProject.dismiss();
                        }
                    });
                    btnCreate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addProject(edtAddProjectName.getText().toString());
                            dialogAddProject.dismiss();
                        }
                    });


                }

            });
        }


        /**
         * on item long press
         */
        projectListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {

                final int mId = projectArrayList.get(position).getmId();
                // Creating the AlertDialog with a custom xml layout (you can still use the default Android version)
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View viewDelete = inflater.inflate(R.layout.delete_project_dialog, null);
                builder.setView(viewDelete);


                final Dialog dialogDeleteProject = builder.show();
                btnCancelDelete = dialogDeleteProject.findViewById(R.id.btnCancelDelete);
                btnDelete = dialogDeleteProject.findViewById(R.id.btnDelete);

                btnCancelDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogDeleteProject.dismiss();
                    }
                });
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Delete Operation
                        deleteProject(mId);
                        Toast.makeText(getApplicationContext(), "Deleted project: " + mId, Toast.LENGTH_SHORT).show();
                        dialogDeleteProject.dismiss();
                    }
                });
                return true;
            }
        });


    }


    /**
     * method to expand and collapse dashoard
     *
     * @param view
     */
    public void expand_collapse(View view) {

        if (expandablelayout.isExpanded()) {
            expandablelayout.collapse();
            expandCollapseArrow.animate().rotation(180).start();
        } else {
            expandCollapseArrow.animate().rotation(0).start();
            expandablelayout.expand();
        }
    }

    public void collapse() {
        if (expandablelayout.isExpanded() && sharedPreferences.getBoolean("collapse", true)) {
            expandablelayout.collapse();
            expandCollapseArrow.animate().rotation(180).start();
        }
    }


    /**
     * add new project
     */
    public void addProject(String projectName) {
        int mStartYear = calender.get(Calendar.YEAR);
        int mStartMonth = calender.get(Calendar.MONTH) + 1;
        int mStartDay = calender.get(Calendar.DAY_OF_MONTH);

        String mStartDate = mStartYear + "-" + mStartMonth + "-" + mStartDay;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PROJECT_NAME, projectName);
        cv.put(COLUMN_START_DATE, mStartDate);
        cv.put(COLUMN_END_DATE, mStartDate);
        Log.i("mStartDate: ", mStartDate);
        if (writableProjectDb.insert(TABLE_NAME, null, cv) == -1) {
            Toast.makeText(getApplicationContext(), "Project can not be added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), projectName + " Added", Toast.LENGTH_SHORT).show();
        }
        queryAllProject();

    }


    /**
     * Query all project
     */
    public void queryAllProject() {
        String[] projection = {
                COLUMN_PROJECT_NAME, _ID, COLUMN_START_DATE, COLUMN_END_DATE, COLUMN_PROJECT_PROGRESS
        };

        Cursor cursor = readableProjectDb.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        projectArrayList.clear();
        int completedCount = 0;
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            int projectProgress = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROJECT_PROGRESS));
            if (projectProgress == 100)
                completedCount++;

        }
        cursor.close();
        cursor = readableProjectDb.query(
                TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        if (getFilter == FILTER_PENDING_PROJECTS) {
            {
                while (cursor.moveToNext()) {
                    int projectProgress = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROJECT_PROGRESS));
                    if (projectProgress != 100) {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
                        String projectName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROJECT_NAME));
                        String startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE));
                        String endDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE));
                        projectArrayList.add(new ProjectModel(id, projectName, startDate, endDate, projectProgress));
                    }

                }
                if (projectArrayList.size() == 0)
                    Toast.makeText(getApplicationContext(), "No pending project found", Toast.LENGTH_SHORT).show();
            }
        } else if (getFilter == FILTER_COMPLETED_PROJECTS) {
            while (cursor.moveToNext()) {
                int projectProgress = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROJECT_PROGRESS));
                if (projectProgress == 100) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
                    String projectName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROJECT_NAME));
                    String startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE));
                    String endDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE));
                    projectArrayList.add(new ProjectModel(id, projectName, startDate, endDate, projectProgress));
                }
            }
            if (projectArrayList.size() == 0)
                Toast.makeText(getApplicationContext(), "No completed project found", Toast.LENGTH_SHORT).show();

        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
                String projectName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROJECT_NAME));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE));
                int projectProgress = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PROJECT_PROGRESS));
                projectArrayList.add(new ProjectModel(id, projectName, startDate, endDate, projectProgress));
            }
        }

        allProjectCount.setText(cursor.getCount() + "");
        completedProjectCount.setText(completedCount + "");
        pendingProjectCount.setText((cursor.getCount() - completedCount) + "");
        mProjectAdapter.notifyDataSetChanged();
        pvAllProjects.setProgress(100);
        if (cursor.getCount() != 0) {
            pvCompletedProjects.setProgress((float) (completedCount * 100 / cursor.getCount()));
            pvPendingProject.setProgress((float) (100 - (completedCount * 100 / cursor.getCount())));
        }
        cursor.close();
    }

    public void deleteProject(int id) {
        // Define 'where' part of query.
        String selection = _ID + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};
// Issue SQL statement.
        queryAllProject();

        int deletedRows = writableProjectDb.delete(TABLE_NAME, selection, selectionArgs);

        queryAllProject();
    }


    /**
     * on item click
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int mId = projectArrayList.get(position).getmId();
        Toast.makeText(getApplicationContext(), "" + mId, Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(MainActivity.this, ProjectDetails.class);
        myIntent.putExtra("mId", mId); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }


}
