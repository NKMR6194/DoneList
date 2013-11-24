package com.donelist;

import android.app.Activity;

public class Names{
    private Activity activity;
    private final String databasePath;
    
    Names(Activity act){
        activity = act;
        databasePath = "data/data/" + activity.getPackageName() + "/diary.db";
    }
    
    public String getDatabasePath(){
        return databasePath;
    }
    
    public String getTableName(int year, int month){
        return "data" + String.format("%4d%2d", year, month);
    }
}