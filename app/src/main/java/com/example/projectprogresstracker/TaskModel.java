package com.example.projectprogresstracker;

public class TaskModel implements Comparable<TaskModel> {

    private String mTaskName;
    private int mTaskProgress;
    private int mTaskId;

    public TaskModel(int mTaskId, String mTaskName, int mTaskProgress) {
        this.mTaskName = mTaskName;
        this.mTaskProgress = mTaskProgress;
        this.mTaskId = mTaskId;
    }

    public String getmTaskName() {
        return mTaskName;
    }

    public void setmTaskName(String mTaskName) {
        this.mTaskName = mTaskName;
    }

    public int getmTaskProgress() {
        return mTaskProgress;
    }

    public void setmTaskProgress(int mTaskProgress) {
        this.mTaskProgress = mTaskProgress;
    }

    public int getmTaskId() {
        return mTaskId;
    }

    public void setmTaskId(int mTaskId) {
        this.mTaskId = mTaskId;
    }

    @Override
    public int compareTo(TaskModel o) {
        return mTaskProgress - o.mTaskProgress;
    }
}
