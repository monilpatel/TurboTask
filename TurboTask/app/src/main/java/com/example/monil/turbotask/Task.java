package com.example.monil.turbotask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monil on 4/23/2017.
 */

public class Task {
    private String uid;
    private String name;
    private String date;
    private String className;
    private int priority;

    public Task(){}
    public Task(String uid, String name, String date, String className, int priority){
        this.uid = uid;
        this.name = name;
        this.date = date;
        this.className = className;
        this.priority = priority;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("date", date);
        result.put("className", className);
        result.put("priority", priority);
        return result;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }



}
