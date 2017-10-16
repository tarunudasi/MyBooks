package com.rbooks.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.rbooks.MyReceiver.ConnectivityReceiver;
import com.rbooks.R;
import com.rbooks.Utility.UrlConstant;

public class PasswordRecovery extends Fragment{

    private TextView forgot_password_login;
    private Button recover_password;
    private EditText forgot_email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_recovery, container, false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        LinearLayout password_recovery_layout = (LinearLayout) view.findViewById(R.id.password_recovery_layout);
        ViewGroup.LayoutParams params = password_recovery_layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = 0;
        params.width = (int)((float)displayMetrics.widthPixels * 0.75);
        password_recovery_layout.setLayoutParams(params);

        init(view);
        return view;
    }

    public boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void init(View view){
        forgot_password_login = (TextView) view.findViewById(R.id.forgot_password_login);
        recover_password = (Button) view.findViewById(R.id.recover_password);
        forgot_email = (EditText) view.findViewById(R.id.forgot_email);

        forgot_password_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        recover_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(isValidEmail(forgot_email.getText()) && forgot_email.getText().toString().length() > 0)){
                    forgot_email.setError("Invalid Email Address");
                    return;
                }

                if (ConnectivityReceiver.isConnected()) {
                    forgotPassword(forgot_email.getText().toString());
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

    }

    private void sentEmailDialog(){
        final Dialog alert = new Dialog(getContext(), R.style.full_screen_dialog);
        alert.setContentView(R.layout.password_recovery_alert);
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
        Button button = (Button) alert.getWindow().findViewById(R.id.ok_email_sent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private void forgotPassword(final String email){

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.RECOVERY_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            getActivity().onBackPressed();
                            sentEmailDialog();
                            progressDialog.dismiss();
                        }else if (response.trim().equals("unsuccessful")){
                            progressDialog.dismiss();
                            forgot_email.setError("Invalid Email Address");
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_LONG).show();
                        }
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
                params.put("email", email);
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

}
