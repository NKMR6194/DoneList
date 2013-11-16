package com.donelist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class OneDay extends Activity{
    private int year, month, date;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_day);
        
        Intent intent = getIntent();
        year  = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        date  = intent.getIntExtra("date", 0); 
        
        TextView text = (TextView)findViewById(R.id.textNowDate);
        text.setText(year + "/" + (month+1) + "/" + date);
    }
}