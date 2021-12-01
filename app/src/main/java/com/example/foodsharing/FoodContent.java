package com.example.foodsharing;

import static com.example.foodsharing.Home.EXTRA_NAME;
import static com.example.foodsharing.Home.EXTRA_PDES;
import static com.example.foodsharing.Home.EXTRA_PIMG;
import static com.example.foodsharing.Home.EXTRA_PLOC;
import static com.example.foodsharing.Home.EXTRA_PTIME;
import static com.example.foodsharing.Home.EXTRA_PTITLE;
import static com.example.foodsharing.Home.EXTRA_UIMG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class FoodContent extends AppCompatActivity {
    ImageView pImgIv,uImgIv;
    TextView pTitleTv,pDesTv,uNameTv,pTimeTv,pLocTv;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_content);

       Toolbar editToolbar = findViewById(R.id.toolbar_edit);

        setSupportActionBar(editToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String pImg = intent.getStringExtra(EXTRA_PIMG);
        String pTitle = intent.getStringExtra(EXTRA_PTITLE);
        String pDes = intent.getStringExtra(EXTRA_PDES);
        String uName = intent.getStringExtra(EXTRA_NAME);
        String uImg = intent.getStringExtra(EXTRA_UIMG);
        String pTime = intent.getStringExtra(EXTRA_PTIME);
        String pLoc = intent.getStringExtra(EXTRA_PLOC);

        pImgIv = findViewById(R.id.pImage);
        pTitleTv = findViewById(R.id.pTitle);
        pDesTv = findViewById(R.id.pDes);
        pTimeTv = findViewById(R.id.pTime);
        pLocTv = findViewById(R.id.pLoc);
        uImgIv = findViewById(R.id.uImg);
        uNameTv = findViewById(R.id.uName);

        Glide.with(this)
                .load(pImg)
                .into(pImgIv);
        Glide.with(this)
                .load(uImg)
                .into(uImgIv);
        pTitleTv.setText(pTitle);
        pDesTv.setText(pDes);
        pTimeTv.setText(pTime);
        pLocTv.setText(pLoc);
        uNameTv.setText(uName);



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