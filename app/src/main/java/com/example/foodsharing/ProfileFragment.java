package com.example.foodsharing;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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


public class ProfileFragment extends Fragment {


    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String storagePath = "Users_Profile_Imgs/";

    //views
    ImageView myphoto;
    TextView nameTv,introTv,edit;

    ProgressDialog pd;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    String cameraPermissions[];
    String storagePermissions[];

    Uri image_uri;

    String profilePhoto;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //init permission
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init view
        myphoto = view.findViewById(R.id.myphoto);
        nameTv = view.findViewById(R.id.nameTv);
        introTv = view.findViewById(R.id.introTv);

        edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EditProfile.class));
            }
        });

        pd = new ProgressDialog(getActivity());

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //get data
                    String name = ""+ds.child("name").getValue();
                    String image = ""+ds.child("image").getValue();
                    String intro = ""+ds.child("intro").getValue();

                    //set data
                    nameTv.setText(name);
                    introTv.setText(intro);

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
        return view;
    }

//    private boolean checkStoragePermission(){
//        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == (PackageManager.PERMISSION_GRANTED);
//        return result;
//    }
//    private void requesetStoragePermission(){
//        ActivityCompat.requestPermissions(getActivity(),storagePermissions,STORAGE_REQUEST_CODE);
//    }
//    private boolean checkCameraPermission(){
//        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
//                == (PackageManager.PERMISSION_GRANTED);
//
//        boolean result2 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == (PackageManager.PERMISSION_GRANTED);
//        return result && result2;
//    }
//    private void requesetCameraPermission(){
//        ActivityCompat.requestPermissions(getActivity(),cameraPermissions,CAMERA_REQUEST_CODE);
//    }
//
//
//    private void showImagePicDialog() {
//        String option[] = {"相機", "相簿"};
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("選擇照片來源...");
//        builder.setItems(option, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (which == 0) {
//                    if (!checkCameraPermission()){
//                        requesetCameraPermission();
//                    }else{
//                        pickFromCamera();
//                    }
//                } else if (which == 1) {
//                    if (!checkStoragePermission()){
//                        requesetStoragePermission();
//                    }else {
//                        pickFromGallery();
//                    }
//                }
//            }
//        });
//        builder.create().show();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case CAMERA_REQUEST_CODE:{
//                if (grantResults.length > 0){
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (cameraAccepted && writeStorageAccepted){
//                        pickFromCamera();
//                    }else {
//                        Toast.makeText(getActivity(),"請確認授權",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            case STORAGE_REQUEST_CODE:{
//                if (grantResults.length > 0){
//                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    if (writeStorageAccepted){
//                        pickFromGallery();
//                    }else {
//                        Toast.makeText(getActivity(),"請確認授權",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == RESULT_OK){
//            if (requestCode == IMAGE_PICK_GALLERY_CODE){
//                image_uri = data.getData();
//                uploadProfileCamera(image_uri);
//            }
//            if (requestCode == IMAGE_PICK_CAMERA_CODE){
//                uploadProfileCamera(image_uri);
//            }
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    private void uploadProfileCamera(Uri uri) {
//        pd.show();
//
//        String filePathAndName = storagePath + "" + profilePhoto + "_" + user.getUid();
//        StorageReference storageReference2nd = storageReference.child(filePathAndName);
//        storageReference2nd.putFile(uri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
//                        while (!uriTask.isSuccessful()){
//                            Uri downloadUri = uriTask.getResult();
//
//                            if (uriTask.isSuccessful()){
//                                HashMap<String , Object> results = new HashMap<>();
//                                results.put(profilePhoto,downloadUri.toString());
//                                databaseReference.child(user.getUid()).updateChildren(results)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                pd.dismiss();
//                                                Toast.makeText(getActivity(),"照片上傳中",Toast.LENGTH_SHORT).show();
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                pd.dismiss();
//                                                Toast.makeText(getActivity(),"照片上傳失敗",Toast.LENGTH_SHORT).show();
//                                            };
//                                        });
//                            }else {
//                                pd.dismiss();
//                                Toast.makeText(getActivity(),"error",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        pd.dismiss();
//                        Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void pickFromGallery() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
//        galleryIntent.setType("image/*");
//        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
//    }
//    private void pickFromCamera() {
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
//        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
//        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
//
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
//        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
//    }
}