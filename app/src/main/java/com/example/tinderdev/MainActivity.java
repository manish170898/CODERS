package com.example.tinderdev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.tinderdev.Fragments.HomeFragment;
import com.example.tinderdev.Fragments.MessegesFragment;
import com.example.tinderdev.Fragments.NotificationsFragment;
import com.example.tinderdev.Fragments.ProfileFragment;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    private ChipNavigationBar chipNavigationBar;
    private Fragment fragment =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.chipNavigation);
        chipNavigationBar.setItemSelected(R.id.home, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.notifications:
                        fragment = new NotificationsFragment();
                        break;

                    case R.id.messeges:
                        fragment = new MessegesFragment();
                        break;

                    case R.id.profile:
                        fragment = new ProfileFragment();
                        break;
                }
                 if(fragment!=null){
                     getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
                 }
            }
        });

    }
}


