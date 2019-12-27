package com.example.inclass11_group34;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    ArrayList<String> images;
    MainActivity mainActivity;

    public ImageAdapter(ArrayList<String> images, MainActivity mainActivity) {
        this.images = images;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageAdapter.ViewHolder holder, final int position) {
        if (images.size() != 0) {
            final String imageItem = images.get(position);
            holder.image = imageItem;
            holder.imageArrayList = images;
            Picasso.get().load(imageItem).into(holder.iv);
            holder.iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageReference = storage.getReference();
                    String path= holder.image.split("images%2F")[1].split("png")[0];
                    StorageReference storageRef = storageReference.child("images/"+path+"png");
                    storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(mainActivity, "Image deleted Successfully", Toast.LENGTH_SHORT).show();
                            images.remove(holder.image);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, images.size());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mainActivity, "Image couldn't be deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        String image;
        ArrayList<String> imageArrayList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }
    }
