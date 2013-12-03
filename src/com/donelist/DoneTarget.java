package com.donelist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DoneTarget{
    private List<String> list;
    private Activity activity;
    private final String tableName;
    private final String databasePath;
    private EditText editText;
    
    public DoneTarget(Activity thisActivity){
        activity = thisActivity;
        tableName = "setting";
        databasePath = "data/data/" + activity.getPackageName() + "/diary.db";
        list = new ArrayList<String>();
    }
    
    public boolean loadTarget(){
        boolean result;
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
        
        String findTableQuery = "select * from sqlite_master where type='table' and name='" + tableName + "';";
        Cursor findTableCursor = database.rawQuery(findTableQuery, null);
        
        if(findTableCursor.getCount() == 1){
            String query = "select * from " + tableName + " where item='doneTarget';";
            Cursor cursor = database.rawQuery(query, null);
            
            if(cursor.getCount() >= 1){
                int columnIndex = cursor.getColumnIndex("setting");
                
                cursor.moveToFirst();               
                do{
                    list.add( cursor.getString(columnIndex) );
                }while(cursor.moveToNext());
                
                result =  true;
            }
            else{
                result =  false;
            }
        }
        else{
            String createTableQuery = "create table " + tableName + " (item char(10), setting text);";
            database.execSQL(createTableQuery);
            result =  false;
        }
        
        database.close();
        return result;
    }
    
    public void setDoneTarget(boolean inputMiss){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        dialog.setTitle("目標");
        
        if(inputMiss){
            dialog.setMessage("設定に失敗しました\nもう一度入力してください");
        }
        else{
            dialog.setMessage("記録したい事を入力してください\n(設定で追加や編集ができます)");
        }
        
        editText = new EditText(activity);
        layout.addView(editText);
        dialog.setView(layout);
        dialog.setPositiveButton("決定", new DialogClickListener());
        dialog.setNegativeButton("戻る", null);
        dialog.show();
    }
    
    class DialogClickListener implements DialogInterface.OnClickListener{
        public void onClick(DialogInterface dialog, int i){
            String tmpTarget = editText.getText().toString();
            
            if(tmpTarget.length() == 0){
                setDoneTarget(true);
            }
            else{
                list.add(tmpTarget);
                
                SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databasePath, null);
                String insert = "insert into " + tableName + " values ('doneTarget', '" + tmpTarget + "');";
                database.execSQL(insert);
                database.close();
            }
        }
    }
    
    public List<String> getTargetList(){
        return list;
    }
}