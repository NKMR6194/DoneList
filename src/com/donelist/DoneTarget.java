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
    }
    
    public void loadTarget(){
        list = new ArrayList<String>();
        
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
                
            }
            else{
                setDoneTarget(false);
            }
        }
        else{
            String createTableQuery = "create table " + tableName + " (item char(10), setting text);";
            database.execSQL(createTableQuery);
            setDoneTarget(false);
        }
        
        database.close();
    }
    
    public void setDoneTarget(boolean inputMiss){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        if(inputMiss){
            dialog.setMessage("設定に失敗しました\nもう一度入力してください");
        }
        else{
            dialog.setMessage("あなたが記録したい事を教えてください\n(設定で追加や編集ができます)");
        }
        
        editText = new EditText(activity);
        layout.addView(editText);
        dialog.setView(layout);
        dialog.setPositiveButton("決定", new DialogClickListener());
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