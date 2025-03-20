package com.example.chenpu.birdaudioapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.chenpu.birdaudioapp.fragment.AudioFragment;
import com.example.chenpu.birdaudioapp.fragment.DailyFragment;
import com.example.chenpu.birdaudioapp.fragment.FindFragment;
import com.example.chenpu.birdaudioapp.fragment.HomeFragment;
import com.example.chenpu.birdaudioapp.fragment.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private DailyFragment dailyFragment;
    private MapFragment mapFragment;
    private AudioFragment audioFragment;
    private FindFragment findFragment;

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.BottomNavigator);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.find){
                    selectFragment(0);
                }else if(item.getItemId()==R.id.home){
                    selectFragment(1);
                }else if(item.getItemId()==R.id.daily){
                    selectFragment(2);
                }else if(item.getItemId()==R.id.audio){
                    selectFragment(3);
                }else {
                    selectFragment(4);
                }
                return true;
            }
        });
        selectFragment(0);
    }

    private void selectFragment(int position){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(fragmentTransaction);

        if(position == 0){
            if(findFragment == null){
                findFragment = new FindFragment();
                fragmentTransaction.add(R.id.content,findFragment);
            }else {
                fragmentTransaction.show(findFragment);
            }
        }else if(position == 1){
            if(homeFragment == null){
                homeFragment = new HomeFragment();
                fragmentTransaction.add(R.id.content,homeFragment);
            }else {
                fragmentTransaction.show(homeFragment);
            }
        }else if(position == 2){
            if(dailyFragment == null){
                dailyFragment = new DailyFragment();
                fragmentTransaction.add(R.id.content,dailyFragment);
            }else {
                fragmentTransaction.show(dailyFragment);
            }
        }else if(position == 3){
            if(audioFragment == null){
                audioFragment = new AudioFragment();
                fragmentTransaction.add(R.id.content,audioFragment);
            }else {
                fragmentTransaction.show(audioFragment);
            }
        }else{
            if(mapFragment == null){
                mapFragment = new MapFragment();
                fragmentTransaction.add(R.id.content,mapFragment);
            }else {
                fragmentTransaction.show(mapFragment);
            }
        }

        fragmentTransaction.commit();

    }

    private void hideFragment(FragmentTransaction fragmentTransaction){
        if(findFragment != null){
            fragmentTransaction.hide(findFragment);
        }

        if(homeFragment != null){
            fragmentTransaction.hide(homeFragment);
        }

        if(dailyFragment != null){
            fragmentTransaction.hide(dailyFragment);
        }

        if(mapFragment != null){
            fragmentTransaction.hide(mapFragment);
        }

        if(audioFragment != null){
            fragmentTransaction.hide(audioFragment);
        }
    }
}