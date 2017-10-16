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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.victor.loading.book.BookLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rbooks.Activities.BookDescription;
import com.rbooks.Adapter.ViewBooksRecyclerAdapter;
import com.rbooks.AppVolley.SingletonVolley;
import com.rbooks.MainActivity;
import com.rbooks.Model.Books;
import com.rbooks.R;
import com.rbooks.SharedPrefs.LocationPrefs;
import com.rbooks.Utility.UrlConstant;
import com.rbooks.listener.OnClickListener;

public class SearchResult extends Fragment{

    private RecyclerView recyclerView;
    private ViewBooksRecyclerAdapter adapter;
    private List<Object> list;
    private int sorting = 2;
    private ProgressDialog progressDialog;
    private TextView textView;
    private String type = null;
    private String data = null;
    private RelativeLayout search_books_unavailable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            data = bundle.getString("data");
        }

        search_books_unavailable = (RelativeLayout) view.findViewById(R.id.search_books_unavailable);

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

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        adapter = new ViewBooksRecyclerAdapter(list, getContext(), getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.view_books_search_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        textView = (TextView) view.findViewById(R.id.books_search_distance_tv);
        textView.setText(LocationPrefs.getInstance(getContext()).getDistance() + " KM");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {" 10 KM "," 20 KM "," 50 KM"," 100 KM", " 200 KM"};

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
                            default:
                                distance = 10;
                        }

                        LocationPrefs.getInstance(getContext()).setDistance(distance);
                        textView.setText(LocationPrefs.getInstance(getContext()).getDistance() + " KM");
                        getData(type, data);
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
                Books bookLists = (Books) list.get(position);
                Intent intent = new Intent(getActivity(), BookDescription.class);
                intent.putExtra("data", bookLists);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.slide_out);
            }
        });

        getData(type, data);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_books_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Search Result");
        ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
    }


    private void getSorting(){

        final CharSequence[] items = {" Low to High Price"," High to Low Price"," New to Old"," Old to New"};

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
        dialog.setSingleChoiceItems(items, sorting, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                sorting = which;
                getData(type, data);
                dialog.dismiss();

            }
        });

        final AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setGravity(Gravity.BOTTOM);
        alertDialog.show();

    }

    private void getData(final String type, final String data){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.GET_BOOKS_SEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();

                        list.clear();

                        adapter.notifyDataSetChanged();

                        if (response.equals("unsuccessful")){
                            search_books_unavailable.setVisibility(View.VISIBLE);
                            return;
                        }

                        search_books_unavailable.setVisibility(View.GONE);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("books");

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                list.add(new Books(object.getString("id"),object.getString("name"),object.getString("amount"),object.getString("description"),object.getString("author"),object.getString("publication"),object.getString("img"),object.getString("user"), object.getString("latitude"), object.getString("longitude")));
                                adapter.notifyDataSetChanged();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("latitude", String.valueOf(LocationPrefs.getInstance(getContext()).getLocation().getLatitude()));
                params.put("longitude", String.valueOf(LocationPrefs.getInstance(getContext()).getLocation().getLongitude()));
                params.put("distance", String.valueOf(LocationPrefs.getInstance(getContext()).getDistance()));
                params.put("sorting", String.valueOf(sorting));
                params.put("data", data);
                params.put("type", type);
                return params;
            }
        };

        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);



    }



}
