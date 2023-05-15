package com.example.mp_snow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//https://www.youtube.com/watch?v=NJgolOfKcYE
public class MainActivity extends AppCompatActivity {

//    private FirebaseAuth mFirebaseAuth; //파이어 베이스 인증
//    private DatabaseReference mDatabaseRef; //실시간 데이터 베이스
//    private EditText idEmail, idPwd;
//    private Button nBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mDatabaseRef  = FirebaseDatabase.getInstance().getReference();
//
//        idEmail = findViewById(R.id.id_email);
//        idPwd = findViewById(R.id.id_pwd);
//        nBtnRegister = findViewById(R.id.btn_register);
    }
}