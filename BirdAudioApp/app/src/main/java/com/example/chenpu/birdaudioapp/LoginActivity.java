package com.example.chenpu.birdaudioapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chenpu.birdaudioapp.db.UserDbHelper;
import com.example.chenpu.birdaudioapp.entity.User;

public class LoginActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private CheckBox cb;

    private boolean is_remember;

    private SharedPreferences newSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        cb = findViewById(R.id.remember);

        newSharedPreferences = getSharedPreferences("user",MODE_PRIVATE);

        //remember or not
        is_remember = newSharedPreferences.getBoolean("is_remember",false);
        if(is_remember){
            String username = newSharedPreferences.getString("username",null);
            String password = newSharedPreferences.getString("password",null);
            et_username.setText(username);
            et_password.setText(password);
            cb.setChecked(true);
        }

        //sign in button
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"输入的用户名或密码为空",Toast.LENGTH_SHORT).show();
                }else {
                    User user_info = UserDbHelper.getInstance(LoginActivity.this).login(username);
                    if(user_info != null) {
                        if (password.equals(user_info.getPassword())) {
                            //记住密码
                            SharedPreferences.Editor edit = newSharedPreferences.edit();
                            edit.putBoolean("is_remember",is_remember);
                            edit.putString("username",username);
                            edit.putString("password",password);
                            edit.commit();

                            User.setTmpUserInfo(user_info);

                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "输入的用户名或密码有误", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "未查询到有效的账号", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //register link
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //remember password
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_remember = isChecked;
            }
        });
    }
}