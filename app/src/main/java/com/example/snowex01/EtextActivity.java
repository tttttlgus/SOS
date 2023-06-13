package com.example.snowex01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EtextActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    EditText emer;
    String text;
    String id = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etext);

        emer = findViewById(R.id.tvEmer);
        Button modfBtn = findViewById(R.id.BtnModf);

        //DBHelper 객체 생성
        String dbName = "Address.db";
        int dbVersion = 1;

        final DBHelper dbHelper = new DBHelper(this,dbName,null,dbVersion);
        try{
            db = dbHelper.getWritableDatabase();
        }catch (SQLiteException ex){
            db = dbHelper.getReadableDatabase();
        }

        try {
            db.execSQL("insert into EmerText values('"+text+"');");
            db.close();
           // Cursor cursor = db.rawQuery("SELECT Etext FROM EmerText",null);
            //emer.setText(cursor.getString(0));

        }catch (Exception e) {
            Log.v("OUT", "아무것도 입력시키지 못함");
        }

        Cursor cursor = db.rawQuery("select * from EmerText", null);
        cursor.moveToNext();
        text = cursor.getString(0);
        emer.setText(text);

        modfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = emer.getText().toString();
                String sql = "UPDATE EmerText SET Etext ='"+ text +"';";
                //WHERE _id = '"+ id +"'
                db.execSQL(sql);

                AlertDialog.Builder builder = new AlertDialog.Builder(EtextActivity.this);
                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setMessage("수정되었습니다 :)");
                builder.setTitle("알림");
                builder.show();

            }
        });
    }
}