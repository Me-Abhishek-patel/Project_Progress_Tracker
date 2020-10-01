package com.example.projectprogresstracker.Architecture;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.projectprogresstracker.Entity.Project;
import com.example.projectprogresstracker.data.ProjectDao;
import com.example.projectprogresstracker.data.ProjectDataBase;

import java.util.List;

public class ProjectRepository {
    private ProjectDao projectDao;
    private LiveData<List<Project>> allProjects;

    public ProjectRepository(Application application){
        ProjectDataBase projectDataBase = ProjectDataBase.getInstance(application);
        projectDao = projectDataBase.projectDao();
        allProjects = projectDao.getAllProjects();
    }

    public LiveData<List<Project>> getAllProjects() {
        return allProjects;
    }

    public void insert(Project project) {
        new AddNewProjectAsyncTask(projectDao).execute(project);
    }

    public void update(Project project) {
        new updateAsyncTask(projectDao).execute(project);
    }

    private static class AddNewProjectAsyncTask extends AsyncTask<Project, Void, Void> {
        private ProjectDao projectDao;
        private AddNewProjectAsyncTask(ProjectDao projectDao) {
            this.projectDao = projectDao;
        }
        @Override
        protected Void doInBackground(Project... projects) {
            projectDao.AddNewProject(projects[0]);
            return null;
        }

    }

    private static class updateAsyncTask extends AsyncTask<Project, Void, Void> {
        private ProjectDao projectDao;

        private updateAsyncTask(ProjectDao projectDao) {
            this.projectDao = projectDao;
        }

        @Override
        protected Void doInBackground(Project... projects) {
            projectDao.UpdateProject(projects[0]);
            return null;
        }
    }
    }
