package com.example.bar.sharedrecipes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MyCameraFragment extends DialogFragment implements View.OnClickListener {


    private static final int GALLERY = 1, CAMERA = 2;
    private File imgFile;
    private Uri outputFileUi;
    private ImageView recipeImg;
    private Boolean photoBoolean = true;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.camera_fragment,null);
        view.findViewById(R.id.camera_fragment_camera).setOnClickListener(this);
        view.findViewById(R.id.camera_fragment_gallery).setOnClickListener(this);
        recipeImg = getActivity().findViewById(R.id.Activity_Add_Recipe_Photo);


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.camera_fragment_camera:
                openCamera();
                break;
            case R.id.camera_fragment_gallery:
                openGallery();
        }

    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imgFile = Environment.getExternalStoragePublicDirectory("myPhotos.jpg");
        outputFileUi = MyFileProvider.getUriForFile(getActivity(),"com.example.bar.fileprovider3",imgFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUi);
        startActivityForResult(intent,CAMERA);


    }
    private void openGallery(){
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent,GALLERY);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        SharedPreferences pref = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if(resultCode == getActivity().RESULT_OK){
            switch (requestCode){
                case CAMERA:

                    Uri cameraUri = Uri.fromFile(imgFile);
                    editor.putString("URI",cameraUri.toString());
                    editor.apply();
                    Picasso.get().load(cameraUri).into(recipeImg);


                   /* int targetW = recipeImg.getWidth();
                    int targetH = recipeImg.getHeight();
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
                    int photoW = recipeImg.getWidth();
                    int photoH = recipeImg.getHeight();

                    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
                    bmOptions.inJustDecodeBounds = true;
                    bmOptions.inSampleSize = scaleFactor;*/

                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    AddRecipe.setBitmap(bitmap);
                    AddRecipe.setPhotoChanged(photoBoolean);



                    dismiss();
                    break;
                case GALLERY:
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                    String galleryUri = selectedImage.toString();
                    editor.putString("URI", galleryUri);
                    editor.commit();
                    Picasso.get().load(selectedImage).into(recipeImg);
                }
                    



                    AddRecipe.setPhotoChanged(photoBoolean);


                    try {
                        Bitmap galleryBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        AddRecipe.setBitmap(galleryBitmap);

                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                    dismiss();



            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}