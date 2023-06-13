package com.example.snowex01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity1 extends AppCompatActivity {

    private Button btn_register_1;

    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증 관련
    private EditText et_email, et_pwd;
    private Button btn_login;
    int volumeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        btn_register_1 = findViewById(R.id.btn_register_1);
        btn_register_1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity1.this, RegisterActivity3.class);
            startActivity(intent);
        });

        mFirebaseAuth = FirebaseAuth.getInstance();

        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);

        et_email.setText("sunday@naver.com");
        et_pwd.setText("i123123");

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(v -> {
            String strEmail = et_email.getText().toString();
            String strPwd = et_pwd.getText().toString();

            mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(MainActivity1.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Intent intent = new Intent(MainActivity1.this, CurrentLocation4.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity1.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    ////////////// volume key 활용
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeCount++;
            Toast.makeText(this, ""+volumeCount, Toast.LENGTH_SHORT).show();
            if(volumeCount ==5){
                Toast.makeText(this, "System back 버튼 눌림", Toast.LENGTH_SHORT).show();
                volumeCount = 0;
                Intent intent = new Intent(this, SendMessage.class);
                startActivity(intent);
            }
            return true;
        }
        return false;
    }
    ////////////////////

}