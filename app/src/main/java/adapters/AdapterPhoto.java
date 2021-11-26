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
//    private List<Uri> mListPhotos = new ArrayList<>();

    private OnItemClickLitener listener;//點選事件介面
    private ArrayList<String> imageUrls;
    private PhotoViewHolder viewHolder;
    private View view;

    public AdapterPhoto(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }


    public void setData(List<Uri> list){
//        this.mListPhotos = list;
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
//        Uri uri = mListPhotos.get(position);
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
//            holder.imgPhoto.setImageBitmap(bitmap);
//        }catch (IOException e){
//            e.printStackTrace();
//        }

//        if (mListPhotos.size() == 0 || mListPhotos.size() == position){
//            holder.imgDel.setVisibility(View.GONE);
//            holder.imgPhoto.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_default_image));
//        }else {
//            holder.imgDel.setVisibility(View.VISIBLE);
//            Glide.with(context)
//                    .load(mListPhotos.get(position))
//                    .placeholder(R.drawable.ic_default_image)
//                    .into(holder.imgPhoto);
//        }
        if(imageUrls.get(position).equals("dele")){
            holder.imgPhoto.setBackgroundResource(R.drawable.ic_default_image);
            holder.imgDel.setVisibility(View.GONE);
        }else {
            holder.imgDel.setVisibility(View.VISIBLE);
        }

        if (imageUrls.get(position).contains("storage")){
            try {
                File file = new File(imageUrls.get(position));
                Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(file));
                Drawable drawable =new BitmapDrawable(bmp);
                holder.imgPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.imgPhoto.setImageBitmap(bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
//        if (mListPhotos == null){
//            return 0;
//        }else {
//            return mListPhotos.size();
//        }
        return imageUrls.size();
    }

    public void setOnItemClickLitener(OnItemClickLitener listener) {
        this.listener = listener;
    }
    public interface OnItemClickLitener {
        void onNewClick(int position);
        void onDeleClick(int position);
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgPhoto, imgDel;

        public PhotoViewHolder(View itemView){
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.imgPhoto);
            imgDel = itemView.findViewById(R.id.imgDel);

            imgPhoto.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int position = (Integer) v.getTag();
                    listener.onNewClick(getPosition());
                }
            });
            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer)v.getTag();
                    listener.onDeleClick(getPosition());
                }
            });
        }

    }
}
