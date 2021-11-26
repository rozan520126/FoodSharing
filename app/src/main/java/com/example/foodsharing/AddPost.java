package com.example.foodsharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.app.ProgressDialog;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Spinner;

import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.AdapterPhoto;
import gun0912.tedbottompicker.TedBottomPicker;
import gun0912.tedbottompicker.TedBottomSheetDialogFragment;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class AddPost extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    DatabaseReference userDbRef;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    String[] cameraPermissions;
    String[] storagePermissions;

    //view
    EditText titleEt, desEt, daytimeEt, locationEt;
    ImageView imageIv;
    Button uploadBtn;

    private RecyclerView rcvPhoto;
    private AdapterPhoto photoAdapter;

    private Spinner timeSpinner;
    private ArrayAdapter timeArrayAdapter;



    //user info
    String uid,uName,uImage ,email;

    Uri image_uri = null;

//    ArrayList<Uri> imageUris = new ArrayList<>();
    ArrayList<String> imageUris = new ArrayList<>();
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        //init permission
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUserStatus();

        //取得會員資料
        userDbRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    uName = "" + ds.child("name").getValue();
                    uImage = "" + ds.child("image").getValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        titleEt = (EditText) findViewById(R.id.pTitleEt);
        desEt = (EditText)findViewById(R.id.pDescription);
        daytimeEt = (EditText)findViewById(R.id.pTime);
        locationEt = (EditText)findViewById(R.id.pLocation);
//        imageIv = (ImageView) findViewById(R.id.pImageIv);
        uploadBtn = (Button) findViewById(R.id.pUploadBtn);
        photoAdapter = new AdapterPhoto(AddPost.this,imageUris);
        rcvPhoto = (RecyclerView) findViewById(R.id.rcvPhoto);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        rcvPhoto.setLayoutManager(linearLayoutManager);
        rcvPhoto.setFocusable(false);
        rcvPhoto.setAdapter(photoAdapter);


        //test
        photoAdapter.setOnItemClickLitener(new AdapterPhoto.OnItemClickLitener() {
            @Override
            public void onNewClick(int position) {
               if (position == imageUris.size()-1){
                   Intent intent = new Intent(AddPost.this, MultiImageSelectorActivity.class);
                   intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                   intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                   intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                   startActivityForResult(intent, 10001);
               }else {
                   Intent intent = new Intent(AddPost.this, MultiImageSelectorActivity.class);
                   intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                   intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                   intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
                   int poss = position;
                   startActivityForResult(intent, 10002);//10002-->修改
               }
            }

            @Override
            public void onDeleClick(int position) {
                imageUris.remove(position);
                photoAdapter.notifyItemRemoved(position);
            }
        });

        if(imageUris.size() == 0){
            imageUris.add("dele");
        }


        rcvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                requestPermission();
