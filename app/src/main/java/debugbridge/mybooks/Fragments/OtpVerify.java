package debugbridge.mybooks.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.victor.loading.book.BookLoading;

import java.util.HashMap;
import java.util.Map;

import debugbridge.mybooks.Activities.GetLocation;
import debugbridge.mybooks.AppVolley.SingletonVolley;
import debugbridge.mybooks.Model.User;
import debugbridge.mybooks.MyReceiver.MySMSBroadcastReceiver;
import debugbridge.mybooks.R;
import debugbridge.mybooks.SharedPrefs.UserData;
import debugbridge.mybooks.Utility.UrlConstant;
import debugbridge.mybooks.listener.OnSmsReceived;

public class OtpVerify extends Fragment{

    private String email, name, mobile;
    private EditText otp_number;
    private Button otp_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mobile = bundle.getString("mobile");
            email = bundle.getString("email");
            name = bundle.getString("name");
        }

        otp_button = (Button) view.findViewById(R.id.otp_button);
        otp_number = (EditText) view.findViewById(R.id.otp_number);

        otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp_number.getText().length() != 6){
                    otp_button.setError("Invalid OTP");
                    return;
                }

                verifyOTP(otp_number.getText().toString());

            }
        });

        getOtp();

        return view;
    }

    private void getOtp(){
        SmsRetrieverClient client = SmsRetriever.getClient(getContext());

// Starts SmsRetriever, waits for ONE matching SMS message until timeout
// (5 minutes).
        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task.
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                final MySMSBroadcastReceiver receiver = new MySMSBroadcastReceiver();
                getActivity().registerReceiver(receiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));
                receiver.setOnSmsReceivedListener(new OnSmsReceived() {
                    @Override
                    public void onOtpReceived(String otp) {
                        otp_number.setText(otp);
                        otp_button.performClick();
                        getActivity().unregisterReceiver(receiver);
                    }
                });
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("SMS Failed","Failed to start retriever");
            }
        });

    }

    private void verifyOTP(final String otp){

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.VERIFY_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            UserData.getInstance(getContext()).setLogin(new User(email, name, mobile, "1"));
                            openProfile();

                            return;
                        }else if (response.trim().equals("invalid")){
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),"Wrong OTP",Toast.LENGTH_LONG).show();
                            return;
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),response, Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("otp", otp);
                return params;
            }

        };

        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingletonVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void openProfile(){
        Intent login = new Intent(getActivity(), GetLocation.class);
        getActivity().startActivity(login);
        getActivity().overridePendingTransition(R.anim.right_enter, R.anim.slide_out);
        getActivity().finish();
    }

}
