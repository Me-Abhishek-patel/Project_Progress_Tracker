package com.example.projectprogresstracker;

import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.skydoves.progressview.HighlightView;
import com.skydoves.progressview.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class ProjectAdapter extends ArrayAdapter<ProjectModel> {

    Context mContext;
    private List<ProjectModel> mProjectList = new ArrayList<>();


    public ProjectAdapter(@NonNull Context context, @NonNull List<ProjectModel> objects) {
        super(context, 0, objects);
        mContext = context;
        mProjectList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.project_item,parent,false);

        ProjectModel currentProjectModel = mProjectList.get(position);


        /**
         * days left textview
         */
        TextView daysLeft = (TextView) listItem.findViewById(R.id.tv_days_left);
        if(currentProjectModel.getmProjectProgress()==100)
        {
            daysLeft.setText(getContext().getText(R.string.completed));
            daysLeft.setGravity(Gravity.END);

            daysLeft.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen));
        }
        else {
            daysLeft.setText("Days left: ");
            daysLeft.setGravity(Gravity.START);

            daysLeft.setTextColor(ContextCompat.getColor(mContext,R.color.colorSecondary));
        }

        /**
         * project name text view
         */
        TextView name = (TextView) listItem.findViewById(R.id.tv_project_name);
        name.setText(currentProjectModel.getmProjectName());

        /**
         * progress bar for project progress
         */
        ProgressView progressView = (ProgressView)listItem.findViewById(R.id.pv_project);
        HighlightView pvHighlightView = progressView.getHighlightView();
        progressView.setProgress((float)currentProjectModel.getmProjectProgress());
        if(currentProjectModel.getmProjectProgress()>75) {

            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorGreen));
        } else if(currentProjectModel.getmProjectProgress()>50) {
            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorSkyBlue));
        } else {
            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorSecondary));      }


        /**
         * progress percentage textView
         */
        TextView progress = (TextView) listItem.findViewById(R.id.tv_project_progress);
        progress.setText(currentProjectModel.getmProjectProgress() + "%");


        /**
         * project Start Date, end date & today's date text views
         */
        TextView startDate = (TextView) listItem.findViewById(R.id.tv_project_star_date);
        TextView endDate = (TextView) listItem.findViewById(R.id.tv_project_end_date);
        TextView todaysDate = (TextView) listItem.findViewById(R.id.tv_todays_date);










        return listItem;
    }
}
