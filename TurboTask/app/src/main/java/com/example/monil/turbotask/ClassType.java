package com.example.monil.turbotask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monil on 4/28/2017.
 */

public class ClassType {
    private String className;

    ClassType(String name)
    {
        className = name;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", className);
        return result;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

}
