package com.example.snowex01;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MessageIntent extends AppCompatActivity {
    private TextView messageTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_intent);

        // 메시지를 전달받기 위해 Intent에서 데이터를 가져옵니다.
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");

        messageTextView = findViewById(R.id.message_textview);
        messageTextView.setText(message);
    }
}