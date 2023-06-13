package com.example.snowex01;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SharetextActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharefrom_text); //공유받기

        TextView msg = (TextView) findViewById(R.id.tv_location);


        //공유받은 대상의 위치가 *날짜까지 메인화면에 표시
        //*상대방이 설정한 나에게 위치를 공유할 기간..?
        // 상대방이 설정한 나에게 위치 공유할 기간을 firebase통해 알아야 되는건가

        Button backToMain = (Button) findViewById(R.id.btn_main);
        backToMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), CurrentLocation4.class);
                startActivity(intent);
            }
        });



        intent = getIntent();
        String ID = intent.getStringExtra("위치 공유할 ID");       //ShareEnterActivity에서 옴
        String Period = intent.getStringExtra("위치 공유 기간");       //ShareEnterActivity에서 옴
        msg.setText(ID+"와 "+ Period+ " 동안 실시간 위치를 공유합니다.");

        //String fID = intent.getStringExtra("위치를 확인할 ID");    //FriendAdapter에서 옴
        //msg.setText(fID+" 실시간 위치가 메인 화면에 표시됩니다.");


        //날짜 part구현 후 intent로 값 넘겨받아 setText


    }
}