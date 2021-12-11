package com.example.foodsharing;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapters.AdapterPost;
import models.Post;


public class Home extends Fragment implements AdapterPost.OnItemClickListener {
    public static final String EXTRA_PID = "pId";


    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    RecyclerView recyclerView;
    AdapterPost adapterPost;
    ArrayList<Post> postList;
    String email,uid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //init
        firebaseAuth = FirebaseAuth.getInstance();

        //recycler view and its properties
        recyclerView = view.findViewById(R.id.postsrecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
//        //show newest post first
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
//        //set layout to recyclerview
        recyclerView.setLayoutManager(linearLayoutManager);

        //init post list
        postList = new ArrayList<>();

        loadPosts();

        return view;
    }

    private void loadPosts() {
//        path of all posts
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //get all data from this ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    postList.add(post);
                    adapterPost = new AdapterPost(getActivity(),postList);
                    recyclerView.setAdapter(adapterPost);
                    adapterPost.setOnItemClickListener(Home.this);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getActivity(),""+error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            email = user.getEmail();
            uid = user.getUid();
        }else {
            startActivity(new Intent(getActivity(),Login.class));
        }
    }
    @Override
    public void onStart(){
        checkUserStatus();
        super.onStart();
    }


    @Override
    public void onItemClick(int postition) {
        Intent contentIntent = new Intent(getActivity(),FoodContent.class);
        Post clickItem = postList.get(postition);
        contentIntent.putExtra(EXTRA_PID,clickItem.getpId());
        startActivity(contentIntent);
    }
}