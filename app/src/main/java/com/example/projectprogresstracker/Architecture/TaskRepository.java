package com.example.projectprogresstracker.Architecture;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.projectprogresstracker.Entity.Task;
import com.example.projectprogresstracker.data.TaskDao;
import com.example.projectprogresstracker.data.TaskDataBase;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> listLiveData;
    public TaskRepository(Application application){
        TaskDataBase taskDataBase = TaskDataBase.getTaskDataBase(application);
        taskDao = taskDataBase.taskDao();
    }
    public LiveData<List<Task>> getTaskLiveData(int id){
        return taskDao.getTasksForProject(id);
    }
    public void insert(Task task){
        taskDao.insert(task);
    }
}
