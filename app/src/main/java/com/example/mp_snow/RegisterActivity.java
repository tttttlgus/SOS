package com.example.mp_snow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //파이어 베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터 베이스
    private EditText idEmail, idPwd;
    private Button nBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef  = FirebaseDatabase.getInstance().getReference();

        idEmail = findViewById(R.id.id_email);
        idPwd = findViewById(R.id.id_pwd);
        nBtnRegister = findViewById(R.id.btn_register);

        nBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //회원가입 처리 시작
                String strEmail = idEmail.getText().toString();
                String strPwd = idPwd.getText().toString();

                //Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser fisebaseUser = mFirebaseAuth.getCurrentUser();
                            UserAccount account = new UserAccount();
                            account.setIdToken(fisebaseUser.getUid());
                            account.setEmailId(fisebaseUser.getEmail());
                            account.setPassword(strPwd);

                            mDatabaseRef.child("UserAccount").child(fisebaseUser.getUid()).setValue(account);
                            Toast.makeText(RegisterActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}