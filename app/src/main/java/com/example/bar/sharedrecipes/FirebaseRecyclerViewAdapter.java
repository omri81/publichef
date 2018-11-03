package com.example.bar.sharedrecipes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FirebaseRecyclerViewAdapter extends RecyclerView.Adapter<FirebaseRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Recipe> recipes;
    private StorageReference storageRef;
    private ArrayList<String> urls;
    private Context context;
    private DatabaseReference myRef;
    SQLiteDatabase likeDBR;
    SQLiteDatabase likeDBW;
    private ArrayList<Integer> likes;




    public FirebaseRecyclerViewAdapter(ArrayList<Recipe> recipes, StorageReference storageRef, ArrayList<String> urls,Context context,DatabaseReference myRef,SQLiteDatabase likeDBR, SQLiteDatabase likeDBW,ArrayList<Integer> likes){
        this.recipes = recipes;
        this.storageRef = storageRef;
        this.urls = urls;
        this.context = context;
        this.myRef = myRef;
        this.likeDBR = likeDBR;
        this.likeDBW = likeDBW;
        this.likes = likes;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_recipes_item,parent,false);
        return new FirebaseRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.chefName.setText(recipes.get(position).getPersonName());
        holder.recipeName.setText(recipes.get(position).getRecipeName());
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.likeCounter.setText(recipes.get(position).getLikes()+"");





        if (!recipes.get(position).getLiked()) {
            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rsz_1rsz_like_pressed));
        }else{
            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heartred));
        }

        storageRef.child(urls.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
             Picasso.get().load(uri).into(holder.iv);
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context,RecipeDescription.class);
                intent.putExtra("sharedRecipe",recipes.get(position));
                intent.putExtra("downloadUrl",urls.get(position));
                context.startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int postion) {

            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    holder.counter = recipes.get(position).getLikes();
                    holder.counter++;
                    myRef.child(urls.get(position)).child("likes").setValue(holder.counter);
                    

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Toast.makeText(context, recipes.get(position).getLiked()+"", Toast.LENGTH_SHORT).show();
                            holder.likeCounter.setText(holder.counter + "");
                            holder.likeBtn.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heartred));
                            changeImage(position);
                            Toast.makeText(context, recipes.get(position).getLiked()+"", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }




                    /*myRef.child(urls.get(position)).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long likes = (long) dataSnapshot.getValue();
                            recipes.get(position).setLikes(likes);
                            holder.likeCounter.setText(holder.counter +"");
                            holder.clicked = false;

                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    myRef.child(urls.get(position)).child("likes").setValue(holder.counter);*/


                    //holder.likeBtn.setClickable(false);


        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
    public void changeImage(int index){
        recipes.get(index).setLiked(true);

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView iv;
        private TextView chefName, recipeName;
        private ProgressBar progressBar;
        private ImageView likeBtn;
        private TextView likeCounter;
        private ItemClickListener itemClickListener;
        private boolean clicked;
        private long counter;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.my_recipes_item_image_view);
            chefName = (TextView) itemView.findViewById(R.id.my_recipes_item_chef_name);
            recipeName = (TextView) itemView.findViewById(R.id.my_recipes_item_recipe_name);
            progressBar = (ProgressBar) itemView.findViewById(R.id.my_recipes_item_progressbar);
            likeBtn = (ImageView) itemView.findViewById(R.id.recipe_item_like_img);
            likeCounter = (TextView) itemView.findViewById(R.id.my_recipes_item_likes_counter);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition());

        }

    }

}
