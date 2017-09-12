package debugbridge.mybooks.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import debugbridge.mybooks.AppVolley.SingletonVolley;
import debugbridge.mybooks.MainActivity;
import debugbridge.mybooks.MyReceiver.ConnectivityReceiver;
import debugbridge.mybooks.MyReceiver.SMSBroadcastReceiver;
import debugbridge.mybooks.R;
import debugbridge.mybooks.SharedPrefs.UserData;
import debugbridge.mybooks.Utility.UrlConstant;
import debugbridge.mybooks.listener.OnSmsReceived;

public class NewOtpVerify extends Fragment {

    private EditText new_otp_number;
    private Button new_otp_button;
    private String mobile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_otp, container,false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mobile = bundle.getString("mobile");
        }

        Log.e("mobile",mobile);
        new_otp_button = (Button) view.findViewById(R.id.new_otp_button);
        new_otp_number = (EditText) view.findViewById(R.id.new_otp_number);

        new_otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_otp_number.getText().length() != 6){
                    new_otp_number.setError("Invalid OTP");
                    return;
                }

                if (ConnectivityReceiver.isConnected()) {
                    verifyOTP(new_otp_number.getText().toString());
                }else {
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
                            }
                        }
                    });

                    alert.show();
                }

            }
        });

        getOtp();

        return view;
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

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.CHANGE_MOBILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            UserData.getInstance(getContext()).changeMobile(mobile);
                            getActivity().onBackPressed();
                            progressDialog.dismiss();
                            return;
                        }else if (response.trim().equals("invalid")){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"Wrong OTP",Toast.LENGTH_LONG).show();
                            return;
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),response, Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.e("erro", error.toString());
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG ).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email", UserData.getInstance(getContext()).getUser().getEmail());
                params.put("mobile", mobile);
                params.put("otp", otp);
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

    private void getOtp(){
        SmsRetrieverClient client = SmsRetriever.getClient(getActivity());

// Starts SmsRetriever, waits for ONE matching SMS message until timeout
// (5 minutes).
        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task.
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                final SMSBroadcastReceiver receiver = new SMSBroadcastReceiver();
                getActivity().registerReceiver(receiver, new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION));
                receiver.setOnSmsReceivedListener(new OnSmsReceived() {
                    @Override
                    public void onOtpReceived(String otp) {
                        new_otp_number.setText(otp);
                        new_otp_button.performClick();
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Verify Mobile");
        ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
                .getSystemService(android.content.Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(
                getActivity().getCurrentFocus()
                        .getWindowToken(), 0);

    }

}
