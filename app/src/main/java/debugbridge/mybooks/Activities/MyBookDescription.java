package debugbridge.mybooks.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.victor.loading.book.BookLoading;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import debugbridge.mybooks.AppVolley.SingletonVolley;
import debugbridge.mybooks.Model.Books;
import debugbridge.mybooks.R;
import debugbridge.mybooks.Utility.UrlConstant;


public class MyBookDescription extends AppCompatActivity {

    private TextView book_description, book_cost, book_author, book_publication, book_location;
    private Button my_book_sold_out_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_book_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Books bookLists = (Books) getIntent().getSerializableExtra("data");
        getSupportActionBar().setTitle(bookLists.getName());

        ImageView imageView = (ImageView) findViewById(R.id.my_book_image_collapsing);
        Picasso.with(this)
                .load(bookLists.getImg())
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        book_description = (TextView) findViewById(R.id.my_book_description);
        book_cost = (TextView) findViewById(R.id.my_book_cost);
        book_author = (TextView) findViewById(R.id.my_book_author);
        book_publication = (TextView) findViewById(R.id.my_book_publication);
        book_location = (TextView) findViewById(R.id.my_book_location);
        my_book_sold_out_btn = (Button) findViewById(R.id.my_book_sold_out_btn);

        if (bookLists.getVerify().trim().equals("1")){
            my_book_sold_out_btn.setVisibility(View.VISIBLE);
        }else {
            my_book_sold_out_btn.setVisibility(View.GONE);
        }

        if (bookLists.getSold().equals("1")){
            my_book_sold_out_btn.setVisibility(View.GONE);
        }

        my_book_sold_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soldOut(bookLists.getId());
            }
        });

        Log.e("id",bookLists.getId());

        try {
            getCity(Double.parseDouble(bookLists.getLatitude()), Double.parseDouble(bookLists.getLongitude()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        book_author.setText(bookLists.getAuthor());
        book_publication.setText(bookLists.getPublication());
        book_cost.setText(bookLists.getAmount() + " /-");
        book_description.setText(bookLists.getDescription());

    }

    private void soldOut(final String id){

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.BOOK_SOLD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            setResult(RESULT_OK);
                            finish();
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Try Later", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                stringRequest.cancel();
                finish();
            }
        });

        SingletonVolley.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

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

}

