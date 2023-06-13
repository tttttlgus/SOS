package com.example.snowex01;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AlarmRecevier extends BroadcastReceiver {
/////////////////////
    FirebaseAuth mFirebaseAuth;
    DatabaseReference myRef;
    private String muid = null;

    private String searchedUid;
    ////////////////////

    public AlarmRecevier(){ }

    NotificationManager manager;
    NotificationCompat.Builder builder;

    //오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동함
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";


    @Override
    public void onReceive(Context context, Intent intent) {
        String fEmail = intent.getDataString();
        Log.d("TAG", fEmail);

        mFirebaseAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("FirebaseEmailAccount");
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        muid = firebaseUser.getUid();

        DatabaseReference usersRef = myRef.child("userAccount");
        Query searchQuery = usersRef.orderByChild("email").equalTo(fEmail);
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        searchedUid = userSnapshot.getKey(); // 친구의 UID 가져오기
                        DatabaseReference userFriendRef = myRef.child("userFriend").child(muid).child(searchedUid);
                        userFriendRef.child("bool").setValue(false);
                    }
                } else {
                    searchedUid = null;
                    System.out.println("No results found for query: " + fEmail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Search error: " + databaseError.getMessage());
            }
        });
        /////////////////////////////////////

        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        builder = null;
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }

        //알림창 클릭 시 어떤 activity 화면을 부를지
        Intent intent2 = new Intent(context, ShareenterActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,101,intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        //알림창 제목
        builder.setContentTitle(fEmail+ "과 위치 공유를 마칩니다.");
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        //알림창 터치시 자동 삭제
        builder.setAutoCancel(true);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        manager.notify(1,notification);

    }
}