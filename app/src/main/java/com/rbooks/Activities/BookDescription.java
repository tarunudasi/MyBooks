package com.rbooks.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rbooks.AppVolley.SingletonVolley;
import com.rbooks.Model.Books;
import com.rbooks.MyReceiver.ConnectivityReceiver;
import com.rbooks.R;
import com.rbooks.Utility.UrlConstant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookDescription extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    private TextView book_description, contact_person, contact_number, book_cost, book_author, book_publication, book_location;
    private CardView book_seller_contact_details, call_now;
    private AdView mAdView, adViewLarge;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);

        mAdView = (AdView) findViewById(R.id.adView);
        adViewLarge = (AdView) findViewById(R.id.adViewLarge);

        adRequest = new AdRequest.Builder()
                .build();

        mAdView.loadAd(adRequest);
        adViewLarge.loadAd(adRequest);

//        mAdView.setAdListener(new AdListener(){
//            @Override
//            public void onAdLoaded() {
//                mAdView.setVisibility(View.VISIBLE);
//                super.onAdLoaded();
//            }
//
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Books bookLists = (Books) getIntent().getSerializableExtra("data");
        getSupportActionBar().setTitle(bookLists.getName());


        if (!ConnectivityReceiver.isConnected()){
            final Dialog alert = new Dialog(this, R.style.full_screen_dialog);
            alert.setContentView(R.layout.internet_customize_alert);
            alert.setCancelable(true);
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
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
                        try {
                            getCity(Double.parseDouble(bookLists.getLatitude()), Double.parseDouble(bookLists.getLongitude()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        getUserDetails(bookLists.getUser());
                    }
                }
            });

            alert.show();
        }

        ImageView imageView = (ImageView) findViewById(R.id.image_collapsing);
        Picasso.with(this)
                .load(bookLists.getImg())
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        book_description = (TextView) findViewById(R.id.book_description);
        contact_person = (TextView) findViewById(R.id.contact_person);
        contact_number = (TextView) findViewById(R.id.contact_number);
        book_cost = (TextView) findViewById(R.id.book_cost);
        book_author = (TextView) findViewById(R.id.book_author);
        book_publication = (TextView) findViewById(R.id.book_publication);
        book_location = (TextView) findViewById(R.id.book_location);
        book_seller_contact_details = (CardView) findViewById(R.id.book_seller_contact_details);
        call_now = (CardView) findViewById(R.id.call_now);

        call_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_no = contact_number.getText().toString().replaceAll("-", "");
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+phone_no));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(BookDescription.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });

        try {
            getCity(Double.parseDouble(bookLists.getLatitude()), Double.parseDouble(bookLists.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        book_author.setText(bookLists.getAuthor());
        book_publication.setText(bookLists.getPublication());
        book_cost.setText(bookLists.getAmount() + " /-");
        book_description.setText(bookLists.getDescription());

        getUserDetails(bookLists.getUser());

//        contact_number.setText(bookLists.getContact_number());
//        contact_person.setText(bookLists.getContact_person());

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void getCity(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

        /*String outputAddress = null;
        for(Address add : addresses) {
            *//*for(int i = 0; i < add.getMaxAddressLineIndex(); i++) {
                outputAddress += " --- " + add.getAddressLine(i);
            }*//*
            if (add.getMaxAddressLineIndex() - 2 > 0){
                outputAddress = add.getAddressLine(add.getMaxAddressLineIndex() - 2);
            }
        }*/

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

        book_location.setText(loc);

        //Log.e(TAG, city + state + country);

    }

    private void getUserDetails(final String user){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.GET_BOOK_SELLER_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("unsuccessful")){
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("seller");

                            JSONObject object = jsonArray.getJSONObject(0);

                            book_seller_contact_details.setVisibility(View.VISIBLE);
                            call_now.setVisibility(View.VISIBLE);

                            contact_person.setText(object.getString("name"));
                            contact_number.setText(object.getString("mobile"));


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
                Map<String,String> params= new HashMap<>();
                params.put("user", user);
                return params;
            }
        };

        SingletonVolley.getInstance(this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            mAdView.loadAd(adRequest);
            adViewLarge.loadAd(adRequest);
        }
    }
}
