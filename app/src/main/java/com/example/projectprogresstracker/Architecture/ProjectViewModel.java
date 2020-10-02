package com.example.projectprogresstracker.Architecture;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projectprogresstracker.Entity.Project;
import com.example.projectprogresstracker.Entity.Task;

import java.util.List;

public class ProjectViewModel extends AndroidViewModel {
   ProjectRepository projectRepository;
   LiveData<List<Project>> projectModels;
    public ProjectViewModel(@NonNull Application application) {
        super(application);
        projectRepository = new ProjectRepository(application);

    }
    public void insert(Project project){
        projectRepository.insert(project);
    }
    public void insert(Task task){projectRepository.insert(task);}
    public void update(Project project){
        projectRepository.update(project);
    }
    public void delete(Task task){
        projectRepository.delete(task);
    }
    public void deleteAllProjects(){
        projectRepository.deleteAllProjects();
    }
public void delete(Project project){
        projectRepository.delete(project);
}
    public LiveData<List<Project>> getProjectModels() {
        return projectRepository.getAllProjects();
    }
    public LiveData<List<Task>> getTasks(int id){return projectRepository.getAllTasks(id);}
}
