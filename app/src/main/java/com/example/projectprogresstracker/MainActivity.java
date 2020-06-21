package com.example.projectprogresstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

//import com.example.flatdialoglibrary.dialog.FlatDialog;

import com.example.flatdialoglibrary.dialog.FlatDialog;

import com.example.projectprogresstracker.data.ProjectContract;
import com.example.projectprogresstracker.data.ProjectDbHelper;
import com.gohn.nativedialog.ButtonClickListener;
import com.gohn.nativedialog.ButtonType;
import com.gohn.nativedialog.NDialog;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.skydoves.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_END_DATE;
import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_PROJECT_NAME;
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
    FloatingActionButton fab;
    SmoothBottomBar filterSmoothBottomBar;
    ArrayList<ProjectModel> projectArrayList;
    SQLiteDatabase writableProjectDb, readableProjectDb;
    ProjectDbHelper projectDbHelper;
    NDialog nDialog;
    EditText edtddProject;
    Calendar calender;

    @Override
    protected void onPostResume() {
        queryProject();
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * initialisation of variables
         */
        expandablelayout = (ExpandableLayout) findViewById(R.id.expandable);
        expandCollapseArrow = (ImageView) findViewById(R.id.expand_collapse_arrow);
        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppbar);
        projectListView = (ListView) findViewById(R.id.project_listView);
        fab = (FloatingActionButton) findViewById(R.id.fab_add_project);
        filterSmoothBottomBar = (SmoothBottomBar) findViewById(R.id.filterAppbar);
        projectDbHelper = new ProjectDbHelper(this);
        writableProjectDb = projectDbHelper.getWritableDatabase();
        readableProjectDb = projectDbHelper.getReadableDatabase();
        projectListView.setOnItemClickListener(this);
        projectArrayList = new ArrayList<>();
        mProjectAdapter = new ProjectAdapter(this, projectArrayList);
        calender = Calendar.getInstance();
        nDialog = new NDialog(MainActivity.this, ButtonType.ONE_BUTTON);


        projectListView.setAdapter(mProjectAdapter);
        projectListView.setEmptyView(findViewById(R.id.emptyListView));


        filterSmoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public boolean onItemSelect(int i) {
                switch (i) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "all projects clicked", Toast.LENGTH_SHORT).show();

                        return true;
                    case 1:
                        Toast.makeText(getApplicationContext(), "completed clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case 2:
                        Toast.makeText(getApplicationContext(), "pending clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
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
                        Toast.makeText(getApplicationContext(), "Dashboard clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menuTasks:
                        Toast.makeText(getApplicationContext(), "Task clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menuSettings:
                        Toast.makeText(getApplicationContext(), "settings clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        queryProject();


        /**
         * fab onClick method
         */
        {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final FlatDialog flatDialog = new FlatDialog(MainActivity.this);
                    flatDialog.setTitle("Add New Project")
                            .setFirstTextFieldHint("project name")
                            .setFirstButtonText("ADD PROJECT")
                            .isCancelable(true)
                            .setFirstButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.colorSecondary))
                            .setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                            .withFirstButtonListner(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    addProject(flatDialog.getFirstTextField());

                                    flatDialog.dismiss();
                                }
                            })
                            .show();
//                    nDialog.setTitle("Add New Project");
//                    nDialog.setMessage(R.string.dialog_message);
//                    final List<View> childViews = nDialog.getCustomViewChildren();
//                    // Bottom Button OnClick Event Handler
//
//                    ButtonClickListener buttonClickListener = new ButtonClickListener() {
//                        @Override
//                        public void onClick(int button) {
//                            switch (button) {
//                                case NDialog.BUTTON_POSITIVE:
//                                    for (View childView : childViews) {
//                                        switch (childView.getId()) {
//                                            case R.id.edt_add_project:
//                                                String mproName;
//                                                mproName = ((Switch) childView).getText().toString();
//                                                addProject(edtddProject.getText().toString());
//                                                break;
//
//                                        }
//                                    }
//
//
//
//
//                                    Toast.makeText(getApplicationContext(),"Added", Toast.LENGTH_SHORT).show();
//                                    break;
//
//                            }
//                        }
//                    };
//
//                    nDialog.setPositiveButtonText("Add Project");
//                    nDialog.setPositiveButtonTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorWhite));
//                    nDialog.setPositiveButtonOnClickDismiss(false); // default : true
//                    nDialog.setPositiveButtonClickListener(buttonClickListener);
//
//
//// Custom View Setup (View or resourceId)
//                    nDialog.setCustomView(R.layout.edit_text);
//
//                    nDialog.show();


                }
            });
        }


        /**
         * on item long press
         */
        projectListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {

                int mId = projectArrayList.get(position).getmId();
                deleteProject(mId);
                Toast.makeText(getApplicationContext(), "Deleted project: " + mId, Toast.LENGTH_SHORT).show();

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
        queryProject();

    }


    /**
     * Query all project
     */
    public void queryProject() {
        String[] projection = {
                COLUMN_PROJECT_NAME, _ID, COLUMN_START_DATE, COLUMN_END_DATE
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
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            String projectName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROJECT_NAME));
            String startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE));
            String endDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE));

            projectArrayList.add(new ProjectModel(id, projectName, startDate, endDate, 98));
            mProjectAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }

    public void deleteProject(int id) {
        // Define 'where' part of query.
        String selection = ProjectContract.ProjectEntry._ID + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};
// Issue SQL statement.
        queryProject();

        int deletedRows = writableProjectDb.delete(TABLE_NAME, selection, selectionArgs);

        queryProject();
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
