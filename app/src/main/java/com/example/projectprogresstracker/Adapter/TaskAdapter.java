package com.example.projectprogresstracker.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.projectprogresstracker.Entity.Task;
import com.example.projectprogresstracker.R;
import com.skydoves.progressview.HighlightView;
import com.skydoves.progressview.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {

    Context mContext;
    private List<Task> mTaskList;

    public TaskAdapter(@NonNull Context context, @NonNull ArrayList<Task> resource) {
        super(context, 0, resource);
        mContext = context;
        mTaskList = resource;
    }

    @Override
    public Task getItem(int position) {
        return mTaskList.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);

        Task currentTask = mTaskList.get(position);

        Switch switcherX = listItem.findViewById(R.id.task_witcher);
        if (currentTask.getTaskProgress() == 100)
            switcherX.setChecked(true);
        else
            switcherX.setChecked(false);

        TextView taskName = listItem.findViewById(R.id.tv_task_name);
        taskName.setText(currentTask.getTaskName());

        ProgressView progressView = listItem.findViewById(R.id.pv_task);
        HighlightView pvHighlightView = progressView.getHighlightView();
        progressView.setProgress((float) currentTask.getTaskProgress());
        if (currentTask.getTaskProgress() > 75) {

            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorGreen));
        } else if (currentTask.getTaskProgress() > 50) {
            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorSkyBlue));
        } else {
            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorSecondary));
        }

        TextView taskProgress = listItem.findViewById(R.id.tv_task_progress);
        taskProgress.setText(currentTask.getTaskProgress() + " %");


        return listItem;


    }
}
