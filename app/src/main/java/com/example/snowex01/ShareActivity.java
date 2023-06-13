package com.example.snowex01;

import static android.content.ContentValues.TAG;
import static java.sql.DriverManager.println;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity {

    ////DB UID 정보 가져오기
    FirebaseAuth mFirebaseAuth;
    DatabaseReference myRef;
    private String muid = null;

    public String searchedUid;
    int i = 0;
    int count = 0;


///////////////친구 list 구현
    ArrayList<Friend> fArrayList = new ArrayList<>();
    ListView listView;
    String[] id = new String[3];

    public interface FirebaseCallback {
        void onResponse(String name);
    }
/////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_list);

        listView = (ListView)findViewById(R.id.listview);

        mFirebaseAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("FirebaseEmailAccount");
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        muid = firebaseUser.getUid();

        Button frndIdBtn = (Button) findViewById(R.id.useridBtn);
        frndIdBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), FrndIdSearchActivity.class);
                startActivity(intent);
            }
        });
        Button settBtn = (Button) findViewById(R.id.settingBtn);
        settBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), PListActivity.class);
                startActivity(intent);
            }
        });



        ///토글버튼은 다중마커를 켤지 말지를 결정하게 될 듯
        //다중마커는,
        //상대방의 위치를 가져오는 메서드 실행 여부를  토글버튼의 t/f 여부로 판단
        //sqlite에 토글버튼의 t/f여부를 저장해서 currentLocation 들어갈 때 sqlite DB에 저장된 t/f값 가져와서 마커 표시여부 선택
        readFirebaseName(new FirebaseCallback() {
            @Override
            public void onResponse(String name) {
                id[i] = name;
                Friend m = new Friend(id[i], false);
                //Log.d("TAG!!!!!!!"+i, id[i]);
                fArrayList.add(m);
                i++;

                ///main안에서 변수가지고 뭔가를 하기는 하기는 힘들 듯,,,
                // 이 안에서 최대한 해결,,,
                if(id[2] != null){      //현재 친구 갯수를 알고 있기 때문에 설정할 수 있었음,, 친구갯수를 count로 먼저 가져와서 id[2]자리에 id[count]로 하면 될 듯
                    //Log.d("TAG!!!!!!!"+i, Integer.toString(count));
                    load();
                }
            }
        });
    }

/////list에 adapter 연결
    public void load(){
            FriendAdapter mAdapter = new FriendAdapter(fArrayList,this);
            listView.setAdapter(mAdapter);
            //Log.d("실행!!!!!!!!!!!!!!!!!!!","실행!!!!!!!!!!!!!!!!!!!");
    }

    ////firebase는 비동기식
    ///전역 변수를 통해서 함수 밖에서 값 확인 불가, interface와 callback method 사용필요

    ////내 친구목록을 firebase에서 가져오는 과정
    public void readFirebaseName(FirebaseCallback callback) {
        DatabaseReference userFdRef = myRef.child("userFriend").child(muid);
        Query getQuery = userFdRef.orderByKey();
        getQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        //searchedUid = userSnapshot.getKey();
                        //count ++;
                        String id = userSnapshot.child("frndEmail").getValue(String.class);
                        //Log.d("자식 갯수!!!!!!!!!!!!!!!", Integer.toString(count));
                        callback.onResponse(id);
                    }

                } else {
                    System.out.println("No results found for query: ");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Search error: " + databaseError.getMessage());
            }
        });
    }

    public void goToSharetextActivity(View view) {
        Intent intent = new Intent(this, SharetextActivity.class);
        startActivity(intent);
    }
}
