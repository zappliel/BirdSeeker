package com.example.chenpu.birdaudioapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenpu.birdaudioapp.AppInfoActivity;
import com.example.chenpu.birdaudioapp.LoginActivity;
import com.example.chenpu.birdaudioapp.NoticeActivity;
import com.example.chenpu.birdaudioapp.PrivacyActivity;
import com.example.chenpu.birdaudioapp.R;
import com.example.chenpu.birdaudioapp.SearchResultActivity;
import com.example.chenpu.birdaudioapp.UpdateEmailActivity;
import com.example.chenpu.birdaudioapp.UpdatePasswordActivity;
import com.example.chenpu.birdaudioapp.entity.User;


public class HomeFragment extends Fragment {
    private SearchView mSearchView;

    private View rootView;
    private TextView tv_username;
    private TextView tv_email;
    private androidx.appcompat.widget.SearchView searchView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home,container,false);

        tv_username = rootView.findViewById(R.id.tv_username);
        tv_email = rootView.findViewById(R.id.tv_email);
        searchView = rootView.findViewById(R.id.home_search_view);

        rootView.findViewById(R.id.out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("提示")
                        .setMessage("确定退出登录吗？")
                        .setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        rootView.findViewById(R.id.home_update_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdatePasswordActivity.class);
                startActivityForResult(intent,1000);
            }
        });

        rootView.findViewById(R.id.home_update_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateEmailActivity.class);
                startActivityForResult(intent,1000);
            }
        });

        rootView.findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrivacyActivity.class);
                startActivityForResult(intent,1000);
            }
        });

        rootView.findViewById(R.id.about_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppInfoActivity.class);
                startActivityForResult(intent,1000);
            }
        });

        rootView.findViewById(R.id.notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                startActivityForResult(intent,1000);
            }
        });

        // 设置查询文本监听器
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
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

        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        User user_info = User.getTmpUserInfo();
        if(user_info != null){
            tv_username.setText(user_info.getUsername());
            tv_email.setText(user_info.getEmail());
        }
    }
}