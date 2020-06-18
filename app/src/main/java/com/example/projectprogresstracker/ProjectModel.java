package com.example.projectprogresstracker;

public class ProjectModel {
    private String mProjectName;
    private String mProjectStartDate;
    private String mProjectEndDate;
    private int mProjectProgress;
    int mId;

    public ProjectModel(int id,String mProjectName, String mProjectStartDate, String mProjectEndDate, int mProjectProgress) {
        this.mProjectName = mProjectName;
        this.mProjectStartDate = mProjectStartDate;
        this.mProjectEndDate = mProjectEndDate;
        this.mProjectProgress = mProjectProgress;
        this.mId = id;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmProjectName() {
        return mProjectName;
    }

    public void setmProjectName(String mProjectName) {
        this.mProjectName = mProjectName;
    }

    public String getmProjectStartDate() {
        return mProjectStartDate;
    }

    public void setmProjectStartDate(String mProjectStartDate) {
        this.mProjectStartDate = mProjectStartDate;
    }

    public String getmProjectEndDate() {
        return mProjectEndDate;
    }

    public void setmProjectEndDate(String mProjectEndDate) {
        this.mProjectEndDate = mProjectEndDate;
    }

    public int getmProjectProgress() {
        return mProjectProgress;
    }

    public void setmProjectProgress(int mProjectProgress) {
        this.mProjectProgress = mProjectProgress;
    }
}
