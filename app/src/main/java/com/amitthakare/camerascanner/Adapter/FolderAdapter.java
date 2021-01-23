package com.amitthakare.camerascanner.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amitthakare.camerascanner.MainActivity;
import com.amitthakare.camerascanner.Model.FolderData;
import com.amitthakare.camerascanner.R;
import com.amitthakare.camerascanner.Var;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> implements Filterable {


    Context mContext;
    List<FolderData> mData;
    //for text search
    List<FolderData> mDataFullList;

    private OnRecyclerClickListener recyclerClickListener;

    public void setOnRecyclerClickListerner(OnRecyclerClickListener listerner)
    {
        recyclerClickListener = listerner;
    }

    public interface OnRecyclerClickListener {

        void onRecyclerItemClick(int position);

    }
    public FolderAdapter(Context mContext, List<FolderData> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mDataFullList = new ArrayList<>(mData);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.folder_design,parent,false);
        return new MyViewHolder(view,recyclerClickListener);
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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        ImageView fImage;

        TextView fName, fDate, fTime, fPages;

        public MyViewHolder(@NonNull View itemView, final OnRecyclerClickListener listener) {
            super(itemView);

            fImage = itemView.findViewById(R.id.folderImage);
            fName = itemView.findViewById(R.id.folderName);
            fDate = itemView.findViewById(R.id.folderDate);
            fTime = itemView.findViewById(R.id.folderTime);
            fPages = itemView.findViewById(R.id.folderPages);
            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onRecyclerItemClick(position);
                        }
                    }
                }
            });


        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.add(getAdapterPosition(),101,0,"Delete");
            menu.add(getAdapterPosition(),102,1,"Rename");

        }

    }
    //this is not interface method when contexmenu is generated then we can access this in oncontextitemselected by adapter.

    public boolean RemoveItem(int position)
    {
        File file = new File(Var.IMAGE_DIR+"/"+mData.get(position).getFolderName());
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

    //for text search
    @Override
    public Filter getFilter() {
        return recyclerFilter;
    }

    private Filter recyclerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<FolderData> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0)
            {
                //if we not entered any value then show full list as it is
                filteredList.addAll(mDataFullList);
            }else
            {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (FolderData item : mDataFullList)
                {
                    if (item.getFolderName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }
            }

            //this results will be passed to the publishResults and then added the data to the mData and refresh the recycler.
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mData.clear();
            mData.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}

