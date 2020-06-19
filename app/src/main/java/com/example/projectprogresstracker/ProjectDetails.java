package com.example.projectprogresstracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.skydoves.expandablelayout.ExpandableLayout;

public class ProjectDetails extends AppCompatActivity {
    ExpandableLayout expandablelayout2;
    ImageView expandCollapseArrow2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);


        expandablelayout2 = (ExpandableLayout) findViewById(R.id.expandable2);
        expandCollapseArrow2 = (ImageView) findViewById(R.id.expand_collapse_arrow2);
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

}