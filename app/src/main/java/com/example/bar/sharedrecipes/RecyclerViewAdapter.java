package com.example.bar.sharedrecipes;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<Recipe> recipes;
    private StorageReference storageRef;
    private ArrayList<String> urls;

    public RecyclerViewAdapter(ArrayList<Recipe> recipes, StorageReference storageRef, ArrayList<String> urls) {
        this.recipes = recipes;
        this.storageRef = storageRef;
        this.urls = urls;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_recipes_item,parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.nameTv.setText(recipes.get(position).getPersonName());
        holder.recipeName.setText(recipes.get(position).getRecipeName());



    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements ItemClickListener{
        private TextView nameTv;
        private TextView recipeName;
        private ImageView iv;
        private ItemClickListener itemClickListener;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.my_recipes_item_chef_name);
            recipeName = itemView.findViewById(R.id.my_recipes_item_recipe_name);
            iv = (ImageView) itemView.findViewById(R.id.my_recipes_item_image_view);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }


        @Override
        public void onClick(View view, int position) {
            itemClickListener.onClick(view, getAdapterPosition());
        }


        @Override
        public void onLongClick(View view, int postion) {

        }
    }
}
