package debugbridge.mybooks.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import debugbridge.mybooks.Activities.ChangeLocation;
import debugbridge.mybooks.Activities.SearchableActivity;
import debugbridge.mybooks.Adapter.CategoryRecyclerAdapter;
import debugbridge.mybooks.AppVolley.SingletonVolley;
import debugbridge.mybooks.MainActivity;
import debugbridge.mybooks.Model.MainCategory;
import debugbridge.mybooks.Model.Slidder;
import debugbridge.mybooks.MyReceiver.ConnectivityReceiver;
import debugbridge.mybooks.R;
import debugbridge.mybooks.SharedPrefs.LocationPrefs;
import debugbridge.mybooks.Utility.UrlConstant;
import debugbridge.mybooks.listener.OnClickListener;

import static android.app.Activity.RESULT_OK;

public class RBooks extends Fragment {

    private RecyclerView category_recycler_view;
    private List<Object> list;
    private List<String> img;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    private final int SEARCH_REQUEST = 2222;
    private final int CHANGE_LOCATION = 4504;
    private SliderLayout mDemoSlider;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_rbooks,container,false);


        if (!ConnectivityReceiver.isConnected()){
            final Dialog alert = new Dialog(getContext(), R.style.full_screen_dialog);
            alert.setContentView(R.layout.internet_customize_alert);
            alert.setCancelable(true);
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    getActivity().finish();
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
                        initialize(view);
                    }
                }
            });

            alert.show();
        }else {
            initialize(view);
        }


        return view;
    }

    private void initialize(View view){
        img = new ArrayList<>();
        getCategory();
        list = new ArrayList<>();

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(list,getContext());

        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Stack);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);

        getSlider();

        category_recycler_view = (RecyclerView) view.findViewById(R.id.category_recycler_view);
        category_recycler_view.setHasFixedSize(true);
        category_recycler_view.setItemAnimator(new DefaultItemAnimator());
        category_recycler_view.setAdapter(categoryRecyclerAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        category_recycler_view.setLayoutManager(mLayoutManager);
        category_recycler_view.setNestedScrollingEnabled(false);
        categoryRecyclerAdapter.setOnItemClick(new OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                MainCategory mainCategory = (MainCategory) list.get(position);
                Fragment fragment = new BooksList();
                Bundle bundle = new Bundle();
                bundle.putString("id", mainCategory.getId());
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.add(R.id.home_content, fragment);
                fragmentTransaction.addToBackStack(RBooks.class.getName());
                fragmentTransaction.commit();
            }
        });
    }

    private void getSlider(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlConstant.SLIDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.equals("unsuccessful")){
                            return;
                        }

//                        list.clear();
//
                        img.clear();
//
//                        categoryRecyclerAdapter.notifyDataSetChanged();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("slider");

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                img.add(object.getString("img"));
                            }

//                            list.add(new Slidder(img));
//
//                            categoryRecyclerAdapter.notifyDataSetChanged();
//
//                            getCategory();


                            Slidder slidder = (Slidder) new Slidder(img);

                            HashMap<String,String> url_maps = new HashMap<>();

                            for (int i = 0 ; i < slidder.getList().size() ; i++){
                                url_maps.put(i+1+"",slidder.getList().get(i).replace(" ","%20"));
                            }

                            for(String name : url_maps.keySet()){
                                DefaultSliderView textSliderView = new DefaultSliderView(getContext());
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra",name);
                                mDemoSlider.addSlider(textSliderView);
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
            /*@Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new String(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }*/
        };

        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

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

                            list.clear();

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                list.add(new MainCategory(object.getString("id"),object.getString("name"),object.getString("img")));
                                categoryRecyclerAdapter.notifyDataSetChanged();
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
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new String(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }
        };

        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (ConnectivityReceiver.isConnected()) {

            try {
                getCity(LocationPrefs.getInstance(getContext()).getLocation().getLatitude(),
                        LocationPrefs.getInstance(getContext()).getLocation().getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("R Books");
            ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(null);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        if (!ConnectivityReceiver.isConnected()){
            final Dialog alert = new Dialog(getContext(), R.style.full_screen_dialog);
            alert.setContentView(R.layout.internet_customize_alert);
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

            return super.onOptionsItemSelected(item);

        }else {
            switch (item.getItemId()) {
                case R.id.action_search:
                    startActivityForResult(new Intent(getActivity(), SearchableActivity.class), SEARCH_REQUEST);
                    getActivity().overridePendingTransition(R.anim.right_enter, R.anim.slide_out);
                    return true;
                case R.id.action_location:
                    startActivityForResult(new Intent(getActivity(), ChangeLocation.class), CHANGE_LOCATION);
                    getActivity().overridePendingTransition(R.anim.right_enter, R.anim.slide_out);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == SEARCH_REQUEST){
            String type = null;
            String search;
            if (data.getStringExtra("type").equals("name")){
                type = data.getStringExtra("type");
            }else if (data.getStringExtra("type").equals("author")){
                type = data.getStringExtra("type");
            }else if (data.getStringExtra("type").equals("publication")){
                type = data.getStringExtra("type");
            }
            search = data.getStringExtra("search");
            getSearchResult(search, type);
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

    private void getSearchResult(String data, String type){

        Fragment fragment = new SearchResult();
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.add(R.id.home_content, fragment);
        fragmentTransaction.addToBackStack(RBooks.class.getName());
        fragmentTransaction.commit();

    }

    private void getCity(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());

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

        if (city != null){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle(city);
        }
        if (state != null){
            if (city == null){
                ((MainActivity)getActivity()).getSupportActionBar().setTitle(state);
            }else {
                ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(state);
            }

        }

        if (country != null){
            if (state == null && city == null){
                ((MainActivity)getActivity()).getSupportActionBar().setTitle(country);
            }else if (state == null){
                ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(country);
            }else {
                ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(state + ", " + country);
            }
        }

        if (country == null && state == null & city == null){
            ((MainActivity)getActivity()).getSupportActionBar().setTitle("R Books");
        }

        //Log.e(TAG, city + state + country);

    }



}
