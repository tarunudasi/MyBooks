package debugbridge.mybooks.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

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

import debugbridge.mybooks.Activities.MyBookDescription;
import debugbridge.mybooks.Adapter.MyBooksRecyclerAdapter;
import debugbridge.mybooks.AppVolley.SingletonVolley;
import debugbridge.mybooks.MainActivity;
import debugbridge.mybooks.Model.Books;
import debugbridge.mybooks.MyReceiver.ConnectivityReceiver;
import debugbridge.mybooks.R;
import debugbridge.mybooks.SharedPrefs.UserData;
import debugbridge.mybooks.Utility.UrlConstant;
import debugbridge.mybooks.listener.OnClickListener;

import static android.app.Activity.RESULT_OK;

public class MyBooks extends Fragment {

    private RecyclerView recyclerView;
    private MyBooksRecyclerAdapter adapter;
    private List<Object> list;
    private final int REQUEST_CODE = 5689;
    private LinearLayout no_books_for_sell;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_books, container, false);

        no_books_for_sell = (LinearLayout) view.findViewById(R.id.no_books_for_sell);

        list = new ArrayList<>();
        adapter = new MyBooksRecyclerAdapter(list, getContext(), getActivity());
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_books_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if (ConnectivityReceiver.isConnected()){
            getMyBooks();
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
                        getMyBooks();
                    }
                }
            });

            alert.show();
        }


        adapter.setOnItemClick(new OnClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                Books bookLists = (Books) list.get(position);
                Intent intent = new Intent(getActivity(), MyBookDescription.class);
                intent.putExtra("data", bookLists);
                startActivityForResult(intent, REQUEST_CODE);
                getActivity().overridePendingTransition(R.anim.right_enter,R.anim.slide_out);
            }
        });

        return view;
    }

    private void getMyBooks(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.GET_USER_BOOKS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        no_books_for_sell.setVisibility(View.GONE);
                        list.clear();
                        adapter.notifyDataSetChanged();

                        if (response.equals("unsuccessful")){
                            no_books_for_sell.setVisibility(View.VISIBLE);
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("books");

                            for (int i = 0 ; i < jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                list.add(new Books(object.getString("id"),object.getString("name"),object.getString("amount"),object.getString("description"),object.getString("author"),object.getString("publication"),object.getString("img"),object.getString("user"), object.getString("latitude"), object.getString("longitude"), object.getString("verify"), object.getString("sold")));
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
                params.put("email", UserData.getInstance(getContext()).getUser().getEmail());
                return params;
            }
        };

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                stringRequest.cancel();
                getActivity().onBackPressed();
            }
        });

        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            getMyBooks();
        }
    }

    public void onPrepareOptionsMenu(Menu menu) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Books");
        ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);


    }

}
