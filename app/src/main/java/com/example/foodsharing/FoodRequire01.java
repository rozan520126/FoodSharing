package com.example.foodsharing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class FoodRequire01 extends AppCompatActivity {

    private View mToolbar;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_require01);

        mToolbar =findViewById(R.id.toolbar00);
        setSupportActionBar((androidx.appcompat.widget.Toolbar) mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }
}