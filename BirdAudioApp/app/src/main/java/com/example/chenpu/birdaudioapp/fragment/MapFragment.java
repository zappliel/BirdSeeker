package com.example.chenpu.birdaudioapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.example.chenpu.birdaudioapp.R;
import com.example.chenpu.birdaudioapp.SearchResultActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapFragment extends Fragment implements
        AMapLocationListener,LocationSource,AMap.OnMapClickListener, AMap.OnMapLongClickListener{

    private View rootView;
    private androidx.appcompat.widget.SearchView searchView;
    MapView mMapView = null;
    //地图控制器
    private AMap aMap = null;
    //位置更改监听
    private LocationSource.OnLocationChangedListener mListener;
    //请求权限码
    private static final int REQUEST_PERMISSIONS = 9527;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    //现在的经度
    private double current_ing;
    //现在的纬度
    private double curremt_lat;

    private ImageView refresh;

    private List<Marker> markerList = new ArrayList<>();

    private final String load_url = "http://192.168.110.100:8080/api/point/map";
    private final String insert_url = "http://192.168.110.100:8080/api/point/add";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        searchView = rootView.findViewById(R.id.map_search_view);
        refresh = rootView.findViewById(R.id.refresh_img);

        initLocation();

        initMap(savedInstanceState);

        checkingAndroidVersion();

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

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllPoints();
                // 在这里处理图片点击事件
                Toast.makeText(getActivity(), "已刷新", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        mLocationClient.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    private void checkingAndroidVersion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Android6.0及以上先获取权限再定位
            requestPermission();
        }else {
            //Android6.0以下直接定位
            mLocationClient.startLocation();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (EasyPermissions.hasPermissions(this.getActivity(), permissions)) {
            //true 有权限 开始定位
            Toast.makeText(getActivity(), "已获得权限", Toast.LENGTH_SHORT).show();

            mLocationClient.startLocation();
        } else {
            //false 无权限
            EasyPermissions.requestPermissions(this, "需要权限", REQUEST_PERMISSIONS, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //设置权限请求结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
            //关闭缓存机制，高精度定位会产生缓存。
            mLocationOption.setLocationCacheEnable(false);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
        }
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //地址
                String address = aMapLocation.getAddress();
                curremt_lat = aMapLocation.getLatitude();
                current_ing = aMapLocation.getLongitude();
                Toast.makeText(getActivity(), address, Toast.LENGTH_SHORT).show();

                mLocationClient.stopLocation();

                if(mListener != null){
                    mListener.onLocationChanged(aMapLocation);
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    private void initMap(Bundle savedInstanceState) {
        mMapView = rootView.findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mMapView.getMap();


        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);

        aMap.setOnMapClickListener(this);

        aMap.setOnMapLongClickListener(this);

        loadAllPoints();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient != null) {
            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getActivity(), "经度："+latLng.longitude+"，纬度："+latLng.latitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        createDialog(latLng,this.getActivity());
    }

    public void createDialog(LatLng latLng, Context context){
        curremt_lat = latLng.latitude;
        current_ing = latLng.longitude;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请输入您发现的鸟类");

        // 设置输入框
        final EditText input = new EditText(context);
        input.setPadding(40, 20, 40, 10);  // 可以调节与输入框的间距
        input.setHint("请输入鸟类的名字");
        builder.setView(input);

        builder.setMessage("您现在的经纬度是：\n"+current_ing+"\n"+curremt_lat);

        // 设置确定和取消按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取用户输入的文本
                String userInput = input.getText().toString();
                // 处理用户输入的内容
                insertNewPoint(latLng,userInput);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 取消操作
                dialog.dismiss();
            }
        });

        // 显示弹窗
        builder.show();
    }

    public void insertNewPoint(LatLng latLng,String name){
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).snippet(name));
        markerList.add(marker);

        double lat = latLng.latitude;
        double ing = latLng.longitude;

        OkHttpClient client = new OkHttpClient();

                // 构造 POST 请求的 Body 部分，使用 FormBody 传递参数
        FormBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("lat", String.valueOf(lat))
                .add("ing", String.valueOf(ing))
                .build();

                // 创建 Request 对象
        Request request = new Request.Builder()
                .url(insert_url) // 替换成实际的接口 URL
                .post(formBody)  // 发送 POST 请求
                .build();

        // 发送网络请求
        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(()->{
                        Toast.makeText(getActivity(), "感谢您的支持，录入已成功", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    getActivity().runOnUiThread(()->{
                        Toast.makeText(getActivity(), "录入失败", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(()-> {
                    Toast.makeText(getActivity(), "请求出错", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    public void loadAllPoints(){
        if (markerList != null && markerList.size()>0){
            for (Marker markerItem : markerList) {
                markerItem.remove();
            }
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(load_url)
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
                        makeAllMarkers(responseBody);
                    });
                } else {
                    getActivity().runOnUiThread(()->{
                        Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(()-> {
                    Toast.makeText(getActivity(), "请求出错", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    public void makeAllMarkers(String responseData){
        try {
            // 使用 Jackson ObjectMapper 解析 JSON
            JSONObject jsonObject = new JSONObject(responseData);
            int code = jsonObject.getInt("code");
            String msg = jsonObject.getString("msg");
            // 解析成功后处理数据
            if (code == 200) {
                JSONArray resultArray = jsonObject.getJSONArray("result");

                // 遍历 result 数组
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject point = resultArray.getJSONObject(i);

                    // 获取每个 Point 的字段
                    String name = point.getString("name");
                    String lat = point.getString("lat");
                    String ing = point.getString("ing");

                    LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(ing));

                    Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).snippet(name));
                    markerList.add(marker);

                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "数据解析失败", Toast.LENGTH_SHORT).show();
        }
    }
}