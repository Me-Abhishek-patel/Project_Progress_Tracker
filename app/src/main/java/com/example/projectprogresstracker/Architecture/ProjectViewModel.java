package com.example.projectprogresstracker.Architecture;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.projectprogresstracker.Entity.Project;

import java.util.List;

public class ProjectViewModel extends AndroidViewModel {
   ProjectRepository projectRepository;
   LiveData<List<Project>> projectModels;
    public ProjectViewModel(@NonNull Application application) {
        super(application);
        projectRepository = new ProjectRepository(application);
        projectModels = projectRepository.getAllProjects();
    }
    public void insert(Project project){
        projectRepository.insert(project);
    }
    public void update(Project project){
        projectRepository.update(project);
    }
    public LiveData<List<Project>> getProjectModels() {
        return projectModels;
    }
}
