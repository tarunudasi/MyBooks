package com.rbooks.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rbooks.Activities.ChangeLocation;
import com.rbooks.Adapter.SpinnerCustomArrayAdapter;
import com.rbooks.AppVolley.SingletonVolley;
import com.rbooks.MainActivity;
import com.rbooks.Model.MainCategory;
import com.rbooks.Model.SubCategory;
import com.rbooks.MyReceiver.ConnectivityReceiver;
import com.rbooks.R;
import com.rbooks.SharedPrefs.LocationPrefs;
import com.rbooks.SharedPrefs.UserData;
import com.rbooks.Utility.ImageHelper;
import com.rbooks.Utility.UrlConstant;
import com.victor.loading.book.BookLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class SellBooks extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner category,subcategory;
    private EditText description, title, amount, author, publication;
    private AppCompatImageView sell_book;
    public static final int IMAGE_REQUEST_CODE = 9162;
    private Bitmap bitmapImage = null;
    private List<MainCategory> mainCategoryList;
    private List<SubCategory> subCategoryList;
    private List<String> categories, subCategory;
    private ArrayAdapter<String> categoryDataAdapter;
    private ArrayAdapter<String> subCategoryDataAdapter;
    private Button button;
    private TextView sell_location;
    private final int CHANGE_LOCATION = 4500;
    private InterstitialAd mInterstitialAd;
    private AdRequest adRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void getCity(double latitude, double longitude) throws IOException {
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder = new Geocoder(getContext(), Locale.getDefault());
//
//        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
//
//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//
//        /*String outputAddress = null;
//        for(Address add : addresses) {
//            *//*for(int i = 0; i < add.getMaxAddressLineIndex(); i++) {
//                outputAddress += " --- " + add.getAddressLine(i);
//            }*//*
//            if (add.getMaxAddressLineIndex() - 2 > 0){
//                outputAddress = add.getAddressLine(add.getMaxAddressLineIndex() - 2);
//            }
//        }*/


        String city = LocationPrefs.getInstance(getContext()).getCity().getCity();
        String state = LocationPrefs.getInstance(getContext()).getCity().getState();
        String country = LocationPrefs.getInstance(getContext()).getCity().getCountry();

        String loc = "";
        if (city != null){
            loc += city;
        }
        if (state != null){
            if (city == null){
                loc += state;
            }else {
                loc += ", " + state;
            }

        }

        if (country != null){
            if (state == null && city == null){
                loc += country;
            }else {
                loc += ", " + country;
            }
        }

        sell_location.setText(loc);

        //Log.e(TAG, city + state + country);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell_books, container, false);

        mInterstitialAd = new InterstitialAd(getActivity());

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.ad_mob_full_screen));

        adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });

        category = (Spinner) view.findViewById(R.id.category);
        description = (EditText) view.findViewById(R.id.description);
        sell_book = (AppCompatImageView) view.findViewById(R.id.bookimage);
        subcategory=(Spinner)view.findViewById(R.id.subcategory);
        title = (EditText) view.findViewById(R.id.book_title);
        amount = (EditText) view.findViewById(R.id.sell_amount);
        button = (Button) view.findViewById(R.id.sell_books);
        author = (EditText) view.findViewById(R.id.book_author);
        publication = (EditText) view.findViewById(R.id.book_publication);
        sell_location = (TextView) view.findViewById(R.id.sell_location);

        Uri imageUri = null;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            imageUri = Uri.parse(bundle.getString("imageUri"));
        }

        if (imageUri == null){
            getActivity().onBackPressed();
        }

        Bitmap bitmap;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            sell_book.setImageBitmap(ImageHelper.getResizedBitmapLessThanMaxSize(bitmap, 500));
            bitmapImage = ImageHelper.getResizedBitmapLessThanMaxSize(bitmap, 500);
        } catch (IOException e) {
            e.printStackTrace();
        }

        sell_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), ChangeLocation.class), CHANGE_LOCATION);
            }
        });

        try {
            getCity(LocationPrefs.getInstance(getContext()).getLocation().getLatitude(),
                    LocationPrefs.getInstance(getContext()).getLocation().getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = subCategoryList.get(subcategory.getSelectedItemPosition()).getId();
                String bookTitle = title.getText().toString();
                String bookDescription = description.getText().toString();
                String bookAmount = amount.getText().toString();
                String bookAuthor = author.getText().toString();
                String bookPubliction = publication.getText().toString();

                if (bookTitle.length() < 1){
                    title.setError("Enter Title");
                    return;
                }else if (bookAuthor.length() < 1){
                    author.setError("Write Author of book");
                    return;
                }else if (bookPubliction.length() < 1){
                    publication.setError("Type Publication Name");
                    return;
                }else if (bookDescription.length() < 1){
                    description.setError("Write Description");
                    return;
                }else if (bookAmount.length() < 1){
                    amount.setError("Amount not mentioned");
                    return;
                }else if (bitmapImage == null){
                    startActivityForResult(ImageHelper.getPickImageChooserIntent(getActivity()), IMAGE_REQUEST_CODE);
                    return;
                }

                if (ConnectivityReceiver.isConnected()){
                    insertData(id, bookTitle, bookDescription, bookAmount, bookAuthor, bookPubliction);
                }else {
                    final Dialog alert = new Dialog(getContext(), R.style.full_screen_dialog);
                    alert.setContentView(R.layout.internet_customize_alert);
                    alert.setCancelable(true);
                    alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            getActivity().onBackPressed();
                        }
                    });
                    alert.setCanceledOnTouchOutside(false);
                    alert.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT);
                    Button button = (Button) alert.getWindow().findViewById(R.id.try_again_internet_button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (ConnectivityReceiver.isConnected()){
                                alert.dismiss();
                            }
                        }
                    });

                    alert.show();
                }


            }
        });

        sell_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getImageFromAlbum();
                startActivityForResult(ImageHelper.getPickImageChooserIntent(getActivity()), IMAGE_REQUEST_CODE);
            }
        });

        mainCategoryList = new ArrayList<>();

        subCategoryList = new ArrayList<>();

        // Spinner Drop down elements
        categories = new ArrayList<>();

        subCategory = new ArrayList<>();

        getCategory();

        categoryDataAdapter = new SpinnerCustomArrayAdapter(getContext(), android.R.layout.simple_spinner_item, categories);
        //categoryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        category.setAdapter(categoryDataAdapter);
        category.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        getSubCategory(mainCategoryList.get(i).getId());

        subCategoryDataAdapter = new SpinnerCustomArrayAdapter(getContext(),android.R.layout.simple_spinner_item, subCategory);
        //subCategoryDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subCategoryDataAdapter.notifyDataSetChanged();
        subcategory.setAdapter(subCategoryDataAdapter);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = ImageHelper.getPickImageResultUri(getActivity(),data);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                sell_book.setImageBitmap(ImageHelper.getResizedBitmapLessThanMaxSize(bitmap, 500));
                bitmapImage = ImageHelper.getResizedBitmapLessThanMaxSize(bitmap, 500);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (resultCode == RESULT_OK && requestCode == CHANGE_LOCATION){
            try {
                getCity(LocationPrefs.getInstance(getContext()).getLocation().getLatitude(),
                        LocationPrefs.getInstance(getContext()).getLocation().getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getCategory(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConstant.CATEGORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("unsuccessful")){
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("categories");

                            mainCategoryList.clear();

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                mainCategoryList.add(new MainCategory(object.getString("id"),object.getString("name"),object.getString("img")));
                                categories.add(mainCategoryList.get(i).getName());
                                categoryDataAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private void getSubCategory(final String category){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.SUBCATEGORYBYID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("unsuccessful")){
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("subcategories");

                            subCategoryList.clear();
                            subCategory.clear();
                            subCategoryDataAdapter.notifyDataSetChanged();

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                subCategoryList.add(new SubCategory(object.getString("id"),object.getString("category"),object.getString("name"),object.getString("img")));
                                subCategory.add(subCategoryList.get(i).getTitle());
                                subCategoryDataAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("category",category);
                return params;
            }
        };

        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void refreshViews(){
        sell_book.setImageResource(R.drawable.download);
        title.setText("");
        amount.setText("");
        description.setText("");
        bitmapImage = null;
    }

    private void showThankYouMessage(){

        final Dialog alert = new Dialog(getContext(), R.style.full_screen_dialog);
        alert.setContentView(R.layout.thank_you_dialog);
        alert.setCancelable(true);
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                alert.dismiss();
            }
        });
        alert.setCanceledOnTouchOutside(false);
        alert.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        Button button = (Button) alert.getWindow().findViewById(R.id.thank_you_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterstitialAd.loadAd(adRequest);
                alert.dismiss();
            }
        });

        alert.show();

    }

    private void insertData(final String category_id, final String book_title, final String book_desc, final String book_amount, final String book_author, final String book_publication){

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.INSERTBOOK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equals("success")){
                            //refreshViews();
                            getActivity().onBackPressed();
                            showThankYouMessage();
                            progressDialog.dismiss();
                        }else if (response.trim().equals("unsuccess")){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Try Again !", Toast.LENGTH_LONG).show();
                        }
                        //clickable = true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //clickable = true;
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                bitmapImage = ((BitmapDrawable)sell_book.getDrawable()).getBitmap();
                params.put("email", UserData.getInstance(getContext()).getUser().getEmail());
                params.put("image", getStringImage(ImageHelper.getResizedBitmapLessThanMaxSize(bitmapImage,50)));
                params.put("category",category_id);
                params.put("title",book_title);
                params.put("author",book_author);
                params.put("publication", book_publication);
                params.put("desc",book_desc);
                params.put("amount",book_amount);
                params.put("longitude", String.valueOf(LocationPrefs.getInstance(getContext()).getLocation().getLongitude()));
                params.put("latitude", String.valueOf(LocationPrefs.getInstance(getContext()).getLocation().getLatitude()));
                return params;
            }
        };

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                stringRequest.cancel();
            }
        });


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        stringRequest.setShouldCache(false);
        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Sell Books");
        ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);


        for (int i = 0 ; i < menu.size(); i++){
            menu.getItem(i).setVisible(false);
        }

    }

}