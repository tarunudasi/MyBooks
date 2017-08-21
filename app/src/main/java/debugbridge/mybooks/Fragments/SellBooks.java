package debugbridge.mybooks.Fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import debugbridge.mybooks.MainActivity;
import debugbridge.mybooks.R;

import static android.app.Activity.RESULT_OK;

public class SellBooks extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final int RESULT_LOAD_IMG = 111;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    Spinner category,subcategory;
     EditText description;
    AppCompatImageView sell_book;
    Context applicationContext;
    FloatingActionButton upload_book_image;
    private static final String TAG = "sell_fragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_books, container, false);



      category = (Spinner) view.findViewById(R.id.category);
         description = (EditText) view.findViewById(R.id.description);
         sell_book = (AppCompatImageView) view.findViewById(R.id.bookimage);
         upload_book_image = (FloatingActionButton) view.findViewById(R.id.fab);
        subcategory=(Spinner)view.findViewById(R.id.subcategory);


        upload_book_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                getImageFromAlbum();
            }
        });


        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Competitive");
        categories.add("Diploma");
        categories.add("Engineering");
        categories.add("High School");
        categories.add("Medical");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        // attaching data adapter to spinner
        category.setAdapter(dataAdapter);
        category.setOnItemSelectedListener(this);


        return view;


    }


    private void getImageFromAlbum() {

            final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Take Photo")) {


                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                        intent.putExtra(MediaStore.EXTRA_OUTPUT, 1);

                        // start the image capture Intent
                        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                    }
                     else if (options[item].equals("Choose from Gallery")) {
                        try {
                            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                            photoPickerIntent.setType("image/*");
                            startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Image"), RESULT_LOAD_IMG);
                        } catch (Exception exp) {
                            Log.i("Error", exp.toString());
                        }
                    }
                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
        builder.show();
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String sp1= String.valueOf(category.getSelectedItem());

        if(sp1.contentEquals("Competitive")) {
            List<String> list = new ArrayList<String>();
            list.add("Quantitative");
            list.add("Reasoning");
            list.add("GA");
            list.add("Banking");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            dataAdapter.notifyDataSetChanged();
            subcategory.setAdapter(dataAdapter);
        }
        if(sp1.contentEquals("Diploma")) {
            List<String> list = new ArrayList<String>();
            list.add("hi");
            list.add("hello");
            list.add("wht");
            list.add("Bank");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            subcategory.setAdapter(dataAdapter);
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMG) {
                try {    // Get the url from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri) {
                        // Get the path from the Uri
                        String path = getPathFromURI(selectedImageUri);
                        Log.i(TAG, "Image Path : " + path);

                        applicationContext = MainActivity.getContextOfApplication();

                        final InputStream imageStream = applicationContext.getContentResolver().openInputStream(selectedImageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        sell_book.setImageBitmap(selectedImage);
                        // Set the image in ImageView
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
   /*******         else if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);

                    sell_book.setImageBitmap(bitmap);

                }catch (Exception e){e.printStackTrace();}
            }
        }
    }/***********
    /* Get the real path from the URI */
        }
    }
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        applicationContext = MainActivity.getContextOfApplication();
        Cursor cursor = applicationContext.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }



}



