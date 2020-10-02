package com.example.projectprogresstracker.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Project_Table")
public class Project {

    @ColumnInfo(name = "ID")
    @PrimaryKey(autoGenerate = false)
    int Id;
    @ColumnInfo(name = "Project_Name")
    private String ProjectName;
    @ColumnInfo(name = "Project_StartDate")
    private String ProjectStartDate;
    @ColumnInfo(name = "Project_EndDate")
    private String ProjectEndDate;
    @ColumnInfo(name = "Project_Progress")
    private int ProjectProgress;

    public Project(int Id, String ProjectName, String ProjectStartDate, String ProjectEndDate, int ProjectProgress) {
        this.Id = Id;
        this.ProjectName = ProjectName;
        this.ProjectStartDate = ProjectStartDate;
        this.ProjectEndDate = ProjectEndDate;
        this.ProjectProgress = ProjectProgress;
    }



    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getProjectStartDate() {
        return ProjectStartDate;
    }

    public void setProjectStartDate(String projectStartDate) {
        ProjectStartDate = projectStartDate;
    }

    public String getProjectEndDate() {
        return ProjectEndDate;
    }

    public void setProjectEndDate(String projectEndDate) {
        ProjectEndDate = projectEndDate;
    }

    public int getProjectProgress() {
        return ProjectProgress;
    }

    public void setProjectProgress(int projectProgress) {
        ProjectProgress = projectProgress;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
}
