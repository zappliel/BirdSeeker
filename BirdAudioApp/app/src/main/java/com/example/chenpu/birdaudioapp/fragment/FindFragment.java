package com.example.chenpu.birdaudioapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenpu.birdaudioapp.R;
import com.example.chenpu.birdaudioapp.SearchResultActivity;
import com.example.chenpu.birdaudioapp.adapter.BirdAdapter;
import com.example.chenpu.birdaudioapp.entity.BirdShortInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.*;

public class FindFragment extends Fragment {


    private RecyclerView recyclerView;
    private BirdAdapter birdAdapter;
    private List<BirdShortInfo> birdList = new ArrayList<>();
    private TextView refreshButton;
    private static final String API_URL = "http://192.168.110.100:8080/api/bird/six";
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_find, container, false);


        recyclerView = rootView.findViewById(R.id.birdRecyclerView);
        refreshButton = rootView.findViewById(R.id.refreshButton);
        searchView = rootView.findViewById(R.id.find_search_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        birdAdapter = new BirdAdapter(getContext(), birdList);
        recyclerView.setAdapter(birdAdapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // 初次加载数据
        loadBirdData();

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

        // 点击刷新按钮
        refreshButton.setOnClickListener(v -> loadBirdData());

        return rootView;
    }

    private void loadBirdData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    parseBirdData(responseData);
                }
            }
        });
    }

    private void parseBirdData(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray resultArray = jsonObject.getJSONArray("result");
            birdList.clear();

            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject birdObject = resultArray.getJSONObject(i);
                String chinese = birdObject.getString("chinese");
                String picture = birdObject.getString("picture");

                birdList.add(new BirdShortInfo(chinese, picture));
            }

            // 更新UI
            getActivity().runOnUiThread(() -> birdAdapter.notifyDataSetChanged());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}