package com.tmsai.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.kakao.auth.Session;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        /* 앱의 시작점이 로그인 Activity가 아닌 다른 Activity(예를 들어 로딩 화면 Activity 등)라면,
           로그인 Activity가 아닌 시작점인 Activity의 onCreate 안에 checkAndImplicitOpen()를 넣어주어야 한다.
         */
        Session.getCurrentSession().checkAndImplicitOpen(); //자동로그인

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}
