package com.example.projectprogresstracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.projectprogresstracker.R;
import com.example.projectprogresstracker.data.DbModifier;
import com.example.projectprogresstracker.model.TaskModel;
import com.skydoves.progressview.HighlightView;
import com.skydoves.progressview.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class ActivityAdapter extends ArrayAdapter<TaskModel> {

    Context mContext;
    DbModifier dbModifier;
    private List<TaskModel> mTaskList = new ArrayList<>();

    public ActivityAdapter(@NonNull Context context, @NonNull ArrayList<TaskModel> resource) {
        super(context, 0, resource);
        mContext = context;
        mTaskList = resource;
        dbModifier = new DbModifier(mContext);
    }

    @Override
    public TaskModel getItem(int position) {
        return mTaskList.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.task_item, parent, false);

        final TaskModel currentTaskModel = mTaskList.get(position);

        final Switch switcherX = listItem.findViewById(R.id.task_witcher);
        if (currentTaskModel.getmTaskProgress() == 100)
            switcherX.setChecked(true);
        else
            switcherX.setChecked(false);

        switcherX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switcherX.isChecked()) {
                    currentTaskModel.setmTaskProgress(100);
                    dbModifier.setActivityProgress(currentTaskModel.getmTaskId(), 100);
                    Toast.makeText(mContext, "progress set to 100: please refresh", Toast.LENGTH_SHORT).show();
                } else {
                    dbModifier.setTaskProgress(currentTaskModel.getmTaskId(), 0);
                    currentTaskModel.setmTaskProgress(0);
                    Toast.makeText(mContext, "progress set to 0: please refresh", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView taskName = listItem.findViewById(R.id.tv_task_name);
        taskName.setText(currentTaskModel.getmTaskName());

        ProgressView progressView = listItem.findViewById(R.id.pv_task);
        HighlightView pvHighlightView = progressView.getHighlightView();
        progressView.setProgress((float) currentTaskModel.getmTaskProgress());
        if (currentTaskModel.getmTaskProgress() > 75) {

            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorGreen));
        } else if (currentTaskModel.getmTaskProgress() > 50) {
            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorSkyBlue));
        } else {
            pvHighlightView.setColor(ContextCompat.getColor(mContext, R.color.colorSecondary));
        }

        TextView taskProgress = listItem.findViewById(R.id.tv_task_progress);
        taskProgress.setText(currentTaskModel.getmTaskProgress() + " %");


        return listItem;


    }
}
