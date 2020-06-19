package com.example.projectprogresstracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

//import com.example.flatdialoglibrary.dialog.FlatDialog;

import com.example.flatdialoglibrary.dialog.FlatDialog;

import com.example.projectprogresstracker.data.ProjectContract;
import com.example.projectprogresstracker.data.ProjectDbHelper;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.skydoves.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

import static com.example.projectprogresstracker.data.ProjectContract.ProjectEntry.COLUMN_PROJECT_NAME;
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
    SQLiteDatabase writableProjectDb,readableProjectDb;
    ProjectDbHelper projectDbHelper;



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
        projectListView.setAdapter(mProjectAdapter);


        filterSmoothBottomBar.setOnItemSelectedListener(new OnItemSelectedListener(){

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


                }
            });
        }




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
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PROJECT_NAME,projectName);
        if(writableProjectDb.insert(TABLE_NAME, null, cv)==-1)
        {
            Toast.makeText(getApplicationContext(),"Project can not be added",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),projectName + " Added",Toast.LENGTH_SHORT).show();
        }
        queryProject();

    }


    /**
     * Query all project
     */
    public void queryProject(){
        String[] projection = {
                COLUMN_PROJECT_NAME, _ID
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
        while(cursor.moveToNext()) {
            String projectName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROJECT_NAME));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            projectArrayList.add(new ProjectModel( id,projectName, "20/01/2020", "28/01/2020", 55));
            mProjectAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }


    /**
     * on item click
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int mId = projectArrayList.get(position).getmId();
        Toast.makeText(getApplicationContext(), "" + mId , Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(MainActivity.this, ProjectDetails.class);
        myIntent.putExtra("id", mId); //Optional parameters
        MainActivity.this.startActivity(myIntent);
    }
}
