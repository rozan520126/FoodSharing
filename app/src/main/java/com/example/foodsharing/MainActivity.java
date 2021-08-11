package com.example.foodsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter myAdapter;
    private LinkedList<HashMap<String,String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        doData();

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    //RecyclerView測試的假資料
    private  void doData(){
        data =new LinkedList<>();
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


    //
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


}