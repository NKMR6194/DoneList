package com.donelist;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
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
        }
        
        int dateMax = calendar.getActualMaximum(Calendar.DATE);
        int firstDayWeek = calendar.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY - 1;
        for(int date=1; date<=dateMax; date++){
            button[date+firstDayWeek].setText("" + date);
            button[date+firstDayWeek].setEnabled(true);
        }
        
        monthText.setText(calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1));
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
    
}