package com.example.snowex01;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ShareenterActivity extends AppCompatActivity {

    private String selectedFriend = ""; // 선택한 친구를 저장할 변수
    private String selectedPeriod = ""; // 선택한 공유 기간을 저장할 변수
    //공유 기간의 시간, 분을 따로 저장하기 위함 >> 알람 설정을 위해
    int mHour = 0;
    int mMinute = 0;

    private SQLiteDatabase db;


    //////////////위치 공유 시간 설정
    long mNow;
    Date mDate;
    private String testFr = "friday@naver.com";     //친구 id로 db 수정하기 위한 test값

    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    FirebaseAuth mFirebaseAuth;
    DatabaseReference myRef;
    private String muid = null;

    public String searchedUid;
    //////////

    ///firebase에서 친구 ID 조회
    int i = 0;
    Spinner spinner;

    ArrayList<String> ArrayList = new ArrayList<>();
    String[] id = new String[3];
    TextView tvSelectedPeriod;
    Calendar calenderInstance = Calendar.getInstance();
    /////////////////

////공유할 대상 가져오기 위함
    public interface FirebaseCallback {
        void onResponse(String name);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_enter);

        tvSelectedPeriod = findViewById(R.id.tv_selected_period);

////////////////// 위치 공유 시간 설정
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        mCalender = new GregorianCalendar();

        Log.v("HelloAlarmActivity", mCalender.getTime().toString());

        mFirebaseAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("FirebaseEmailAccount");
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        muid = firebaseUser.getUid();
////////////////////


        //DBHelper 객체 생성
        String dbName = "Address.db";
        int dbVersion =1;
        final DBHelper dbHelper = new DBHelper(this,dbName,null,dbVersion);
        try{
            db = dbHelper.getWritableDatabase();
        }catch (SQLiteException ex){
            db = dbHelper.getReadableDatabase();
        }
        //////////////////


///////공유할 대상 조회해서 spinner에 연결:  firebase의 자신의 친구목록에서 가져옴
        spinner = (Spinner) findViewById(R.id.spinner);
        readFirebaseName(new ShareenterActivity.FirebaseCallback() {
            @Override
            public void onResponse(String name) {
                id[i] = name;
                ArrayList.add(id[i]);
                Log.d("TAG!!!!!!!"+i, id[i]);
                i++;

                String[] items = ArrayList.toArray(new String[3]);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getApplicationContext(), android.R.layout.simple_spinner_item, items);

                ///main안에서 변수가지고 뭔가를 하기는 하기는 힘들 듯,,, 이 안에서 최대한 해결,,,
                if(id[2] != null){          //현재 친구 갯수를 알고 있기 때문에 설정할 수 있었음,, 수정이 필요한 부분!
                    // 스피너에 어댑터 설정
                    spinner.setAdapter(adapter);
                }
            }
        });



        ///DB 확인용...
        Button btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = spinner.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(), "선택한 친구: " + text, Toast.LENGTH_SHORT).show();
                Cursor cursor = db.rawQuery("SELECT * FROM shareFNF;",null);        //여기서부터는 shareFNF내용 확인용
                if (cursor != null && cursor.getCount() != 0) {
                    while (cursor.moveToNext()) {            //위치 공유할 친구 저장된 목록 조회
                        String fr = cursor.getString(0);
                        String tm = cursor.getString(1);
                        String y = cursor.getString(2);
                        Log.v("ID 조회", "("+ fr +", "+ tm+", " + y + ")");
                    }
                }
            }
        });


/*
        // 1번 코드: 친구 목록 다이얼로그를 열기 위한 클릭 리스너 설정
        TextView tvShareTarget = findViewById(R.id.tv_selected_friend);
        tvShareTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showFriendListDialog();
            }
        });
*/

        // 2번 코드: 공유 기간 선택 다이얼로그를 열기 위한 클릭 리스너 설정
        TextView tvSharePeriod = findViewById(R.id.tv_selected_period);
        tvSharePeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/////공유 시간 선택
                int hr = calenderInstance.get(Calendar.HOUR_OF_DAY);
                int min = calenderInstance.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(ShareenterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        onTimeListner, hr, min, true);
                timePickerDialog.setTitle("공유 기간");
                Objects.requireNonNull(timePickerDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();
////////////////////////////
            }
        });

    }


    TimePickerDialog.OnTimeSetListener onTimeListner = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (view.isShown()) {
                Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
                mHour = hourOfDay;
                mMinute =minute;
                selectedPeriod = hourOfDay + "시간 " + minute + "분";
                tvSelectedPeriod.setText(hourOfDay + "시간 " + minute + "분");
            }
        }
    };


