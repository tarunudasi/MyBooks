package com.rbooks.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.victor.loading.book.BookLoading;

import java.util.HashMap;
import java.util.Map;

import com.rbooks.AppVolley.SingletonVolley;
import com.rbooks.MainActivity;
import com.rbooks.MyReceiver.ConnectivityReceiver;
import com.rbooks.R;
import com.rbooks.SharedPrefs.UserData;
import com.rbooks.Utility.UrlConstant;

public class ChangePassword extends Fragment {

    private EditText password_change, new_password_change, confirm_password_change;
    private Button change_password_btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        LinearLayout change_password_layout = (LinearLayout) view.findViewById(R.id.change_password_layout);
        ViewGroup.LayoutParams params = change_password_layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = 0;
        params.width = (int)((float)displayMetrics.widthPixels * 0.75);
        change_password_layout.setLayoutParams(params);

        password_change = (EditText) view.findViewById(R.id.password_change);
        new_password_change = (EditText) view.findViewById(R.id.new_password_change);
        confirm_password_change = (EditText) view.findViewById(R.id.confirm_password_change);

        change_password_btn = (Button) view.findViewById(R.id.change_password_btn);

        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (password_change.getText().length() < 1){
                    password_change.setError("Insert Password");
                    return;
                }else if (new_password_change.getText().length() < 1){
                    new_password_change.setError("Insert New Password");
                    return;
                }else if (confirm_password_change.getText().length() < 1){
                    confirm_password_change.setError("Insert Confirm Password");
                    return;
                }else if (!new_password_change.getText().toString().equals(confirm_password_change.getText().toString())){
                    Toast.makeText(getContext(),"Password Mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ConnectivityReceiver.isConnected()){
                    changePassword(password_change.getText().toString(), new_password_change.getText().toString());
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

        return view;
    }

    private void changePassword(final String password, final String newPassword){

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.CHANGE_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equals("success")){
                            getActivity().onBackPressed();
                            Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_LONG ).show();
                        }else if (response.trim().equals("unsuccessful")){
                            password_change.setError("Wrong Password");
                        }

                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG ).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("email", UserData.getInstance(getContext()).getUser().getEmail());
                params.put("password", password);
                params.put("new_password", newPassword);
                return params;
            }
        };

        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                stringRequest.cancel();
            }
        });

        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingletonVolley.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Change Password");
        ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }


}
