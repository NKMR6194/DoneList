package com.donelist;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarButton{
        
    private Calendar calendar;
    private Button[] button;
    private TextView monthText;
    private Context context;
    
    public CalendarButton(LinearLayout grandParent, TextView headText, Date showingDate, Context nowContext){
        calendar = Calendar.getInstance();
        calendar.setTime(showingDate);
        
        button = new Button[42];
        for(int i=1, count=0; i<grandParent.getChildCount(); i++){
            
            LinearLayout parent = (LinearLayout)grandParent.getChildAt(i);
            for(int j=0; j<parent.getChildCount(); j++){
                button[count] = (Button)parent.getChildAt(j);
                button[count].setGravity(Gravity.TOP);
                count++;
            }
            
        }
        
        monthText = headText;
        context = nowContext;
    }
    
    public void setButtonText(){
        for(int i=0; i<42; i++){
            button[i].setText("");
            button[i].setEnabled(false);
            //button[i].setBackgroundColor(Color.argb(50, 255, 255, 0));
        }
        
        int dateMax = calendar.getActualMaximum(Calendar.DATE);
        int firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY - 1;
        for(int date=1; date<=dateMax; date++){
            button[date+firstDayWeek].setText("" + date);
            button[date+firstDayWeek].setEnabled(true);
            
            /*
            if(checkDone(date) == true){
                button[date+firstDayWeek].setBackgroundColor(Color.argb(50, 255, 255, 0));
            }
            */
        }
        
        monthText.setText(calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1));
    }
    
    public void setButtonColor(){
        int dateMax = calendar.getActualMaximum(Calendar.DATE);
        int firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY - 1;
        
        for(int date=1; date<=dateMax; date++){
            if(checkDone(date) == true){
                button[date+firstDayWeek].setBackgroundColor(Color.argb(50, 255, 255, 0));
            }
            else{
                button[date+firstDayWeek].setBackgroundColor(Color.argb(0, 0, 0, 0));
            }
        }
    }
    
    public void setListener(){
        int dateMax = calendar.getActualMaximum(Calendar.DATE);
        int firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY - 1;
        for(int date=1; date<=dateMax; date++){
            button[date+firstDayWeek].setOnClickListener(new DateButtonListener(date));
        }
    }
    
    private class DateButtonListener implements OnClickListener{
        private int date;
        
        public DateButtonListener(int nowDate){
            date = nowDate;
        }
        
        public void onClick(View v){
            Intent intent = new Intent(context, OneDay.class);
            intent.putExtra("year", calendar.get(Calendar.YEAR));
            intent.putExtra("month", calendar.get(Calendar.MONTH));
            intent.putExtra("date", date);
            context.startActivity(intent);
        }
    }
    
    public boolean checkDone(int date){
            boolean bool;
            String databasePath = "data/data/" + context.getPackageName() + "/diary.db";
            String tableName = "data" + String.format("%4d%2d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
            
            String findTableQuery = "select * from sqlite_master where type='table' and name='" + tableName + "';";
            Cursor findTableCursor = database.rawQuery(findTableQuery, null);
            
            if(findTableCursor.getCount() == 1){
                String query = "select * from " + tableName + " where date = " + date + "";
                Cursor cursor = database.rawQuery(query, null);
                
                if(cursor.getCount() >= 1){
                    bool = true;
                }
                else{
                    bool = false;
                }
                
            }
            else{
                bool = false;
            }
            
            database.close();
            
            return bool;
    }
    
}