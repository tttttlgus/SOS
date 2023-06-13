package com.example.snowex01;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SendMessage extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_SMS = 2;
    private Button emergencyButton;


    //연락처
    private SQLiteDatabase db;
    String[] phone;
    int pCount=0;
    String loadEmerMSG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmsg);

        emergencyButton = findViewById(R.id.msg_button);
        emergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSmsPermission();
            }
        });

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(SendMessage.this, MainActivity1.class);
            startActivity(intent);
        });


        //DBHelper 객체 생성
        String dbName = "Address.db";
        int dbVersion =1;
        final DBHelper dbHelper = new DBHelper(this,dbName,null,dbVersion);
        try{
            db = dbHelper.getWritableDatabase();
        }catch (SQLiteException ex){
            db = dbHelper.getReadableDatabase();
        }

        Cursor cursor = db.rawQuery("SELECT * FROM Address;",null);
        pCount = cursor.getCount();
        phone = new String[pCount];
        if (cursor != null && cursor.getCount() != 0) {
            cursor = db.rawQuery("SELECT * FROM Address;", null);
            int i = 0;
            while (cursor.moveToNext()) {
                phone[i] = cursor.getString(2);
                Log.v("OUT", "phone number (" + phone[i] + ")");
                i++;
            }
        }

        cursor = db.rawQuery("select * from EmerText", null);
        cursor.moveToNext();
        if (cursor != null && cursor.getCount() != 0){
            loadEmerMSG = cursor.getString(0);
        }

    }

    private void checkSmsPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_SMS);
        } else {
            checkLocationPermission();
        }
    }
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
        } else {
            sendEmergencyMessage();
        }
    }

    private void sendEmergencyMessage() {
        // 현재 위치 가져오기
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(SendMessage.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && locationManager != null) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // 위치 정보를 문자열로 변환
                String locationString = "위도: " + (float)latitude + ", 경도: " + (float)longitude;

                //현재 위치 지도에 표시한 URL
                String LoactionMapStr = "http://maps.google.com/maps?&z=18.5&q=" + latitude+ "," + longitude + "&ll=" + latitude+ "," + longitude;

                // 메시지 전송 코드
                String emergencyMessage = loadEmerMSG+ "\n"+ "긴급 상황입니다. 현재 위치는 " + locationString + " 입니다.";

                //메세지 전송
                for(int i =0; i < pCount ; i++){
                    sendSMS("+82"+phone[i], emergencyMessage);
                    sendSMS("+82"+phone[i], LoactionMapStr);
                }

                // 팝업 표시
                showPopup("메시지 전송 완료", emergencyMessage);

                // 새로운 Activity로 전환
                Intent intent = new Intent(SendMessage.this, MessageIntent.class);
                intent.putExtra("message", emergencyMessage);
                startActivity(intent);
            } else {
                Toast.makeText(SendMessage.this, "위치 정보를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SendMessage.this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "메시지 전송 완료", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "메시지 전송 실패", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showPopup(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
            } else {
                Toast.makeText(this, "문자 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendEmergencyMessage();
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}