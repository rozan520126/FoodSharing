package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodsharing.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.PhotoViewHolder>{

    private Context context;
    private List<Uri> mListPhotos = new ArrayList<>();


    public AdapterPhoto(Context context) {
        this.context = context;
//        this.imageUrls = imageUrls;

    }


    public void setData(List<Uri> list){
        this.mListPhotos = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo,parent,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Uri uri = mListPhotos.get(position);
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
            holder.imgPhoto.setImageBitmap(bitmap);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (mListPhotos == null){
            return 0;
        }else {
            return mListPhotos.size();
        }
//        return imageUrls.size();
    }

//    public void setOnItemClickLitener(OnItemClickLitener listener) {
//        this.listener = listener;
//    }
//    public interface OnItemClickLitener {
//        void onNewClick(int position);
//        void onDeleClick(int position);
//    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgPhoto, imgDel;

        public PhotoViewHolder(View itemView){
            super(itemView);
            imgPhoto = (ImageView) itemView.findViewById(R.id.imgPhoto);

        }

    }
}
