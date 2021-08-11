package com.example.foodsharing;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Toolbar extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.toolbar_top);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("");

    }

    //產生Toolbar的Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu01,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.action_search){
            Toast.makeText(getApplicationContext(), "U click search", Toast.LENGTH_SHORT).show();
        }

        else if(id==R.id.action_chat){
            Toast.makeText(getApplicationContext(), "U click chat", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

}




