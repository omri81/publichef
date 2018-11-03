package com.example.bar.sharedrecipes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SqliteRecyclerViewAdapter extends RecyclerView.Adapter<SqliteRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Recipe> recipes;
    private StorageReference storageRef;
    private Context context;
    private ArrayList<String> urls;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public SqliteRecyclerViewAdapter(ArrayList<Recipe> recipes, StorageReference storageRef,ArrayList<String> urls, Context context, DatabaseHelper helper, SQLiteDatabase db){
        this.recipes = recipes;
        this.storageRef = storageRef;
        this.context = context;
        this.urls = urls;
        this.db = db;
        this.helper = helper;
    }
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public SqliteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_recipes_item_no_like,parent,false);
        return new SqliteRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SqliteRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.chefName.setText(recipes.get(position).getPersonName());
        holder.recipeName.setText(recipes.get(position).getRecipeName());
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.itemView.setLongClickable(true);

        if(isOnline()) {
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
        }else{
            holder.progressBar.setVisibility(View.GONE);
            holder.iv.setImageResource(R.drawable.icons8wifioff100);
            holder.noConnection.setText("No Connection");
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(context,RecipeDescription.class);
                intent.putExtra("sharedRecipe",recipes.get(position));
                intent.putExtra("downloadUrl",urls.get(position));
                context.startActivity(intent);
            }

            @Override
            public void onLongClick(View view,final int postion) {
                helper = new DatabaseHelper(context);
                db = helper.getWritableDatabase();

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Are you sure you want to delete this recipe from my recipes?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.delete(DatabaseHelper.TABLE_NAME, "id = " + recipes.get(postion).getId(),null);
                        recipes.remove(position);
                        urls.remove(position);
                        notifyDataSetChanged();
                        MyLoadingFragment fragment = new MyLoadingFragment();
                        FragmentManager manager = ((Activity) context).getFragmentManager();
                        fragment.show(manager,null);
                    }
                });
                alert.setNegativeButton("Cancel",null);

                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

        private ImageView iv;
        private TextView chefName, recipeName,noConnection;
        private ProgressBar progressBar;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.my_recipes_item_image_view_no_like);
            chefName = (TextView) itemView.findViewById(R.id.my_recipes_item_chef_name_no_like);
            recipeName = (TextView) itemView.findViewById(R.id.my_recipes_item_recipe_name_no_like);
            progressBar = (ProgressBar) itemView.findViewById(R.id.my_recipes_item_progressbar_no_like);
            noConnection = (TextView) itemView.findViewById(R.id.my_recipes_item_no_like_no_connection_no_like);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }


        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition());

        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onLongClick(view,getAdapterPosition());
            return true;
        }
    }
}
