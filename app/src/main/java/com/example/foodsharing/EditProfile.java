package com.example.foodsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity {

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String storagePath = "Users_Profile_Imgs/";

    //views
    ImageView myphoto;
    EditText nameEd,phoneEd,introEd;
    TextView emailTv,cancel,finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //init view
        myphoto = (ImageView) findViewById(R.id.myphoto);
        nameEd = (EditText) findViewById(R.id.nameEd);
        phoneEd = (EditText) findViewById(R.id.phoneEd);
        introEd = (EditText) findViewById(R.id.introEd);
        emailTv = (TextView)findViewById(R.id.emailTv);

        //button
        cancel = (TextView) findViewById(R.id.cancel);
        finish = (TextView) findViewById(R.id.finish);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updataProlfile();
            }
        });


        //載入原始資料
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //get data
                    String name = ""+ds.child("name").getValue();
                    String email = ""+ds.child("email").getValue();
                    String phone = ""+ds.child("phone").getValue();
                    String image = ""+ds.child("image").getValue();
                    String intro = ""+ds.child("intro").getValue();

                    //set data
                    nameEd.setText(name);
                    emailTv.setText(email);
                    phoneEd.setText(phone);
                    introEd.setText(intro);
                    try {
                        Picasso.get().load(image).into(myphoto);
                    }catch (Exception e){
                        //射程預設照案
                        Picasso.get().load(R.drawable.guava).into(myphoto);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updataProlfile() {
        String email = user.getEmail();
        String uid =user.getUid();
        String newName = nameEd.getText().toString();
        String newPhone = phoneEd.getText().toString();
        String newIntro = introEd.getText().toString();
        if (newName.isEmpty() || newPhone.isEmpty()) {
            Toast.makeText(this, "資料不能空白喔", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<Object,String> hashMap = new HashMap<>();
            hashMap.put("email",email);
            hashMap.put("uid",uid);
            hashMap.put("name",newName);
            hashMap.put("phone",newPhone);
            hashMap.put("image","");
            hashMap.put("intro",newIntro);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users");
            myRef.child(uid).setValue(hashMap);
            Toast.makeText(this, "更新成功!!", Toast.LENGTH_SHORT).show();
            finish();

        }
    }
}