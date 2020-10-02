package com.example.projectprogresstracker.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "Task_Table", foreignKeys = @ForeignKey(entity = Project.class, parentColumns = "ID", childColumns = "Project_Id", onDelete = CASCADE, onUpdate = CASCADE))
public class Task {


    @ColumnInfo(name = "Project_Id", index = true)
    private int projectId;

    @ColumnInfo(name = "Task_Id")
    @PrimaryKey(autoGenerate = true)
    private int TaskID;

    private String TaskName;

    private int TaskProgress;

    public Task(int projectId, String TaskName, int TaskProgress) {
        this.projectId = projectId;
        this.TaskName = TaskName;
        this.TaskProgress = TaskProgress;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String TaskName) {
        this.TaskName = TaskName;
    }

    public int getTaskProgress() {
        return TaskProgress;
    }

    public void setTaskProgress(int TaskProgress) {
        this.TaskProgress = TaskProgress;
    }


    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getTaskID() {
        return TaskID;
    }
    public void setTaskID(int taskID) {
        TaskID = taskID;
    }


}
