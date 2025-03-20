package com.example.chenpu.birdaudioapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chenpu.birdaudioapp.db.UserDbHelper;
import com.example.chenpu.birdaudioapp.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateEmailActivity extends AppCompatActivity {

    private EditText update_new_email;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_email);

        findViewById(R.id.email_back_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update_new_email = findViewById(R.id.update_new_email);

        findViewById(R.id.update_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_email = update_new_email.getText().toString();
                if(TextUtils.isEmpty(new_email)){
                    Toast.makeText(UpdateEmailActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    Pattern pattern = Pattern.compile(EMAIL_REGEX);
                    Matcher matcher = pattern.matcher(new_email);
                    if(matcher.matches()){
                        User user_info = User.getTmpUserInfo();
                        if (user_info != null) {
                            int i = UserDbHelper.getInstance(UpdateEmailActivity.this).updateEmail(user_info.getUsername(), new_email);
                            if (i > 0) {
                                Toast.makeText(UpdateEmailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UpdateEmailActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(UpdateEmailActivity.this,"请输入有效的邮箱信息",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}