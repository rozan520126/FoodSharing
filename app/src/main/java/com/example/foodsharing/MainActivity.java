package com.example.foodsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView mMainNav;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment(new Home());

        mMainNav = findViewById(R.id.toolbar_bottom);
        mMainNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.p1_list:
                    setFragment(new Home());
                    break;
//                case R.id.p2_shop:
//                    setFragment(new Home());
//                    break;
                case R.id.p3_post:
                    startActivity(new Intent(MainActivity.this, AddPost.class));
                    break;
//                case R.id.p4_group:
//                    setFragment(new Home());
//                    break;
                case R.id.p5_me:
                    setFragment(new ProfileFragment());
                    break;
            }

            return true;

        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    private void checkUserStatus() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
        }
    }


}