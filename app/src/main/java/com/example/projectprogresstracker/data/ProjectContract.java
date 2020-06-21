package com.example.projectprogresstracker.data;

import android.provider.BaseColumns;

public class ProjectContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ProjectContract() {
    }

    /* Inner class that defines the table contents */
    public static class ProjectEntry implements BaseColumns {
        public static final String TABLE_NAME = "Projects";
        public static final String TASK_TABLE_NAME = "Tasks";
        public static final String ACTIVITY_TABLE_NAME = "Activities";
        public static final String _ID = BaseColumns._ID;
        public static final String TASK_ID = BaseColumns._ID;
        public static final String ACTIVITY_ID = BaseColumns._ID;
        public static final String COLUMN_PROJECT_NAME = "name";
        public static final String COLUMN_TASK_NAME = "task_name";
        public static final String COLUMN_ACTIVITY_NAME = "activity_name";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";
        public static final String COLUMN_TASK_END_DATE = "task_end_date";
        public static final String COLUMN_ACTIVITY_END_DATE = "activity_end_date";
        public static final String COLUMN_PROJECT_PROGRESS = "progress";
        public static final String COLUMN_TASK_PROGRESS = "task_progress";
        public static final String COLUMN_ACTIVITY_PROGRESS = "activity_progress";
        public static final String COLUMN_BUDGET = "budget";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TASK_DESCRIPTION = "task_description";
        public static final String COLUMN_ACTIVITY_DESCRIPTION = "activity_description";
        public static final String COLUMN_START_POINT = "start_point";
        public static final String COLUMN_END_POINT = "end_point";
        public static final String COLUMN_TASK_PROJECT_ID = "task_project_id";
        public static final String COLUMN_ACTIVITY_TASK_ID = "activity_task_id";

    }

}
