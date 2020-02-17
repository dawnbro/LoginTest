package com.tmsai.logintest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

public class AccountActivity extends AppCompatActivity {

    String strNickname, strProfile, strEmail, strAgeRange, strGender, strBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ImageView ivProfile = findViewById(R.id.ivProfile);
        TextView tvNickname = findViewById(R.id.tvNickname);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnSignout = findViewById(R.id.btnSignout);

        TextView tvEmail = findViewById(R.id.tvEmail);
        TextView tvAgeRange = findViewById(R.id.tvAgeRange);
        TextView tvGender = findViewById(R.id.tvGender);
        TextView tvBirthday = findViewById(R.id.tvBirthday);

        Intent intent = getIntent();
        strNickname = intent.getStringExtra("name");
        strProfile = intent.getStringExtra("profile");
        strEmail = intent.getStringExtra("email");
        strAgeRange = intent.getStringExtra("ageRange");
        strGender = intent.getStringExtra("gender");
        strBirthday = intent.getStringExtra("birthday");

        tvNickname.setText(strNickname);
        Glide.with(this).load(strProfile).into(ivProfile);

        tvEmail.setText(strEmail);
        tvAgeRange.setText(strAgeRange);
        tvGender.setText(strGender);
        tvBirthday.setText(strBirthday);

        /* 로그아웃 버튼 클릭리스너 처리
         */
        btnLogout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });

        /* 회원탈퇴 버튼 클릭리스너 처리
         */
        btnSignout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AccountActivity.this)
                        .setMessage("탈퇴하시겠습니까?")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    /* onFailure() : 회원탈퇴 실패 시 동작하는 함수. ErrorResult 값을 받아온다.
                                                     ErrorResult.getErrorCode()로 어떤 에러가 발생했는지 알 수 있다.
                                                     인터넷 연결이 원활하지 않아서 발생하는 오류도 여기에 해당된다.
                                                     인터넷 연결 문제인 경우 ErrorResult.getErrorCode() 값이 ApiErrorCode.CLIENT_ERROR_CODE
                                     */
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        int result = errorResult.getErrorCode();

                                        if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                            Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    /* onSessionClosed() : 세션이 닫혔을 때 작동하는 함수.
                                                           로그인 자체가 안 되어 있는 경우.
                                     */
                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    /* onNotSignedUp() : 가입되지 않은 카카오 계정이 회원탈퇴를 요구하는 경우 동작하는 함수
                                                         로그인 및 회원가입 프로세스를 잘못 구현할 경우 발생할 수 있는 오류.
                                                         로그인 및 회원가입 프로세스를 정상적으로 구현했을 경우 이 오류가 등장할 가능성은 매우 낮다.
                                                         만약 회원탈퇴 시 이 오류가 발생한다면, 로그인이나 회원탈퇴 소스 코드 중 잘못 구현한 부분이 있는지를 살펴보아야 한다
                                     */
                                    @Override
                                    public void onNotSignedUp() {
                                        Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    /* onSuccess() : 회원탈퇴 성공 시 동작하는 함수.
                                     */
                                    @Override
                                    public void onSuccess(Long result) {
                                        Toast.makeText(getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }
}