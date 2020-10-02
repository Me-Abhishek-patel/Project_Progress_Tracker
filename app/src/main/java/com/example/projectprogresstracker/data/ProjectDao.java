package com.example.projectprogresstracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projectprogresstracker.Entity.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert
    void AddNewProject(Project project);

    @Delete
    void DeleteProject(Project project);

    @Update
    void UpdateProject(Project project);

    @Delete
    void delete(Project project);

    @Query("SELECT * FROM Project_Table")
    LiveData<List<Project>> getAllProjects();

    @Query("DELETE FROM Project_Table")
    void deleteAllProjects();
}