////내 친구목록을 firebase에서 가져오는 과정
    public void readFirebaseName(ShareenterActivity.FirebaseCallback callback) {
        DatabaseReference userFdRef = myRef.child("userFriend").child(muid);
        Query getQuery = userFdRef.orderByKey();
        getQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String id = userSnapshot.child("frndEmail").getValue(String.class);
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


/*
    // 공유 기간을 선택할 수 있는 다이얼로그 생성
    private void showSharePeriodDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("공유 기간 선택")
                .setItems(R.array.share_periods, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] sharePeriods = getResources().getStringArray(R.array.share_periods);
                        String selectedPeriod = sharePeriods[which];
                        ShareenterActivity.this.selectedPeriod = selectedPeriod; // 변수 업데이트

                        updateSelectedValues();
                        Toast.makeText(getApplicationContext(), "선택한 기간: " + selectedPeriod, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("취소", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
*/


    //공유버튼 로직_ DB에 공유 대상과 기간저장, firebase에서 위치 공유 bool을 true로 변경
    public void goToSharetextActivity(View view) {
        selectedFriend = spinner.getSelectedItem().toString();
        //test위한 친구 id String
        //String time = testFr;
        String time = selectedPeriod;

        // 선택한 친구의 아이디에 해당하는 uid(즉, 친구의uid)를 타고 db의 userFriend에 접속해서 내 uid밑 친구 uid에서 bool을 true로 설정
        //노티가 울릴 때 bool이 false가 되도록 AlarmReceiver에 구현함

        DatabaseReference userFdRef = myRef.child("userAccount");    //친구의 uid를 가져와서 bool설정하는 과정
        Query getQuery = userFdRef.orderByChild("email").equalTo(selectedFriend);
        getQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        searchedUid = userSnapshot.getKey(); // 친구의 UID 가져오기

                        DatabaseReference userFriendRef = myRef.child("userFriend").child(muid).child(searchedUid);
                        userFriendRef.child("bool").setValue(true);
                    }
                } else {
                    searchedUid = null;
                    System.out.println("No results found for query: " + selectedFriend);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Search error: " + databaseError.getMessage());
            }
        });

        if(selectedFriend != "" && selectedPeriod != "" ){      //test할 때 db에는 friday로 저장되나, 공유할 대상: 항목은 비우지 말아야함
        /*
          * 위치 공유를 해야 테이블이 생성 ∴ 초기에는 테이블이 비어있음 >> if문으로 검사, 빈 테이블이면 새 행 삽입
          * 위치 공유 이력이 있는 경우 경우 shareFNF DB table에 데이터 존재
              > 현재 위치 공유하려는 상대방의 id가 테이블에 존재하는지 while문으로 검사
                  >> 존재하면 UPDATE~WHERE문 이용 (기한 테이블만 수정)
                  >> 존재하지 않으면 새로 입력(INSERT)
        */
            Cursor cursor = db.rawQuery("SELECT * FROM shareFNF;",null);
            if (cursor != null && cursor.getCount() != 0){                  //(sqlite)DB가 비어있지 않을 때
                Log.v("공유할 ID", "(" + selectedFriend + ")");
                String fr = "";
                while(cursor.moveToNext()) {          //위치 공유할 친구 저장된 목록 조회
                    fr = cursor.getString(0);
                    Log.v("ID 조회", "(" + fr + ")");
                    Log.v("공유 ID", "(" + selectedFriend + ")");
                    if(selectedFriend.equals(fr)){             // 기존 db에 존재하면 업데이트
                        System.out.println("equals ? " + selectedFriend.equals(fr));
                        String sql = "UPDATE shareFNF SET Time='"+time+"'WHERE Friend='"+selectedFriend+"';";
                        Log.v("공유 아이디와 기간 업데이트", "(" + selectedFriend + "," + time+ ")");
                        db.execSQL(sql);
                        break;
                    }
                }
                if(!selectedFriend.equals(fr)){       //기존 DB에 존재하는 ID가 아닌 경우
                    db.execSQL("INSERT INTO shareFNF VALUES ('" + selectedFriend + "','" + time + "','"+ 0 + "');");
                    Log.v("새 공유 아이디와 기간 저장(DB시작 이후로)", "(" + selectedFriend + "," + time+ ")");
                }
            } else{
                db.execSQL("INSERT INTO shareFNF VALUES ('" + selectedFriend + "','" + time + "','"+ 0 + "');");
                Log.v("DB 새로 시작, 공유 아이디와 기간 저장", "(" + selectedFriend + "," + time+ ")");
            }
////////////////////////// 알림 설정
            setAlarm();
///////////////////////////
            Intent intent = new Intent(this, SharetextActivity.class);
            intent.putExtra("위치 공유할 ID",selectedFriend);   //SharetextActivity로 위치 공유할 친구의 아이디 넘김
            intent.putExtra("위치 공유 기간",selectedPeriod);   //SharetextActivity로 위치 공유할 친구의 아이디 넘김
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "공유할 대상과 기간을 모두 입력하세요.", Toast.LENGTH_SHORT).show();
        }

    }


/////알림 함수
    private void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(ShareenterActivity.this, AlarmRecevier.class);
        receiverIntent.setData(Uri.parse(selectedFriend));  //uid는 intent로 안 넘어감,,ㅜㅜ 대신 ID(email)를 넘김
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ShareenterActivity.this, 0, receiverIntent, PendingIntent.FLAG_IMMUTABLE);

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);        //현재 시간으로 set

        // 테스트용 7초 뒤 공유 끊기 (공유시간 조절 가능)
        //calendar.add(Calendar.SECOND, 7);

        calendar.add(Calendar.MINUTE, mMinute);
        calendar.add(Calendar.HOUR, mHour);


        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),pendingIntent);
    }
}