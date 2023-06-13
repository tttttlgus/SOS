package com.example.snowex01;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //DBHelper 객체 생성
        String dbName = "Address.db";
        int dbVersion =1;
        final DBHelper dbHelper;
        dbHelper = new DBHelper(this,dbName,null,dbVersion);

        Intent intent = getIntent();
        final String got_name = intent.getStringExtra("name");
        String got_phone = intent.getStringExtra("phone");
        int got_positon = intent.getIntExtra("position",0);

        final EditText name = findViewById(R.id.editName);
        final EditText phone = findViewById(R.id.editPhone);
        Button saveBtn = findViewById(R.id.btnSave);
       // Button cancel_btn = findViewById(R.id.detail_ib_cancel);
       // Button back_btn = findViewById(R.id.detail_ib_back);

        name.setText(got_name);
        phone.setText(got_phone);



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이 데이터 값을 바꿔줘야 한다.
                // 받아온 title 로  조회해서 바꾼다.
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String final_name = name.getText().toString();
                String final_phone = phone.getText().toString();
                String sql = "UPDATE Address SET name='"+final_name+"',phone='"+final_phone+"'WHERE name='"+got_name+"';";
                db.execSQL(sql);

                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);

                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(EditActivity.this, PListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setMessage("수정되었습니다 :)");
                builder.setTitle("알림");
                builder.show();

            }
        });

/*        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메인으로 다시
                Intent intent = new Intent(EditActivity.this, MemoMainActivity.class);
                finish();
                startActivity(intent);
            }
        });*/

/*        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditActivity.this,MemoMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setMessage("이전 화면으로 돌아가겠습니까?");
                builder.setTitle("작성 중인 내용이 삭제됩니다.");
                builder.show();

            }

        });*/

    }

    void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);

        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(EditActivity.this, PListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.setMessage("이전 화면으로 돌아가겠습니까?");
        builder.setTitle("작성 중인 내용이 삭제됩니다.");
        builder.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        // 여기다가 원하는 처리를 작성하면 된다.
        Intent intent = new Intent(EditActivity.this, PListActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


}