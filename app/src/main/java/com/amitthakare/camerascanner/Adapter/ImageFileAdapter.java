package com.amitthakare.camerascanner.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amitthakare.camerascanner.Model.ImageFileData;
import com.amitthakare.camerascanner.R;
import com.amitthakare.camerascanner.Var;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

public class ImageFileAdapter extends RecyclerView.Adapter<ImageFileAdapter.MyViewHolder> {

    Context mContext;
    List<ImageFileData> mData;

    public ImageFileAdapter(Context mContext, List<ImageFileData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.folder_file_design,parent,false);
        return new ImageFileAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.cardViewFiles.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_transition));

        Glide.with(mContext).load(mData.get(position).getImage()).into(holder.image);
        holder.imgPosition.setText((position+1)+"");

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        ImageView image;
        TextView imgPosition;
        CardView cardViewFiles;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.fileImage);
            imgPosition = itemView.findViewById(R.id.filePosition);
            cardViewFiles = itemView.findViewById(R.id.cardViewFiles);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            if (!Var.isMovable)
            {
                // add option when movable is false
                menu.add(getAdapterPosition(),101,0,"Delete");
            }
        }
    }

    public boolean RemoveItem(int position,String folderName)
    {
        File file = new File(Var.IMAGE_DIR+"/"+folderName+"/"+mData.get(position).getImageName());
        if (file.delete())
        {
            mData.remove(position);
            notifyDataSetChanged();
            return true;
        }else
        {
            Toast.makeText(mContext, "Not Deleted!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
