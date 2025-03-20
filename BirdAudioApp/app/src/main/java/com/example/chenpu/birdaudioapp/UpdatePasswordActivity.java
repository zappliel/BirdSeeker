package com.example.chenpu.birdaudioapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chenpu.birdaudioapp.db.UserDbHelper;
import com.example.chenpu.birdaudioapp.entity.User;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText update_new_password;
    private EditText update_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_password);

        findViewById(R.id.home_back_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update_new_password = findViewById(R.id.update_new_pwd);
        update_confirm_password = findViewById(R.id.update_confirm_pwd);

        findViewById(R.id.update_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_password = update_new_password.getText().toString();
                String confirm_pasword = update_confirm_password.getText().toString();
                if(TextUtils.isEmpty(new_password) || TextUtils.isEmpty((confirm_pasword))){
                    Toast.makeText(UpdatePasswordActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                }else if(! new_password.equals(confirm_pasword)){
                    Toast.makeText(UpdatePasswordActivity.this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                }else {
                    User user_info = User.getTmpUserInfo();
                    if(user_info!=null){
                        int i = UserDbHelper.getInstance(UpdatePasswordActivity.this).updatePwd(user_info.getUsername(), new_password);
                        if(i>0){
                            Toast.makeText(UpdatePasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(UpdatePasswordActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}