package com.example.projectprogresstracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

//import com.example.flatdialoglibrary.dialog.FlatDialog;

import com.example.flatdialoglibrary.dialog.FlatDialog;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.skydoves.expandablelayout.ExpandableLayout;
import com.skydoves.progressview.HighlightView;
import com.skydoves.progressview.ProgressView;

import java.util.ArrayList;
import java.util.List;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;


public class MainActivity extends AppCompatActivity {

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



        final ArrayList<ProjectModel> projectArrayList = new ArrayList<>();
//
//        projectArrayList.add(new ProjectModel("my Project 1", "20/01/2020", "28/01/2020", 55));
//        projectArrayList.add(new ProjectModel("my Project 2", "20/01/2020", "28/01/2020", 25));
//        projectArrayList.add(new ProjectModel("my Project 3 is very good you know", "20/01/2020", "28/01/2020", 75));
//        projectArrayList.add(new ProjectModel("my Project 4", "20/01/2020", "28/01/2020", 45));
//        projectArrayList.add(new ProjectModel("my Project 5", "20/01/2020", "28/01/2020", 100));
//        projectArrayList.add(new ProjectModel("my Project 6", "20/01/2020", "28/01/2020", 45));
//        projectArrayList.add(new ProjectModel("my Project 6", "20/01/2020", "28/01/2020", 55));
//        projectArrayList.add(new ProjectModel("my Project 6", "20/01/2020", "28/01/2020", 49));
//        projectArrayList.add(new ProjectModel("my Project 6", "20/01/2020", "28/01/2020", 89));
//        projectArrayList.add(new ProjectModel("my Project 6", "20/01/2020", "28/01/2020", 45));
//        projectArrayList.add(new ProjectModel("my Project 6", "20/01/2020", "28/01/2020", 92));
//        projectArrayList.add(new ProjectModel("my Project 6", "20/01/2020", "28/01/2020", 65));

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

        /**
         * fab onClick method
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FlatDialog flatDialog = new FlatDialog(MainActivity.this);
                flatDialog.setTitle("Add New Project")
                        .setFirstTextFieldHint("project name")
                        .setFirstButtonText("CREATE")
//                        .setSecondButtonText("CANCEL")
                        .isCancelable(true)

                        .setFirstButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.colorSecondary))
//                        .setSecondButtonColor(ContextCompat.getColor(getApplicationContext(),R.color.colorSecondary))
//                        .setFirstTextFieldBorderColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent))
                        .setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary))
//                        .setFirstTextFieldHintColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent))
//                        .setTitleColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent))
//                        .setSubtitleColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent))


                        .withFirstButtonListner(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this, flatDialog.getFirstTextField()+"added", Toast.LENGTH_SHORT).show();
                                projectArrayList.add(new ProjectModel(flatDialog.getFirstTextField(), "00/00/0000", "00/00/00020", 0));
                                mProjectAdapter.notifyDataSetChanged();
                                flatDialog.dismiss();
                            }
                        })
//                        .withSecondButtonListner(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                flatDialog.dismiss();
//                            }
//                        })
                        .show();



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
}
