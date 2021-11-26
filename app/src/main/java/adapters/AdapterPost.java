package adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodsharing.R;

import com.squareup.picasso.Picasso;

import java.util.List;

import models.Post;

public class AdapterPost extends RecyclerView.Adapter<AdapterPost.MyHolder>{

    Context context;
    List<Post> postList;

    private static OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int postition);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AdapterPost(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //layout row_post
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        Post currentItem = postList.get(i);

        //get data
//        String uid = postList.get(i).getUid();
        String uName = currentItem.getuName();
        String uImage = currentItem.getuImage();
//        String pId = postList.get(i).getpId();
        String pTitle = currentItem.getpTitle();
//        String pDes = postList.get(i).getpDes();
        String pImage = currentItem.getpImage();
        String pdayTime = currentItem.getpdaytime();
        String pLocation = currentItem.getpLocation();
//        String pTimeStamp = postList.get(i).getpTime();

        //convert timestamp to dd/mm/yyyy hh:mm am/pm
//        Calendar calendar = Calendar.getInstance(Locale.getDefault());
//        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
//        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        //set data
        myHolder.uNameTv.setText(uName);
//        myHolder.pTimeTv.setText(pTime);
        myHolder.pTitleTv.setText(pTitle);
//        myHolder.pDes.setText(pDes);

        //set user dp
        try{
            Glide.with(context)
                    .load(uImage)
                    .into(myHolder.uImageIv);
//            Picasso.get().load(uImage).placeholder(R.drawable.ic_default_image).into(myHolder.uImageIv);
        }
        catch (Exception e){
            System.out.print(e.toString());
        }
        //if no image (pImage.equl("noImage")) then hide ImageView
        if (pImage.equals("noImage")){
            //hide imageview
            myHolder.pImageIv.setVisibility(View.GONE);
        }else {
            try {

            }catch (Exception e){
                System.out.print(e.toString());
            }
        }

        //set post image
        try{
            Glide.with(context)
                    .load(pImage)
                    .into(myHolder.pImageIv);
//            Picasso.get().load(pImage).into(myHolder.pImageIv);
        }
        catch (Exception e){
            System.out.print(e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder{

        //view from row_post
        ImageView uImageIv,pImageIv;
        TextView uNameTv,pTitleTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //init view
            uImageIv = itemView.findViewById(R.id.uPicIv);
            pImageIv = itemView.findViewById(R.id.pImageIv);
            uNameTv = itemView.findViewById(R.id.uNameTv);
            pTitleTv = itemView.findViewById(R.id.pTitleTv);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
