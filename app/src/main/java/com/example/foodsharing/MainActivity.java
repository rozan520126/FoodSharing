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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainActivity.MyAdapter myAdapter;
    private LinkedList<HashMap<String,String>> data;
    private FirebaseAnalytics mFirebaseAnalytics;

    private BottomNavigationView mMainNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        recyclerView = findViewById(R.id.postsrecyclerView);

//        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(mLayoutManager);
//        setFragment(new Home());
        startActivity(new Intent(MainActivity.this,AddPost.class));
//        doData();
//
//        myAdapter = new MainActivity.MyAdapter();
//        recyclerView.setAdapter(myAdapter);



        mMainNav = findViewById(R.id.toolbar_bottom);
        mMainNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.p1_list:
                    setFragment(new Home());
                    break;
                case R.id.p2_shop:
                    setFragment(new Home());
                    break;
                case R.id.p3_post:
                    startActivity(new Intent(MainActivity.this,AddPost.class));
                    break;
                case R.id.p4_group:
                    setFragment(new Home());
                    break;
                case R.id.p5_me:
                    setFragment(new Home());
                    break;
            }

            return true;

        });
    }
    //RecyclerView測試的假資料
    private void doData(){
        data = new LinkedList<>();
        for (int i=0;i<10;i++){
            HashMap<String,String> row = new HashMap<>();
            int random01 =(int)(Math.random()*100);
            row.put("food_name01","Food_name:"+random01);
            int random02 =(int)(Math.random()*1000);
            row.put("poster","PPoster:"+random02);
            int random03 =(int)(Math.random()*100);
            row.put("food_name02","Food_name:"+random03);
            data.add(row);
        }
    }
    //宣告調變器Adapter
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View post_list_View_test;
            public TextView food_name01,food_name02,poster;


            public MyViewHolder(View v){
                super(v);
                post_list_View_test = v;

                food_name01 = post_list_View_test.findViewById(R.id.item_name01);
                food_name02 = post_list_View_test.findViewById(R.id.item_name02);
                poster = post_list_View_test.findViewById(R.id.item_poster);


            }
        }

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View post_list_View_test = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.postlist_test_recyclerview, parent, false);
            MyViewHolder vh =new MyViewHolder(post_list_View_test);

            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
            holder.food_name01.setText(data.get(position).get("food_name01"));
            holder.food_name02.setText(data.get(position).get("food_name02"));
            holder.poster.setText(data.get(position).get("poster"));
        }

        @Override
        public int getItemCount() {

            return data.size();
        }
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }



}