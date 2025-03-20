package com.example.chenpu.birdaudioapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chenpu.birdaudioapp.R;
import com.example.chenpu.birdaudioapp.SearchResultActivity;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DailyFragment extends Fragment {

    private View rootView;
    private androidx.appcompat.widget.SearchView searchView;
    private TextView nameTextView, introduceTextView, protonymTextView, chineseTextView, geoTextView;
    private ImageView imageView;
    private static final String RANDOM_URL = "http://192.168.110.100:8080/api/bird/random";
    private List<String> introList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_daily, container, false);

        introList = new ArrayList<>();
        introList.add("如同翅膀展开的瞬间，你也将迎来新的机遇。勇敢飞翔，未来就在你脚下。");
        introList.add("今天，鸟儿在枝头歌唱，预示着你将收到来自远方的好消息。放松心情，迎接它的到来。");
        introList.add("就像自由飞翔的鸟儿，你的内心也正在寻求独立与解放。跟随直觉，敢于改变。");
        introList.add("黑鸟的飞翔提醒你，今天或许会遇到挑战，但只要坚持，你会看到曙光。");
        introList.add("轻盈的羽翼指引着你，今天是释放压力、放飞梦想的好时机。");
        introList.add("鸟儿在高空盘旋，象征着你未来的方向在变得更加清晰，坚定信念，朝着目标飞去。");
        introList.add("晨曦中的鸟儿展翅飞翔，预示着你的努力将在不久的将来开花结果。");
        introList.add("鸟儿鸣叫，唤醒了宁静的心灵。今天是你内心深处灵感爆发的一天。");
        introList.add("飞翔的鸟儿告诉你，虽然旅程遥远，但只要脚步坚定，终会到达理想的彼岸");
        introList.add("鸟儿停在枝头，静默片刻后再次飞起，提醒你：有时停下脚步，是为了更好地前行。");
        introList.add("飞鸟振翅，象征着心灵的净化。今天，你可能会获得一种新的视角，重新审视自己的人生。");

        searchView = rootView.findViewById(R.id.daily_search_view);
        // 初始化布局中的控件
        nameTextView = rootView.findViewById(R.id.nameTextView);
        introduceTextView = rootView.findViewById(R.id.introduceTextView);
        protonymTextView = rootView.findViewById(R.id.protonymTextView);
        chineseTextView = rootView.findViewById(R.id.chineseTextView);
        geoTextView = rootView.findViewById(R.id.geoTextView);
        imageView = rootView.findViewById(R.id.imageView);
        // 设置查询文本监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 当用户点击搜索按钮时，跳转到搜索结果页面
                if (!query.isEmpty()) {
                    // 创建 Intent，跳转到 SearchResultActivity
                    Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                    intent.putExtra("query", query); // 将查询文本传递给 SearchResultActivity
                    startActivity(intent);
                } else {
                    // 如果搜索内容为空，显示提示
                    Toast.makeText(getActivity(), "请输入搜索内容", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        fetchRandomBird();

        return rootView;
    }

    private void fetchRandomBird() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().build(); // 空的请求体，因为接口不需要任何参数

        Request request = new Request.Builder()
                .url(RANDOM_URL)
                .post(body)
                .build();

        // 发送网络请求
        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    // 获取响应体并解析
                    String responseBody = response.body().string();

                    // 更新 UI
                    getActivity().runOnUiThread(() -> {
                        parseAndDisplayData(responseBody);
                    });
                } else {
                    Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "请求出错", Toast.LENGTH_SHORT).show();
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

                introduce = introList.get(new Random().nextInt(introList.size()));

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
                Toast.makeText(getActivity(), "搜索失败: " + msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "数据解析失败", Toast.LENGTH_SHORT).show();
        }
    }
}