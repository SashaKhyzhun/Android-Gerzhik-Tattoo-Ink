package com.sashakhyzhun.gerzhiktattooink.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.gerzhiktattooink.activity.MainActivity;
import com.sashakhyzhun.gerzhiktattooink.utils.SessionManager;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getCacheDir;
import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.CAMERA_REQUEST;
import static com.sashakhyzhun.gerzhiktattooink.utils.Constants.CROP_PIC;
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
    private String userPathToPic;
    private ImageView imageUserPicture;
    private Context context;
    public Uri picUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = SessionManager.getInstance(getContext());
        userPathToPic = sessionManager.getUserPathToPic();
        userName = sessionManager.getUserName();
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        imageUserPicture = (ImageView)view.findViewById(R.id.ivUserPicture);
        Glide.with(this)
                .load(userPathToPic)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageUserPicture);

        tvUserName = (TextView)view.findViewById(R.id.tvUserName);
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

                LinearLayout buttonsLayout   = (LinearLayout) getActivity().findViewById(R.id.buttonsLayout);
                Button       btnTakeAPicture = (Button)       getActivity().findViewById(R.id.buttonTakeAPicture);
                Button       btnChooseFrom   = (Button)       getActivity().findViewById(R.id.buttonChooseFromGallery);

                // if we got a permission for read & write storage
                if (checkPermissions()) {
                    // if our layout is gone then show, else hide.
                    if (buttonsLayout.getVisibility() == View.GONE) {
                        buttonsLayout.setVisibility(View.VISIBLE);
                        btnTakeAPicture.setVisibility(View.VISIBLE);
                        btnChooseFrom.setVisibility(View.VISIBLE);

                        btnTakeAPicture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File f = new File(Environment.getExternalStorageDirectory(), "profile.jpg");
                                picUri = Uri.fromFile(f);
                                startActivityForResult(intent, CAMERA_REQUEST);
                            }
                        });

                        btnChooseFrom.setOnClickListener(v1 -> Crop.pickImage(context, this));

                    } else {
                        buttonsLayout.setVisibility(View.GONE);
                        btnTakeAPicture.setVisibility(View.INVISIBLE);
                        btnChooseFrom.setVisibility(View.INVISIBLE);
                        btnTakeAPicture.setOnClickListener(null);
                        btnChooseFrom.setOnClickListener(null);
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
            if (requestCode == CAMERA_REQUEST) {
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data"); // get the cropped bitmap
                savePhoto(thePic);
                getActivity().recreate();
            }
            else if (requestCode == Crop.REQUEST_PICK) {
                beginCrop(data.getData());
            }
            else if (requestCode == Crop.REQUEST_CROP) {
                try {
                    handleCrop(resultCode, data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == CROP_PIC) {
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data"); // get the cropped bitmap
                savePhoto(thePic);
                getActivity().recreate();
            }
            else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                performCrop(selectedImageUri);
            }


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


    @Nullable
    private String getPath(Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor;

            try {
                cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
                cursor.close(); // added this line, if something went wrong than just delete this line;

            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /**
     * Method to save photo in storage from camera.
     * @param photo - bitmap which we had from extras.getParcelable
     */
    public void savePhoto(Bitmap photo) {
        String root = Environment.getExternalStorageDirectory().toString(); // path
        File myDir = new File(root + "/android/data/com.sashakhyzhun.gerzhiktattooink"); // folder
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


    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().withMaxSize(300, 300).start(getContext(), this);
    }


    private void handleCrop(int resultCode, Intent result) throws IOException {
        if (resultCode == RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            String path = null;
            try {
                path = getPath(uri);
            } catch (URISyntaxException e) {e.printStackTrace();}

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
            savePhoto(bitmap);
            getActivity().recreate();

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getContext(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void performCrop(Uri picUri) {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*"); // indicate image type and Uri
            cropIntent.putExtra("crop", "true");          // set crop properties
            cropIntent.putExtra("aspectX", 4);            // indicate aspect of desired crop
            cropIntent.putExtra("aspectY", 2);            // indicate output X and Y
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 500);
            cropIntent.putExtra("return-data", true);     // retrieve data on return
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException e) {
            Toast toast = Toast.makeText(getContext(), "This device doesn't support the crop action!", Toast.LENGTH_LONG);
            toast.show();
        }
    }


    private boolean checkPermissions() {
        int res = getContext().checkCallingOrSelfPermission(READ_EXTERNAL_STORAGE);
        return (res == PackageManager.PERMISSION_GRANTED);
    }



}
