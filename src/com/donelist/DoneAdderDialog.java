package com.donelist;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class DoneAdderDialog{
    private AlertDialog.Builder dialog;
    private Spinner spinner;
    private DoneTarget target;
    private DatePicker datePicker;
    private Activity parentActivity;
    private AddResult addResult;
    
    public DoneAdderDialog(Activity activity, int year, int month, int date, AddResult result){
        parentActivity = activity;
        
        target = new DoneTarget(activity);
        target.loadTarget();
        List<String> targetList = target.getTargetList();
        
        if(targetList.isEmpty() == false){
            addResult = result;
        
            dialog = new AlertDialog.Builder(activity);
            
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, targetList);
            
            LinearLayout layout = new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);
            
            spinner = new Spinner(activity);
            spinner.setAdapter(adapter);
            layout.addView(spinner);
            
            datePicker = new DatePicker(activity);
            datePicker.setCalendarViewShown(false);
            datePicker.updateDate(year, month, date);
            layout.addView(datePicker);
            
            dialog.setView(layout);
            
            dialog.setPositiveButton("決定", new DialogClickListener());
            dialog.setNegativeButton("戻る", null);   
            dialog.show();
        }
        else{
            Toast toast = Toast.makeText(activity, "目標が設定されていません", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }
    }
    
    class DialogClickListener implements DialogInterface.OnClickListener{
        public void onClick(DialogInterface dialog, int i){
            String tmpTarget = spinner.getSelectedItem().toString();
            
            int year  = datePicker.getYear();
            int month = datePicker.getMonth();
            int date  = datePicker.getDayOfMonth();
            
            String databasePath = "data/data/" + parentActivity.getPackageName() + "/diary.db";
            String tableName = "data" + String.format("%4d%2d", year, month);
            
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
            String insert = "insert into " + tableName + " (date, done) values (" + date + ", '" + tmpTarget +"');";
            //        "insert into " + tableName + " (date, done) values (, '" + tmpTarget + "');";
            database.execSQL(insert);
            database.close();
            
            addResult.reload();
        }
    }
    
}

abstract class AddResult{
    abstract void reload();
}