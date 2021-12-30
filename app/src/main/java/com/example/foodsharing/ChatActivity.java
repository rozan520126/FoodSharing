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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsharing.databinding.ActivityChatBinding;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import org.w3c.dom.Text;

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
    ImageView image, send;
    EditText input;
    ListView msgList;

    String ReceiverUid, ReceiverName, uId;
    Uri imageUri;

    private FirebaseListAdapter<ChatMessage> adapter;
    FirebaseAuth mAuth;
    Query queryP, queryU;
    String pId;

    DatabaseReference databaseReferenceM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSharedPreferences("Msg", MODE_PRIVATE);


        Intialize();

        //取得post ID



        mAuth = FirebaseAuth.getInstance();
        uId = mAuth.getCurrentUser().getUid();

        //Suppose you want to retrieve "chats" in your Firebase DB:
        Query query = FirebaseDatabase.getInstance().getReference("Messages");
        //The error said the constructor expected FirebaseListOptions - here you create them:
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.item_container_sent_message)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
                TextView text = (TextView) v.findViewById(R.id.text_message);
                TextView time = (TextView) v.findViewById(R.id.text_datetime);

                String Otime = String.valueOf(model.getTime());
                text.setText(model.getMessage());
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.setTimeInMillis(Long.parseLong(Otime));
                String Ntime = DateFormat.format("HH:mm", calendar).toString();
                time.setText(Ntime);
            }
        };
        msgList.setAdapter(adapter);

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
                    } catch (Exception e) {
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
                for (DataSnapshot ds : snapshot.getChildren()) {
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

    private void sendMsg() {
        String msg = input.getText().toString();
        String uid = mAuth.getCurrentUser().getUid();

        if (TextUtils.isEmpty(msg)) {

        } else {
            FirebaseDatabase.getInstance()
                    .getReference("Messages")
                    .push()
                    .setValue(new ChatMessage(msg, uid, "ff"));
            input.setText("");
        }
    }

    private void Intialize() {
        send = findViewById(R.id.send);
        input = findViewById(R.id.input);

        title = findViewById(R.id.pTitleTv);
        image = findViewById(R.id.pImageIv);
        msgList = findViewById(R.id.message);

        Intent intent = getIntent();
        pId = intent.getStringExtra(EXTRA_PID);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}