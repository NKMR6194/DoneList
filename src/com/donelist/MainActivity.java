package com.donelist;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import java.util.Calendar;
import java.util.Date;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        LinearLayout grandParent = (LinearLayout)findViewById(R.id.linearLayout1);
        TextView headText   = (TextView)findViewById(R.id.textNowMonth1);
        TextView targetText = (TextView)findViewById(R.id.textDoneTarget);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        
        CalendarButton calendarButton = new CalendarButton(grandParent, headText, calendar.getTime(), MainActivity.this);
        calendarButton.setButtonText();
        calendarButton.setListener();
        
        Setting settings = new Setting();
        targetText.setText(settings.getTarget());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    class Setting{
        private String doneTarget;
        private SQLiteDatabase database;
        private /*final*/ String tableName;
        private EditText editText;
        
        public Setting(){
            String databasePath = "data/data/" + getPackageName() + "/diary.db";
            database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
            
            tableName = "setting";
            String findTableQuery = "select * from sqlite_master where type='table' and name='" + tableName + "';";
            Cursor findTableCursor = database.rawQuery(findTableQuery, null);
            
            
            if(findTableCursor.getCount() == 1){
                String query = "select * from " + tableName + " where item='doneTarget';";
                Cursor cursor = database.rawQuery(query, null);
                
                if(cursor.getCount() == 1){
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex("setting");
                    doneTarget = cursor.getString(columnIndex);
                }
                else{
                    setDoneTarget();
                }
            }
            else{
                String createTableQuery = "create table " + tableName + " (item char(10), setting text);";
                database.execSQL(createTableQuery);
                setDoneTarget();
            }
        }
        
        public void setDoneTarget(){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            LinearLayout layout = new LinearLayout(MainActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            
            editText = new EditText(MainActivity.this);
            dialog.setMessage("あなたが記録したい事を教えてください");
            layout.addView(editText);
            dialog.setView(layout);
            dialog.setPositiveButton("決定", new DialogClickListener());
            dialog.show();
        }
        
        class DialogClickListener implements DialogInterface.OnClickListener{
            public void onClick(DialogInterface dialog, int i){
                doneTarget = editText.getText().toString();
                String insert = "insert into " + tableName + " values ('doneTarget', '" + doneTarget + "');";
                database.execSQL(insert);           
            }
        }
        
        public String getTarget(){
            return doneTarget;
        }
    }

}
