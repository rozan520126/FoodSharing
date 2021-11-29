package com.example.foodsharing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class EditProfile extends AppCompatActivity {

    //firebase
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String storagePath = "Users_Profile_Imgs/";

    //views
    ImageView myphoto;
    EditText nameEd,phoneEd,introEd;
    TextView emailTv,cancel,finish;

    private Uri imageUri;
    private String myUri = "" ;
    private StorageTask uploadTask;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //init firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("ProfilePic");


        //init view
        myphoto = (ImageView) findViewById(R.id.myphoto);
        nameEd = (EditText) findViewById(R.id.nameEd);
        phoneEd = (EditText) findViewById(R.id.phoneEd);
        introEd = (EditText) findViewById(R.id.introEd);
        emailTv = (TextView)findViewById(R.id.emailTv);

        //button
        cancel = (TextView) findViewById(R.id.cancel);
        finish = (TextView) findViewById(R.id.finish);


        myphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(EditProfile.this);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProlfile();
            }
        });


        //顯示原始資料
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
                    imageUri = Uri.parse(image);
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

    private void updateProlfile() {
        String email = user.getEmail();
        String uid =user.getUid();
        String newName = nameEd.getText().toString();
        String newPhone = phoneEd.getText().toString();
        String newIntro = introEd.getText().toString();
        if (newName.isEmpty() || newPhone.isEmpty()) {
            Toast.makeText(this, "資料不能空白喔", Toast.LENGTH_SHORT).show();
        }else {
            if (imageUri != null) {
                final StorageReference fileRef = storageReference
                        .child(mAuth.getCurrentUser().getUid()+".jpg");
                uploadTask = fileRef.putFile(imageUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            myUri = downloadUri.toString();

                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name",newName);
                            hashMap.put("phone",newPhone);
                            hashMap.put("image",myUri);
                            hashMap.put("intro",newIntro);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("Users");
                            myRef.child(uid).setValue(hashMap);
                            Toast.makeText(EditProfile.this, "更新成功!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditProfile.this,MainActivity.class));
                        }
                    }
                });

            }else {
                HashMap<Object,String> hashMap = new HashMap<>();
                hashMap.put("email",email);
                hashMap.put("uid",uid);
                hashMap.put("name",newName);
                hashMap.put("phone",newPhone);
                hashMap.put("image","noImage");
                hashMap.put("intro",newIntro);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Users");
                myRef.child(uid).setValue(hashMap);
                Toast.makeText(this, "更新成功!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditProfile.this,MainActivity.class));
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            myphoto.setImageURI(imageUri);
        }else {
            Toast.makeText(this, "載入失敗", Toast.LENGTH_SHORT).show();
        }
    }
}