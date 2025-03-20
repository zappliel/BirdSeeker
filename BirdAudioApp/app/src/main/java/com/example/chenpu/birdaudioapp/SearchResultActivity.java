package com.example.chenpu.birdaudioapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchResultActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 1000;
    private static final String SEARCH_URL = "http://192.168.110.100:8080/api/bird/name";
    private TextView nameTextView, introduceTextView, protonymTextView, chineseTextView, geoTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);
        // 获取传递的搜索查询文本
        String query = getIntent().getStringExtra("query");

        findViewById(R.id.search_back_tool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 初始化布局中的控件
        nameTextView = findViewById(R.id.nameTextView);
        introduceTextView = findViewById(R.id.introduceTextView);
        protonymTextView = findViewById(R.id.protonymTextView);
        chineseTextView = findViewById(R.id.chineseTextView);
        geoTextView = findViewById(R.id.geoTextView);
        imageView = findViewById(R.id.imageView);

        fetchBirdInfo(query);

    }

    private void fetchBirdInfo(String query){
        FormBody.Builder params = new FormBody.Builder();
        params.add("name",query);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SEARCH_URL)
                .post(params.build())
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        // 获取响应数据
                        String responseData = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 解析返回的 JSON 数据
                                parseAndDisplayData(responseData);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SearchResultActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchResultActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }

    private void parseAndDisplayData(String responseData){
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");

            if (code == 200) { // 成功
                JSONObject result = jsonObject.getJSONObject("result");

                String name = result.getString("name");
                String introduce = result.getString("introduce");
                String pictureUrl = result.getString("picture");
                String protonym = result.getString("protonym");
                String chinese = result.getString("chinese");
                String geo = result.getString("geo");

                introduce = "简介：" + introduce;

                // 更新 UI
                nameTextView.setText(name);
                introduceTextView.setText(introduce);
                protonymTextView.setText(protonym);
                chineseTextView.setText(chinese);
                geoTextView.setText(geo);

                Glide.with(this)
                        .load(pictureUrl)
                        .into(imageView);

            } else {
                Toast.makeText(SearchResultActivity.this, "搜索失败: " + msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SearchResultActivity.this, "数据解析失败", Toast.LENGTH_SHORT).show();
        }
    }
}