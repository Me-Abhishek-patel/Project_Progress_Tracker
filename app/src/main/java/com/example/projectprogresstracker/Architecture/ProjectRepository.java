package com.example.projectprogresstracker.Architecture;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.projectprogresstracker.Entity.Project;
import com.example.projectprogresstracker.Entity.Task;
import com.example.projectprogresstracker.data.ProjectDao;
import com.example.projectprogresstracker.data.ProjectDataBase;
import com.example.projectprogresstracker.data.TaskDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProjectRepository {
    private ProjectDao projectDao;
    private TaskDao taskDao;
    private LiveData<List<Project>> allProjects;
    private LiveData<List<Task>> Tasks;
    public LiveData<List<Task>> getAllTasks(final int id) {

        return taskDao.getTasksForProject(id);
    }

    public ProjectRepository(Application application){
        ProjectDataBase projectDataBase = ProjectDataBase.getInstance(application);
        projectDao = projectDataBase.projectDao();
        taskDao = projectDataBase.taskDao();
    }

    public LiveData<List<Project>> getAllProjects() {
        return projectDao.getAllProjects();
    }

    public void insert(Project project) {
        new AddNewProjectAsyncTask(projectDao).execute(project);
    }

    public void insert(final Task task){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.insert(task);
            }
        });
        }
    public void delete(final Task task){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.delete(task);
            }
        });
    }
    public void update(final Task task){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                taskDao.update(task);
            }
        });
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
    public void deleteAllProjects(){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                projectDao.deleteAllProjects();
            }
        });
    }
    public void delete(final Project project){
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                projectDao.delete(project);
            }
        });
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
