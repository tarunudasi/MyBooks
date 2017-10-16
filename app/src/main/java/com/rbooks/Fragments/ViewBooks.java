package com.rbooks.Fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rbooks.Activities.BookDescription;
import com.rbooks.Adapter.ViewBooksRecyclerAdapter;
import com.rbooks.AppVolley.SingletonVolley;
import com.rbooks.MainActivity;
import com.rbooks.Model.Books;
import com.rbooks.R;
import com.rbooks.SharedPrefs.LocationPrefs;
import com.rbooks.Utility.UrlConstant;
import com.rbooks.listener.EndlessRecyclerViewScrollListener;
import com.rbooks.listener.OnClickListener;
import com.victor.loading.book.BookLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewBooks extends Fragment{

    private RecyclerView recyclerView;
    private ViewBooksRecyclerAdapter adapter;
    private List<Object> list;
    private String id = "", title = "";
    private int sorting = 2;
    private ProgressDialog progressDialog;
    private TextView textView;
    private LinearLayout books_unavailable;
    boolean loadingMore = false;
    private ArrayList<Object> newList=new ArrayList<>();
    private int currentPage = 0;
    private AdView adViewItemNoBooks;
    private AdRequest adRequest;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_view_books, container, false);

        ((MainActivity)getActivity()).showBottomNavigation();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
            title = bundle.getString("title");
        }

        LocationPrefs.getInstance(getContext()).setDistance(10);

        books_unavailable = (LinearLayout) view.findViewById(R.id.books_unavailable);
        adViewItemNoBooks = (AdView) view.findViewById(R.id.adViewItemNoBooks);

        progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                getActivity().onBackPressed();
            }
        });

        list = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(adapter.getItemViewType(position)){
                    case 0:
                        return 2;
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                    case 3:
                        return 2;
                    default:
                        return -1;
                }
            }
        });
        adapter = new ViewBooksRecyclerAdapter(list, getContext(), getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.view_books_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadingMore = true;
                getBooks(page+1);
                int curlsize = adapter.getItemCount();
                list.addAll(newList);
                adapter.notifyItemRangeChanged(curlsize, list.size()-2);
            }
        });

        textView = (TextView) view.findViewById(R.id.books_distance_tv);
        textView.setText(LocationPrefs.getInstance(getContext()).getDistance() + " KM");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {" 10 KM "," 20 KM "," 50 KM"," 100 KM", " 200 KM"," 500 KM", " 1000 KM"};
                int dist = LocationPrefs.getInstance(getContext()).getDistance();
                int pos;

                switch (dist){
                    case 20:
                        pos = 1;
                        break;
                    case 50:
                        pos = 2;
                        break;
                    case 100:
                        pos = 3;
                        break;
                    case 200:
                        pos = 4;
                        break;
                    case 500:
                        pos = 5;
                        break;
                    case 1000:
                        pos = 6;
                        break;
                    default:
                        pos = 0;
                }

                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                dialog.setSingleChoiceItems(items, pos, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int distance;

                                switch (which) {
                                    case 1:
                                        distance = 20;
                                        break;
                                    case 2:
                                        distance = 50;
                                        break;
                                    case 3:
                                        distance = 100;
                                        break;
                                    case 4:
                                        distance = 200;
                                        break;
                                    case 5:
                                        distance = 500;
                                        break;
                                    case 6:
                                        distance = 1000;
                                        break;
                                    default:
                                        distance = 10;
                                }

                                LocationPrefs.getInstance(getContext()).setDistance(distance);
                                textView.setText(LocationPrefs.getInstance(getContext()).getDistance() + " KM");
                                list.clear();
                                adapter.notifyDataSetChanged();
                                getBooks(1);
                                currentPage = 0;
                                dialog.dismiss();

                            }
                        });

                final AlertDialog alertDialog = dialog.create();
                alertDialog.getWindow().setGravity(Gravity.BOTTOM);
                alertDialog.show();

            }
        });

        adapter.setOnItemClick(new OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {

                if (list.get(position) instanceof Books){
                    Books bookLists = (Books) list.get(position);
                    Intent intent = new Intent(getActivity(), BookDescription.class);
                    intent.putExtra("data", bookLists);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_enter,R.anim.slide_out);
                }

                if (list.get(position) instanceof String) {

                    if (view.getId() == R.id.retry_internet) {
                        if (list.contains("No Internet"))
                            list.remove("No Internet");

                        list.add(null);

                        adapter.notifyDataSetChanged();

                        getBooks(currentPage+1);
                    }
                }


            }
        });

        getBooks(1);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(title);
        ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

        menu.findItem(R.id.action_sorting).setVisible(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_books_menu, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_sorting:
                getSorting();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getSorting(){

        final CharSequence[] items = {" Low to High Price"," High to Low Price"," New to Old"," Old to New"};

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
        dialog.setSingleChoiceItems(items, sorting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                sorting = which;
                list.clear();
                adapter.notifyDataSetChanged();
                getBooks(1);
                currentPage = 0;
                dialog.dismiss();

            }
        });
        final AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

    }

    private void getBooks(final int page){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.GETBOOKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        currentPage = page;

                        progressDialog.dismiss();

                        //list.clear();

                        if (response.equals("unsuccessful")){
                            booksUnavailable();
                            return;
                        }

                        if (list.contains(null)){
                            list.remove(null);
                            list.add("AdMob");
                        }

                        if (list.contains("No Internet"))
                            list.remove("No Internet");

                        adapter.notifyItemRemoved(list.size());
                        adapter.notifyDataSetChanged();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("books");

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                list.add(new Books(object.getString("id"),object.getString("name"),object.getString("amount"),object.getString("description"),object.getString("author"),object.getString("publication"),object.getString("img"),object.getString("user"), object.getString("latitude"), object.getString("longitude")));
                                adapter.notifyDataSetChanged();
                            }

                            list.add(null);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (list.isEmpty()){
                            booksUnavailable();
                        }else {
                            booksAvailable();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "onErrorResponse: ", error);

                if (list.contains(null))
                    list.remove(null);

                if (!list.contains("No Internet"))
                    list.add("No Internet");

                adapter.notifyDataSetChanged();

                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("latitude", String.valueOf(LocationPrefs.getInstance(getContext()).getLocation().getLatitude()));
                params.put("longitude", String.valueOf(LocationPrefs.getInstance(getContext()).getLocation().getLongitude()));
                params.put("distance", String.valueOf(LocationPrefs.getInstance(getContext()).getDistance()));
                params.put("category", id);
                params.put("sorting", String.valueOf(sorting));
                params.put("page", String.valueOf(page));
                return params;
            }
        };

        stringRequest.setShouldCache(false);

        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private void booksUnavailable(){
        books_unavailable.setVisibility(View.VISIBLE);
        adRequest = new AdRequest.Builder()
                .build();

        adViewItemNoBooks.loadAd(adRequest);

    }

    private void booksAvailable(){
        books_unavailable.setVisibility(View.GONE);
    }


}
