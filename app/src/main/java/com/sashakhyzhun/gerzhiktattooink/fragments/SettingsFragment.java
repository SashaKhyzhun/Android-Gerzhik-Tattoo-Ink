package com.sashakhyzhun.gerzhiktattooink.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.gerzhiktattooink.utils.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.CAMERA_REQUEST;
import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.SELECT_FILE;

/**
 * Created by SashaKhyzhun on 2/2/17.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private SessionManager sessionManager;
    private TextView tvName;
    private TextView tvProfilePicture;
    private TextView tvLogout;
    private TextView tvDeleteAccount;
    private TextView tvUserName;
    private String userName;
    private ImageView imageUserPicture;
    public Uri picUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        imageUserPicture = (ImageView)view.findViewById(R.id.ivUserPicture);
        tvUserName = (TextView)view.findViewById(R.id.tvUserName);

        String userPathToPic = sessionManager.getUserPathToPic();
        userName = sessionManager.getUserName();

        Glide.with(this).load(userPathToPic).bitmapTransform(new CropCircleTransformation(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageUserPicture);
        tvUserName.setText(userName);

        tvName           = (TextView) view.findViewById(R.id.textViewName);
        tvLogout         = (TextView) view.findViewById(R.id.textViewLogout);
        tvDeleteAccount  = (TextView) view.findViewById(R.id.textViewDeleteAccount);
        tvProfilePicture = (TextView) view.findViewById(R.id.textViewProfilePicture);




        tvName.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvDeleteAccount.setOnClickListener(this);
        tvProfilePicture.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewName:
                final TextView tvEnterYourName = (TextView) getActivity().findViewById(R.id.textViewEnterYourName);
                tvEnterYourName.setVisibility(View.VISIBLE);

                final EditText etNewName = (EditText) getActivity().findViewById(R.id.editTextNewName);
                etNewName.setVisibility(View.VISIBLE);
                etNewName.setText(sessionManager.getUserName());

                final Button imageButtonOK = (Button) getActivity().findViewById(R.id.imageButtonAcceptNewName);
                imageButtonOK.setVisibility(View.VISIBLE);

                final View line11 = getActivity().findViewById(R.id.view11);
                line11.setVisibility(View.VISIBLE);

                imageButtonOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newName = etNewName.getText().toString().trim();

                        sessionManager.setUserName(newName);
                        tvUserName.setText(sessionManager.getUserName());

                        tvName.setVisibility(View.VISIBLE);
                        etNewName.setVisibility(View.GONE);
                        line11.setVisibility(View.GONE);
                        imageButtonOK.setVisibility(View.GONE);
                        tvEnterYourName.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.textViewProfilePicture:

                LinearLayout buttonsLayout  = (LinearLayout) getActivity().findViewById(R.id.buttonsLayout);
                TextView     tvTakeAPicture = (TextView)     getActivity().findViewById(R.id.textViewTakeAPicture);
                TextView     tvChooseFrom   = (TextView)     getActivity().findViewById(R.id.textViewChooseFromGallery);

                if (checkWriteExternalPermission()) {
                    if (buttonsLayout.getVisibility() == View.GONE) {
                        buttonsLayout.setVisibility(View.VISIBLE);
                        tvTakeAPicture.setVisibility(View.VISIBLE);
                        tvChooseFrom.setVisibility(View.VISIBLE);

                        tvTakeAPicture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File f = new File(Environment.getExternalStorageDirectory(), "profile.jpg");
                                picUri = Uri.fromFile(f);
                                startActivityForResult(intent, CAMERA_REQUEST);
                            }
                        });

                        tvChooseFrom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, SELECT_FILE);
                            }
                        });
                    } else {
                        buttonsLayout.setVisibility(View.GONE);
                        tvTakeAPicture.setVisibility(View.INVISIBLE);
                        tvChooseFrom.setVisibility(View.INVISIBLE);
                        tvTakeAPicture.setOnClickListener(null);
                        tvChooseFrom.setOnClickListener(null);
                    }
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE}, 1);
                    return;
                }

                break;
            case R.id.textViewLogout:
                sessionManager.logoutUser();
                break;
            case R.id.textViewDeleteAccount:
                sessionManager.logoutUser();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST ) {
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data"); // get the cropped bitmap
                savePhoto(thePic);
            }
            else if (requestCode == SELECT_FILE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                //ImageView imageView = (ImageView) findViewById(R.id.imgView);
                imageUserPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            }

        }
    }


    public void savePhoto (Bitmap photo) {
        String root = Environment.getExternalStorageDirectory().toString(); // path
        File myDir = new File(root + "/android/data/com.sashakhyzhun.gerzhiktattoink"); // folder
        myDir.mkdirs(); // create folder

        File file = new File(myDir, "profile.jpg"); // file
        if (!file.exists()) {
            try { // if not exist
                file.createNewFile(); // create
            } catch (IOException e) { e.printStackTrace(); }
        } else {
            file.delete(); // if exist - delete
            try {
                file.createNewFile(); // and again create, ibo nexyu
            } catch (IOException e) { e.printStackTrace(); }
        }

        Uri uri = Uri.fromFile(file);
        sessionManager.setUserPathToPic(uri.toString());

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file); // create folder in system like a real FILE, not virtual.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close(); // closing stream
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        out = null;
        try {
            out = new FileOutputStream(file); // create picture in system like a real FILE, not virtual.
            photo.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close(); // closing stream
                }
            } catch (IOException e) { e.printStackTrace(); }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do.
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request
        }
    }


    private boolean checkWriteExternalPermission() {
        int res = getContext().checkCallingOrSelfPermission(READ_EXTERNAL_STORAGE);
        return (res == PackageManager.PERMISSION_GRANTED);
    }



}
