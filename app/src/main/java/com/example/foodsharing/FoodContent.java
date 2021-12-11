package com.example.foodsharing;


import static com.example.foodsharing.Home.EXTRA_PID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class FoodContent extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String uid;
    private Uri pImageUri,uImageUri;

    ImageView pImgIv,uImgIv;
    TextView pTitleTv,pDesTv,uNameTv,pTimeTv,pLocTv;
    Button delete,message,iWant;
    MenuView.ItemView edit;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_content);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");
        //取得post ID
        Intent intent = getIntent();
        String pId = intent.getStringExtra(EXTRA_PID);

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

       Toolbar editToolbar = findViewById(R.id.toolbar_edit);
       edit = findViewById(R.id.action_edit);

        setSupportActionBar(editToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //取得post資料
        Query query = databaseReference.orderByChild("pId").equalTo(pId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
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
                    pTitleTv.setText(pTitle);
                    pDesTv.setText(pDes);
                    pTimeTv.setText(pTime);
                    pLocTv.setText(pLoc);
                    uNameTv.setText(uName);

                    if(uid.equals(postUid)){
                        iWant.setVisibility(View.GONE);
                    }else {
                        delete.setVisibility(View.GONE);
                        message.setVisibility(View.GONE);
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




}