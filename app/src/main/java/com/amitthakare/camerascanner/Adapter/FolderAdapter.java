package com.amitthakare.camerascanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amitthakare.camerascanner.Model.FolderData;
import com.amitthakare.camerascanner.R;
import com.amitthakare.camerascanner.Var;
import com.bumptech.glide.Glide;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> {


    Context mContext;
    List<FolderData> mData;

    public FolderAdapter(Context mContext, List<FolderData> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.folder_design,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(mContext).load(mData.get(position).getFolderImage()).into(holder.fImage);
        holder.fName.setText(mData.get(position).getFolderName());
        holder.fDate.setText(mData.get(position).getFolderDate());
        holder.fTime.setText(mData.get(position).getFolderTime());
        holder.fPages.setText(mData.get(position).getFolderPages());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView fImage;
        TextView fName, fDate, fTime, fPages;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fImage = itemView.findViewById(R.id.folderImage);
            fName = itemView.findViewById(R.id.folderName);
            fDate = itemView.findViewById(R.id.folderDate);
            fTime = itemView.findViewById(R.id.folderTime);
            fPages = itemView.findViewById(R.id.folderPages);

        }
    }
}
