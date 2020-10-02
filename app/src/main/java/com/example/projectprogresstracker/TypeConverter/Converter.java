package com.example.projectprogresstracker.TypeConverter;

import androidx.room.TypeConverter;

import com.example.projectprogresstracker.Entity.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converter {
    @TypeConverter
    public static ArrayList<Task> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Task> list) {
        Gson gson = new Gson();
        return  gson.toJson(list);
    }
}