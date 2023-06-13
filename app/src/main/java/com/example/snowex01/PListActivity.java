package com.example.snowex01;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PListActivity extends AppCompatActivity {

    EditText emer;
    String text = "";
    ArrayList<Address> pArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plist);

        Button addBtn = findViewById(R.id.btnAdd);
        TextView aCount = findViewById(R.id.aCount);
        emer = findViewById(R.id.tvEmer);
        Button modfBtn = findViewById(R.id.BtnModf);
        emer.setText("도와주세요");

        //DBHelper 객체 생성
        String dbName = "Address.db";
        int dbVersion =1;
        final DBHelper dbHelper;
        // memoArrayList 에 데이터 값을 넣어서 가져와주기
        dbHelper = new DBHelper(this,dbName,null,dbVersion);
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final String sql = "SELECT * FROM Address;";
        Cursor cursor = db.rawQuery(sql,null);
        aCount.setText(String.valueOf(cursor.getCount()));   // 메모장 갯수 캐스팅
        try{
            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    // 데이터 베이스에 있는 값들의 string 값을 Memo 클래스 생성자에 담아서 만들어 준다.
                    Address m = new Address(cursor.getString(1),cursor.getString(2));
                    pArrayList.add(m);
                }
            }
        }finally {
            cursor.close();
        }

        cursor = db.rawQuery("select * from EmerText", null);
        cursor.moveToNext();
        if (cursor != null && cursor.getCount() != 0){
            text = cursor.getString(0);
            emer.setText(text);
            // cursor.close();
        }

        // 리스트 뷰 연결
        final ListView listView = findViewById(R.id.listView1);
        AddressAdapter mAdapter = new AddressAdapter(pArrayList,this);
        listView.setAdapter(mAdapter);

        // 리스트 뷰 내 아이템 클릭시 → Edit 액티비티로 넘어간다.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 리스트 뷰를 누를 때 EditActivity 로 넘어가야한다.
                // 이때 클릭된 값에 해당하는 title 과, contents 를 그대로 넘겨 줘야 한다.
                int check_position = listView.getCheckedItemPosition(); //리스트 뷰의 포지션을 가져옴.
                Address vo =(Address)parent.getAdapter().getItem(position);  //리스트 뷰의 포지션 가져옴.
                String name = vo.getName();
                String phone =vo.getPhone();

                Intent intent = new Intent(PListActivity.this, EditActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("position",position);

                startActivity(intent);
                finish();
            }
        });
        // 리스트 뷰 내 아이템 롱 클릭시 → AlertDialog 로 "삭제" 가 뜨고 "삭제"를 누를시에 그 아이템 삭제
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Address vo =(Address)parent.getAdapter().getItem(position);  //리스트 뷰의 포지션 가져옴.
                final String name = vo.getName();
                String phone = vo.getPhone();
                AlertDialog.Builder builder = new AlertDialog.Builder(PListActivity.this);

                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 메모 삭제 작업
                        String sql0 ="DELETE FROM Address WHERE name = '"+ name +"';";
                        db.execSQL(sql0);

                        Intent intent = new Intent(PListActivity.this, PListActivity.class);
                        finish();
                        startActivity(intent);

                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setMessage("삭제하겠습니까?");
                builder.setTitle("삭제 알림");
                builder.show();
                return true;
            }
        });


        // New 버튼 누를 시에 처리 → detail Activity 로 보내주고 거기서 데이터 추가하도록 함
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PListActivity.this, DetailActivity.class);
                finish();   //자꾸 쌓이는 것 방지.
                startActivity(intent);
            }
        });

        modfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("delete from EmerText");
                text = emer.getText().toString();
                db.execSQL("INSERT INTO EmerText VALUES ('" + text + "');");


                AlertDialog.Builder builder = new AlertDialog.Builder(PListActivity.this);
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