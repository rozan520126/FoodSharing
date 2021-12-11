package com.example.foodsharing;


import static com.example.foodsharing.Home.EXTRA_PID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

import models.Post;
import server.server_post;

public class FoodContent extends AppCompatActivity {
    FirebaseAuth mAuth;
    Query queryP;

    String uid;
    private Uri pImageUri,uImageUri;

    ImageView pImgIv,uImgIv;
    TextView pTitleTv,pDesTv,uNameTv,pTimeTv,pLocTv;
    Button delete,message,iWant;

    Toolbar editToolbar;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_content);
        Intialize();

        //firebase
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        //取得post ID
        Intent intent = getIntent();
        String pId = intent.getStringExtra(EXTRA_PID);

        setSupportActionBar(editToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iWant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(FoodContent.this,ChatActivity.class);
                chatIntent.putExtra(EXTRA_PID,pId);
                startActivity(chatIntent);
            }
        });

        //取得post資料
        queryP = new server_post().getQuery(pId);
        queryP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //get data
                    String pImg = "" + ds.child("pImage").getValue();
                    String pTitle = "" + ds.child("pTitle").getValue();
                    String pDes = "" + ds.child("pDes").getValue();
                    String pTime = "" + ds.child("pTime").getValue();
                    String pLoc = "" + ds.child("pLocation").getValue();
                    String uName = "" + ds.child("uName").getValue()+" , ";
                    String uImg = "" + ds.child("uImage").getValue();
                    String postUid = "" +ds.child("uid").getValue();

                    pImageUri = Uri.parse(pImg);
                    try {
                        Picasso.get().load(pImageUri).into(pImgIv);
                    }catch (Exception e){
                        //射程預設照案
                        Picasso.get().load(R.drawable.ic_default_image).into(pImgIv);
                    }
                    uImageUri = Uri.parse(uImg);
                    try {
                        Picasso.get().load(uImageUri).into(uImgIv);
                    }catch (Exception e){
                        //射程預設照案
                        Picasso.get().load(R.drawable.ic_default_image).into(pImgIv);
                    }
                    //convert timestamp to dd/mm/yyyy hh:mm am/pm
                    Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTime));
                    String time = DateFormat.format("yyyy/MM/dd aa hh"+"點"+"發布",calendar).toString();
                    pTitleTv.setText(pTitle);
                    pDesTv.setText(pDes);
                    pTimeTv.setText(time);
                    pLocTv.setText(pLoc);
                    uNameTv.setText(uName);

                    if(uid.equals(postUid)){
                        iWant.setVisibility(View.GONE);
                    }else {
                        delete.setVisibility(View.GONE);
                        message.setVisibility(View.GONE);
                        iWant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(FoodContent.this,ChatActivity.class));
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit,menu);
        return true;
    }

    private void Intialize(){
        pImgIv = findViewById(R.id.pImage);
        pTitleTv = findViewById(R.id.pTitle);
        pDesTv = findViewById(R.id.pDes);
        pTimeTv = findViewById(R.id.pTime);
        pLocTv = findViewById(R.id.pLoc);
        uImgIv = findViewById(R.id.uImg);
        uNameTv = findViewById(R.id.uName);

        delete = findViewById(R.id.delete);
        message = findViewById(R.id.message);
        iWant = findViewById(R.id.iWant);

        editToolbar = findViewById(R.id.toolbar_edit);
    }



}