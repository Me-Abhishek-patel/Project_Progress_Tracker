package com.example.projectprogresstracker.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.projectprogresstracker.Entity.Project;
import com.example.projectprogresstracker.R;
import com.skydoves.progressview.HighlightView;
import com.skydoves.progressview.ProgressView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProjectAdapter extends ArrayAdapter<Project> {

    Context mContext;
    private List<Project> mProjectList;
    SimpleDateFormat sdf, inputFormat;
    Calendar c;


    public ProjectAdapter(@NonNull Context context, @NonNull List<Project> objects) {
        super(context, 0, objects);
        mContext = context;
        mProjectList = objects;
    }

    @Override
    public Project getItem(int position) {
        return mProjectList.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.project_item, parent, false);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        inputFormat = new SimpleDateFormat("yyyy-MM-dd");


        Project currentProject = mProjectList.get(position);

        /**
         * project Start Date, end date & today's date text views
         */
        TextView startDate = listItem.findViewById(R.id.tv_project_star_date);
        startDate.setText(getDay(currentProject.getProjectStartDate()));
        Log.d("Checking", getDay(currentProject.getProjectStartDate()));

        TextView endDate = listItem.findViewById(R.id.tv_project_end_date);
        endDate.setText(getDay(currentProject.getProjectEndDate()));
        Log.d("Checking", getDay(currentProject.getProjectEndDate()));

        TextView todaysDate = listItem.findViewById(R.id.tv_todays_date);
        final Calendar c = Calendar.getInstance();
        todaysDate.setText(String.valueOf(c.get(Calendar.DAY_OF_MONTH)));

        /**
         * days left textview
         */


        TextView daysLeft = listItem.findViewById(R.id.tv_days_left);
        if (currentProject.getProjectProgress() == 100) {
            daysLeft.setText(getContext().getText(R.string.completed));
            daysLeft.setGravity(Gravity.END);

            daysLeft.setTextColor(ContextCompat.getColor(mContext,R.color.colorGreen));
        }
        else {
          //  daysLeft.setText("Days left: " + getDaysLeft(currentProjectModel.getProjectEndDate()));
            daysLeft.setGravity(Gravity.START);

            daysLeft.setTextColor(ContextCompat.getColor(mContext,R.color.colorSecondary));
        }

        /**
         * project name text view
         */
        TextView name = listItem.findViewById(R.id.tv_project_name);
        name.setText(currentProject.getProjectName());
        Log.d("Checking", currentProject.getProjectName());


        /**
         * progress bar for project progress
         */
        ProgressView progressView = listItem.findViewById(R.id.pv_project);
        HighlightView pvHighlightView = progressView.getHighlightView();
        progressView.setProgress((float) currentProject.getProjectProgress());
        if(currentProject.getProjectProgress()>75) {

            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorGreen));
        } else if(currentProject.getProjectProgress()>50) {
            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorSkyBlue));
        } else {
            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorSecondary));      }


        /**
         * progress percentage textView
         */
        TextView progress = listItem.findViewById(R.id.tv_project_progress);
        progress.setText(currentProject.getProjectProgress() + "%");


        View lineToday = listItem.findViewById(R.id.line_today);
        View lineEnd = listItem.findViewById(R.id.line_end_date);


        if (currentProject.getProjectProgress() == 100) {
            lineToday.getBackground().setAlpha(255);
            todaysDate.getBackground().setAlpha(255);
            lineEnd.getBackground().setAlpha(255);
            endDate.getBackground().setAlpha(255);
            todaysDate.setBackgroundResource(R.drawable.circle_skyblue);
            endDate.setBackgroundResource(R.drawable.circle_green);
        } else if (currentProject.getProjectProgress() > 50 && currentProject.getProjectProgress() != 100) {
            lineToday.getBackground().setAlpha(128);
            todaysDate.getBackground().setAlpha(128);
            lineEnd.getBackground().setAlpha(64);
            endDate.getBackground().setAlpha(64);
            todaysDate.setBackgroundResource(R.drawable.circle_skyblue);
            endDate.setBackgroundResource(R.drawable.circle_accent);
        } else if (currentProject.getProjectProgress() < 50 && currentProject.getProjectProgress() != 100) {
            lineToday.getBackground().setAlpha(64);
            todaysDate.getBackground().setAlpha(64);
            lineEnd.getBackground().setAlpha(64);
            endDate.getBackground().setAlpha(64);
            todaysDate.setBackgroundResource(R.drawable.circle_accent);
            endDate.setBackgroundResource(R.drawable.circle_accent);
        }


        return listItem;
    }

    public String getDay(String inputText) {


        Date date = null;
        try {
            date = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String day = String.valueOf(date.getDate());
        return day;

    }

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
    void BackDoorData(List<Project> data){
        mProjectList = data;
        notifyDataSetChanged();
    }
}
