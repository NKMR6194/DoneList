package com.donelist;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import java.util.Calendar;
import java.util.Date;

import android.widget.TextView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        LinearLayout grandParent = (LinearLayout)findViewById(R.id.linearLayout1);
        TextView headText   = (TextView)findViewById(R.id.textNowMonth1);
        
        DoneTarget target = new DoneTarget(MainActivity.this);
        target.loadTarget();
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        
        CalendarButton calendarButton = new CalendarButton(grandParent, headText, calendar.getTime(), MainActivity.this);
        calendarButton.setButtonText();
        calendarButton.setListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    

}