//                showImagePick();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEt.getText().toString().trim();
                String des = desEt.getText().toString().trim();
                String daytime = daytimeEt.getText().toString().trim();
                String location = locationEt.getText().toString().trim();
                if (TextUtils.isEmpty(title)){
                    Toast.makeText(AddPost.this,"請輸入標題!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(des)){
                    Toast.makeText(AddPost.this,"記得描述一下細節喔",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(daytime)){
                    Toast.makeText(AddPost.this,"記得寫時間喔",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(location)){
                    Toast.makeText(AddPost.this,"要在那裡面交呢",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (image_uri == null){
                    uploadData(title,des,daytime,location,"noImage");
                }
                else {
                    uploadData(title,des,daytime,location,String.valueOf(image_uri));
                }
//                if (imageUris == null){
//                    uploadData(title,des,daytime,location,"noImage");
//                }
//                else {
//                    uploadData(title,des,daytime,location,String.valueOf(imageUris));
//                }
            }
        });

        timeSpinner = (Spinner) findViewById(R.id.spinner_time);
        timeArrayAdapter = ArrayAdapter.createFromResource(this,R.array.time_agreement_array, R.layout.support_simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeArrayAdapter);


    }

    //新方法
//    private void requestPermission() {
//        PermissionListener permissionlistener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
//                openBottomPicker();
//            }
//
//            @Override
//            public void onPermissionDenied(List<String> deniedPermissions) {
//                Toast.makeText(AddPost.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//            }
//        };
//        TedPermission.create()
//                .setPermissionListener(permissionlistener)
//                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
//                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .check();
//    }
//
//    private void openBottomPicker() {
//        TedBottomPicker.with(AddPost.this)
//                .setPeekHeight(1600)
//                .showTitle(false)
//                .setCompleteButtonText("Done")
//                .setEmptySelectionText("No Select")
//                .showMultiImage(new TedBottomSheetDialogFragment.OnMultiImageSelectedListener() {
//                    @Override
//                    public void onImagesSelected(List<Uri> uriList) {
//                        if (uriList != null && !uriList.isEmpty()){
//                            photoAdapter.setData(uriList);
//                            for (int i =0;i<uriList.size();i++){
//                                image_uri = uriList.get(i);
//                                imageUris.add(uriList.get(i));
//                            }
//                        }
//                    }
//                });
//
//    }

    private void uploadData(String title, String des,String daytime,String location , String uri) {
        pd.setMessage("發布中...");
        pd.show();

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" + timeStamp;
        if (!uri.equals("noImage")){
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()){
                                HashMap<Object,String> hashMap = new HashMap<>();
                                hashMap.put("uid",uid);
                                hashMap.put("uName",uName);
                                hashMap.put("uImage",uImage);
                                hashMap.put("pId",timeStamp);
                                hashMap.put("pTitle",title);
                                hashMap.put("pDes",des);
                                hashMap.put("pdaytime",daytime);
                                hashMap.put("pLocation",location);
                                hashMap.put("pImage",downloadUri);
                                hashMap.put("pTime",timeStamp);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                ref.child(timeStamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                pd.dismiss();
                                                Toast.makeText(AddPost.this,"post publish",Toast.LENGTH_SHORT).show();
                                                titleEt.setText("");
                                                desEt.setText("");
                                                daytimeEt.setText("");
                                                locationEt.setText("");
                                                imageIv.setImageURI(null);
                                                image_uri = null;
                                                startActivity(new Intent(AddPost.this,MainActivity.class));
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(AddPost.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPost.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            HashMap<Object,String> hashMap = new HashMap<>();
            hashMap.put("uid",uid);
            hashMap.put("uName",uName);
            hashMap.put("uImage",uImage);
            hashMap.put("pId",timeStamp);
            hashMap.put("pTitle",title);
            hashMap.put("pDes",des);
            hashMap.put("pdaytime",daytime);
            hashMap.put("pLocation",location);
            hashMap.put("pImage","noImage");
            hashMap.put("pTime",timeStamp);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
            ref.child(timeStamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            pd.dismiss();
                            Toast.makeText(AddPost.this,"post publish",Toast.LENGTH_SHORT).show();
                            titleEt.setText("");
                            desEt.setText("");
                            daytimeEt.setText("");
                            locationEt.setText("");
                            imageIv.setImageURI(null);
                            image_uri = null;
                            startActivity(new Intent(AddPost.this,MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPost.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if ((data!=null)){
            if (requestCode == 10001 || requestCode == 10002){
                final ArrayList<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                Bitmap bitmap = BitmapFactory.decodeFile(paths.get(0));

                if (requestCode == 10001){
                    imageUris.add(0,paths.get(0));
                    photoAdapter.notifyItemInserted(0);
                }
                else if (requestCode == 10002 && imageUris.size()>0){
                    int pos = 0;
                    imageUris.set(pos,paths.get(0));
                    photoAdapter.notifyItemChanged(pos);
                }
            }
        }
    }

//    private void showImagePick() {
//        String[] options = {"相機","相簿"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("選擇照片來源");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0){
//                    if (!checkCameraPermission()){
//                        requestCameraPermission();
//                    }else {
//                        pickFromCamera();
//                    }
//                }
//                if (which == 1){
//                    if (!checkStoragePermission()){
//                        requestStoragePermission();
//                    }else {
//                        pickFromGallery();
//                    }
//                }
//            }
//        });
//        builder.create().show();
//    }
//
//    private void pickFromCamera() {
//        ContentValues cv = new ContentValues();
//        cv.put(MediaStore.Images.Media.TITLE,"Temp Pcik");
//        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
//        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
//        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
//    }
//    private void pickFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
//    }
//
//    private boolean checkStoragePermission(){
//        boolean result = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
//        return result;
//    }
//    private void requestStoragePermission(){
//        ActivityCompat.requestPermissions(this,storagePermissions,STORAGE_REQUEST_CODE);
//    }
//
//    private boolean checkCameraPermission(){
//        boolean result = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
//        boolean result2 = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
//        return result && result2;
//    }
//    private void requestCameraPermission(){
//        ActivityCompat.requestPermissions(this,cameraPermissions,CAMERA_REQUEST_CODE);
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return super.onSupportNavigateUp();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case CAMERA_REQUEST_CODE:{
//                if (grantResults.length>0){
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (cameraAccepted && storageAccepted){
//                        pickFromCamera();
//                    }else {
//                        Toast.makeText(this,"Camera & Storage both permission are neccessary",Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//
//                }
//            }
//            break;
//            case STORAGE_REQUEST_CODE:{
//                if (grantResults.length > 0){
//                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (storageAccepted){
//                        pickFromGallery();
//                    }else {
//                        Toast.makeText(this,"Storage permissions neccessary",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == RESULT_OK){
//            if (requestCode == IMAGE_PICK_GALLERY_CODE){
//                image_uri = data.getData();
//
//                imageIv.setImageURI(image_uri);
//            }
//            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
//                imageIv.setImageURI(image_uri);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            email = user.getEmail();
            uid = user.getUid();
        }else {
            startActivity(new Intent(this,Start.class));
            finish();
        }
    }


}
