package com.example.foodsharing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    TextInputEditText emailEt, passwordEt, nameEt;
    Button signUp;
    TextView haveAccountTv, imgHint;
    ImageView myPhoto;


    //firebase data
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //firebase storage
    private Uri imageUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("ProfilePic");

        //init
        emailEt = findViewById(R.id.emailEt);
        passwordEt = findViewById(R.id.passwordEt);
        nameEt = findViewById(R.id.name);
        signUp = findViewById(R.id.registerBtn);
        haveAccountTv = findViewById(R.id.have_accountTv);
        myPhoto = findViewById(R.id.myphoto);
        imgHint = findViewById(R.id.hintText);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("註冊中...");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String name = nameEt.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEt.setError("無效的信箱!");
                    emailEt.setFocusable(true);
                } else if (password.length() < 6) {
                    passwordEt.setError("密碼長度至少6位數!");
                    passwordEt.setFocusable(true);
                } else if(nameEt == null){
                    nameEt.setError("名稱不得為空!");
                }
                else {
                    registerUser(email, password,name);
                }
            }
        });

        myPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1).start(Register.this);
            }
        });

        haveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

    }

    private void registerUser(String email, String password,String name) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            if (imageUri != null) {
                                final StorageReference fileRef = storageReference
                                        .child(mAuth.getCurrentUser().getUid() + ".jpg");
                                uploadTask = fileRef.putFile(imageUri);
                                uploadTask.continueWithTask(new Continuation() {
                                    @Override
                                    public Object then(@NonNull Task task) throws Exception {
                                        if (!task.isSuccessful()) {
                                            throw task.getException();
                                        }
                                        return fileRef.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Uri downloadUri = task.getResult();
                                            myUri = downloadUri.toString();

                                            user = mAuth.getCurrentUser();
                                            String uid = user.getUid();
                                            HashMap<Object, String> hashMap = new HashMap<>();
                                            hashMap.put("email", email);
                                            hashMap.put("uid", uid);
                                            hashMap.put("name", name);
                                            hashMap.put("phone", "");
                                            hashMap.put("image", myUri);
                                            hashMap.put("intro", "");
                                            //database instance
                                            firebaseDatabase = FirebaseDatabase.getInstance();
                                            //path to store user data "Users"
                                            databaseReference = firebaseDatabase.getReference("Users");
                                            databaseReference.child(uid).setValue(hashMap);


                                            Toast.makeText(Register.this, "註冊中...\n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                user = mAuth.getCurrentUser();
                                String uid = user.getUid();
                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("email", email);
                                hashMap.put("uid", uid);
                                hashMap.put("name", name);
                                hashMap.put("phone", "");
                                hashMap.put("image", "noImage");
                                hashMap.put("intro", "");
                                //database instance
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                //path to store user data "Users"
                                databaseReference = firebaseDatabase.getReference("Users");
                                databaseReference.child(uid).setValue(hashMap);


                                Toast.makeText(Register.this, "註冊中...\n" + user.getEmail(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Register.this, MainActivity.class));
                                finish();
                            }
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            String email = user.getEmail();
//                            String uid = user.getUid();
//                            HashMap<Object, String> hashMap = new HashMap<>();
//                            hashMap.put("email",email);
//                            hashMap.put("uid",uid);
//                            hashMap.put("name","");
//                            hashMap.put("phone","");
//                            hashMap.put("image","");
//                            hashMap.put("intro","");
//                            //database instance
//                            FirebaseDatabase database = FirebaseDatabase.getInstance();
//                            //path to store user data "Users"
//                            DatabaseReference reference = database.getReference("Users");
//                            reference.child(uid).setValue(hashMap);
//
//
//                            Toast.makeText(Register.this,"註冊中...\n"+user.getEmail(),Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(Register.this, MainActivity.class));
//                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Register.this, "驗證失敗", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Register.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            myPhoto.setImageURI(imageUri);
            if (imageUri != null) {
                imgHint.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "載入失敗", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();

    }
}