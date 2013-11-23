package com.donelist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OneDay extends Activity{
    private int year, month, date;
    private String databasePath;
    private String tableName;
    private List<Done> done;
    //private DoneAdderDialog addDialog;
    
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_day);
        
        databasePath = "data/data/" + getPackageName() + "/diary.db";
        done = new ArrayList<Done>();
        
        Intent intent = getIntent();
        year  = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        date  = intent.getIntExtra("date", 0); 
        
        TextView text = (TextView)findViewById(R.id.textNowDate);
        text.setText(year + "/" + (month+1) + "/" + date);
        
        tableName = "data" + String.format("%4d%2d", year, month);
        
        if(loadDone() == false){
            new DoneAdderDialog(OneDay.this, year, month, date, new AfterAdd());
        }
        
        LinearLayout layout = (LinearLayout)findViewById(R.id.oneDayLayout);
        
        for(int i=0; i<done.size(); i++){
            TextView doneText = new TextView(this);
            doneText.setText( done.get(i).getTarget() );
            layout.addView(doneText);
        }

    }
    
    public boolean loadDone(){
        boolean bool;
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
        
        String findTableQuery = "select * from sqlite_master where type='table' and name='" + tableName + "';";
        Cursor findTableCursor = database.rawQuery(findTableQuery, null);
        
        if(findTableCursor.getCount() == 1){
            String query = "select * from " + tableName + " where date = " + date + "";
            Cursor cursor = database.rawQuery(query, null);
            
            if(cursor.getCount() >= 1){
                int columnIndex = cursor.getColumnIndex("done");
                
                cursor.moveToFirst();               
                do{
                    done.add( new Done(cursor.getString(columnIndex)) );
                }while(cursor.moveToNext());
                
                bool = true;
            }
            else{
                //addDialog = 
                //new DoneAdderDialog(OneDay.this, year, month, date);
                bool = false;
            }
            
        }
        else{
            String createTableQuery = "create table " + tableName + " (date int, done char(10));";
            database.execSQL(createTableQuery);
            //addDialog = 
            //new DoneAdderDialog(OneDay.this, year, month, date);
            bool = false;
        }
        
        database.close();
        
        return bool;
    }
    
    private class Done{
        private String target;
        
        public Done(String string){
            target = string;
        }
        
        public String getTarget(){
            return target;
        }
        
    }
    
    private class AfterAdd extends AddResult{
        public void reload(){
            loadDone();
            LinearLayout layout = (LinearLayout)findViewById(R.id.oneDayLayout);
            layout.removeAllViews();
        
            for(int i=0; i<done.size(); i++){
                TextView doneText = new TextView(OneDay.this);
                doneText.setText( done.get(i).getTarget() );
                layout.addView(doneText);
            }
        }
        
       
    }
    
}