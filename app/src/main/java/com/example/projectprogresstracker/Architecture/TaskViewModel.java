package com.example.projectprogresstracker.Architecture;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projectprogresstracker.Entity.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
  TaskRepository taskRepository;
    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
    }

    public LiveData<List<Task>> getTaskLiveData(int id) {
        return taskRepository.getTaskLiveData(id);
    }
    public void insert(Task task){
        taskRepository.insert(task);
    }
}
