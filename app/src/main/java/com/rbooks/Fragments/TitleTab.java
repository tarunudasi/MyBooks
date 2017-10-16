package com.rbooks.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rbooks.Adapter.SearchAdapterTitle;
import com.rbooks.AppVolley.SingletonVolley;
import com.rbooks.Model.Search;
import com.rbooks.R;
import com.rbooks.Utility.UrlConstant;
import com.rbooks.listener.OnClickListener;

import static android.app.Activity.RESULT_OK;

public class TitleTab extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapterTitle adapter;
    private List<Search> list;
    private static final String VOLLEY_TAG = "TITLE_VT";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.title_tab, container, false);

        list = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.search_list_view_title);
        adapter = new SearchAdapterTitle(list, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter.setOnItemClick(new OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Intent intent = new Intent();
                intent.putExtra("search", list.get(position).getSearch());
                intent.putExtra("type","name");
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }
        });

        return view;
    }

    public void search(String data){
        if (data.trim().length()>1)
            getData(data);
    }

    private void getData(final String data){

        SingletonVolley.getInstance(getContext()).getRequestQueue().cancelAll(VOLLEY_TAG);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.SEARCH_BY_TITLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equals("unsuccessful")){
                            return;
                        }

                        list.clear();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("search");

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                list.add(new Search(object.getString("name")));
                                adapter.notifyDataSetChanged();
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
                params.put("search", data);
                return params;
            }
        };

        stringRequest.setTag(VOLLEY_TAG);
        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
}
