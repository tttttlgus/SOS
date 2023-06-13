package com.example.snowex01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //DBHelper 객체 생성
        String dbName = "Address.db";
        int dbVersion =1;
        final DBHelper dbHelper;
        dbHelper = new DBHelper(this,dbName,null,dbVersion);


        final EditText name = findViewById(R.id.editName);
        final EditText phone = findViewById(R.id.editPhone);
        Button saveBtn = findViewById(R.id.btnSave);
       // Button cancel_btn = findViewById(R.id.detail_ib_cancel);
       // Button back_btn = findViewById(R.id.detail_ib_back);

        // final String titleStr = title.getText().toString();
        // final String contentsStr =contents.getText().toString();

        // saveBtn 을 누르면 데이터 베이스에 들어간다.
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString();
                String phoneStr = phone.getText().toString();

                //데이터 추가
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String sql = String.format("INSERT INTO Address VALUES(NULL,'%s','%s');", nameStr, phoneStr);
                db.execSQL(sql);
                // 저장 된다면 toast 메세지 같은 거 띄우기
                Toast.makeText(getApplicationContext(),"저장되었습니다.",Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(DetailActivity.this,PListActivity.class);
                startActivity(intent);
                finish();

            }
        });

        // cancel_btn 을 누르면 이 액티비티 끝내고 main 화면 돌아간다.
/*        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,MemoMainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/


        // back_btn 을 누르면 이 액티비티 끝내고 main 화면 돌아간다.
/*        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(DetailActivity.this,MemoMainActivity.class);
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


/*    @Override
    // 뒤로가기 버튼을 누르면, Alert 뜨고 이 액티비티 없애고 Main 으로 돌아가게.
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);

        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DetailActivity.this, PListActivity.class);
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
    }*/

    @Override
    protected void onPause() {
        // 작성 중인 메모 contents 저장 해 놓아야 한다.
        super.onPause();
    }
}