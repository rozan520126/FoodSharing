package com.example.foodsharing;

import static com.example.foodsharing.Home.EXTRA_PID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsharing.databinding.ActivityChatBinding;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import models.ChatMessage;
import server.server_post;
import server.server_user;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private PreferenceManager preferenceManager;

    TextView title;
    ImageView image,send;
    EditText input;
    RecyclerView msgList;

    String ReceiverUid,ReceiverName,uId,nowDate,nowTime;
    Uri imageUri;

    FirebaseAuth mAuth;
    Query queryP,queryU;

    DatabaseReference databaseReferenceM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSharedPreferences("Msg",MODE_PRIVATE);


        Intialize();

        //取得post ID
        Intent intent = getIntent();
        String pId = intent.getStringExtra(EXTRA_PID);

        title = findViewById(R.id.pTitleTv);
        image = findViewById(R.id.pImageIv);
        msgList = findViewById(R.id.message);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();


        queryP = new server_post().getQuery(pId);
        queryP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //get data
                    String pImg = "" + ds.child("pImage").getValue();
                    String pTitle = "" + ds.child("pTitle").getValue();
                    String uId = "" + ds.child("uid").getValue();

                    imageUri = Uri.parse(pImg);
                    try {
                        Picasso.get().load(imageUri).into(image);
                    }catch (Exception e){
                        //射程預設照案
                        Picasso.get().load(R.drawable.ic_default_image).into(image);
                    }
                    title.setText(pTitle);
                    ReceiverUid = uId;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        queryU = new server_user().getQuery(uId);
        queryU.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    String uName = "" + ds.child("name").getValue();
                    ReceiverName = uName;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });

    }
    List keyList;
    private void sendMsg() {
        String msg = input.getText().toString();
        String uid = mAuth.getCurrentUser().getUid();
        long time = new Date().getTime();


        if (TextUtils.isEmpty(msg)){
            input.setText("dd");
        }else {
//            FirebaseDatabase.getInstance()
//                    .getReference()
//                    .push()
//                    .setValue(new ChatMessage(msg,uid));
            input.setText("");
        }
    }
    private void Intialize(){
        send = findViewById(R.id.send);
        input = findViewById(R.id.input);
    }
}