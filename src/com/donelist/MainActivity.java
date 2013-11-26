package com.donelist;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Calendar;
import java.util.Date;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    CalendarButton calendarButton;
    
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
        
        calendarButton = new CalendarButton(grandParent, headText, calendar.getTime(), MainActivity.this);
        calendarButton.setButtonText();
        calendarButton.setListener();
        
        ImageButton addDoneButton;
        addDoneButton = (ImageButton)findViewById(R.id.buttonAdd);
        addDoneButton.setOnClickListener(new AddDoneButtonListener());
    }
    
    @Override
    protected void onResume(){
        super.onResume();
        calendarButton.setButtonColor();
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private class AddDoneButtonListener implements OnClickListener{
        public void onClick(View v){
            Calendar calen = Calendar.getInstance();
            new DoneAdderDialog(MainActivity.this, calen.get(Calendar.YEAR), calen.get(Calendar.MONTH), calen.get(Calendar.DATE), new ReloadColor());
        }
    }
    
    protected class ReloadColor extends AddResult{
        void reload(){
            calendarButton.setButtonColor();
        }
    }

}

