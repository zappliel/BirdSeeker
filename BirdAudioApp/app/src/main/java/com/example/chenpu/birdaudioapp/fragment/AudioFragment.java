package com.example.chenpu.birdaudioapp.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.*;

import com.example.chenpu.birdaudioapp.R;
import com.example.chenpu.birdaudioapp.SearchResultActivity;

public class AudioFragment extends Fragment {
    private static final int REQUEST_PERMISSION_CODE = 1000;
    private static final String UPLOAD_URL = "http://192.168.110.100:8080/api/audio/upload"; // 修改为实际Spring Boot服务器地址

    private Button recordButton, stopButton, playButton, uploadButton, resetButton;
    private TextView statusText,resultText;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFilePath;
    private ImageView recordImageView;
    private SearchView searchView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_audio, container, false);

        recordButton = rootView.findViewById(R.id.recordButton);
        stopButton = rootView.findViewById(R.id.stopButton);
        playButton = rootView.findViewById(R.id.playButton);
        uploadButton = rootView.findViewById(R.id.uploadButton);
        statusText = rootView.findViewById(R.id.statusText);
        recordImageView = rootView.findViewById(R.id.recordImage);
        resetButton = rootView.findViewById(R.id.resetButton);
        searchView = rootView.findViewById(R.id.audio_search_view);
        resultText = rootView.findViewById(R.id.resultText);

        // 申请录音权限
        if (!checkPermissions()) {
            requestPermissions();
        }

        // 录音按钮
        recordButton.setOnClickListener(v -> startRecording());

        // 停止录音按钮
        stopButton.setOnClickListener(v -> stopRecording());

        // 播放按钮
        playButton.setOnClickListener(v -> playRecording());

        // 上传按钮
        uploadButton.setOnClickListener(v -> uploadAudio());

        //音频图片
        recordImageView.setOnClickListener(v -> startRecording());

        //重置按钮
        resetButton.setOnClickListener(v -> resetRecording());

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

        return rootView;
    }

    // 申请权限
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
    }

    // 开始录音
    private void startRecording() {
        try {
            File audioDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
            if (audioDir != null) {
                audioFilePath = audioDir.getAbsolutePath() + "/recorded_audio.3gp";
            }

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // 录音操作代码
            mediaRecorder.prepare();
            mediaRecorder.start();
            statusText.setText("录音中...");
            recordButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 停止录音
    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;

            // 文件保存路径
            File internalAudioDir = requireContext().getFilesDir();  // 获取应用的内部存储目录
            File savedAudioFile = new File(internalAudioDir, "saved_recorded_audio.wav");

            statusText.setText("录音完成: " + savedAudioFile.getAbsolutePath());
            stopButton.setVisibility(View.GONE);
            playButton.setVisibility(View.VISIBLE);
            uploadButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.VISIBLE);
        }
    }

    // 播放录音
    private void playRecording() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioFilePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            resultText.setVisibility(View.GONE);
            statusText.setVisibility(View.VISIBLE);
            statusText.setText("播放中...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 上传录音文件到服务器
    private void uploadAudio() {
        File audioFile = new File(audioFilePath);
        if (!audioFile.exists()) {
            Toast.makeText(requireContext(), "录音文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        RequestBody fileBody = RequestBody.create(MediaType.parse("audio/*"),audioFile);
        requestBody.addFormDataPart("file", audioFile.getName(), fileBody);

        Request request = new Request.Builder()
                .url(UPLOAD_URL)
                .post(requestBody.build())
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                System.out.println(response);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String responseData = response.body().string();
                        requireActivity().runOnUiThread(() -> handleServerResponse(responseData));
                    }

                    // 上传成功后，删除临时文件
                    if (audioFile.exists()) {
                        boolean deleted = audioFile.delete();
                        if (deleted) {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireContext(), "文件已删除", Toast.LENGTH_SHORT).show()
                            );
                        } else {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(requireContext(), "文件删除失败", Toast.LENGTH_SHORT).show()
                            );
                        }
                    }
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "上传失败", Toast.LENGTH_SHORT).show()
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "上传失败", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    // 处理服务器返回的数据
    private void handleServerResponse(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");

            if (code == 200) { // 成功
                JSONObject result = jsonObject.getJSONObject("result");
//                String label = result.getString("label");
//                Double score = result.getDouble("score");
//                statusText.setVisibility(View.GONE);
//                resultText.setVisibility(View.VISIBLE);
//                resultText.setText("音频的预测结果标签为：" + label + "，得分：" + score);


                String name = result.getString("name");
                String introduce = result.getString("introduce");
                String pictureUrl = result.getString("picture");
                String protonym = result.getString("protonym");
                String chinese = result.getString("chinese");
                String geo = result.getString("geo");

                // 更新 UI
                statusText.setVisibility(View.GONE);
                resultText.setVisibility(View.VISIBLE);
                resultText.setText("识别结果:" + chinese + "\n" + name + "\n" + protonym  + "\n主要" + geo);

            } else {
                Toast.makeText(requireContext(), "识别失败: " + msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "数据解析失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetRecording() {
        statusText.setText("点击按钮或图标开始录音\n保持外界安静，靠近音源，识别更准确~");
        statusText.setVisibility(View.VISIBLE);
        resultText.setVisibility(View.GONE);

        recordButton.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.GONE);
        uploadButton.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);
    }

    // 处理权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (!checkPermissions()) {
                Toast.makeText(requireContext(), "权限被拒绝，无法录音", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        }
    }
}