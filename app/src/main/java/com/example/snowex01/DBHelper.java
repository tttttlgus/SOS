package com.example.snowex01;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {


    //생성자 - database 파일을 생성한다.
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB 처음 만들 때 한번만 호출 : 테이블 생성 (제목-title, 내용-contents)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Address (id INTEGER PRIMARY KEY AUTOINCREMENT," + "name String, phone String);");
        db.execSQL("CREATE TABLE EmerText (Etext String);");
        db.execSQL("CREATE TABLE shareFNF (Friend String, Time String, ShowBool Integer);");
    //

    }

    // 버전 업데이트 되면 DB를 다시 만든다.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Address");
        db.execSQL("DROP TABLE IF EXISTS EmerText");
        db.execSQL("DROP TABLE IF EXISTS shareFNF");
        onCreate(db);
    }



}