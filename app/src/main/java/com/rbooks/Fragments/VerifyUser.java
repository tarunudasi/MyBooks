package com.rbooks.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rbooks.Adapter.SpinnerCountryArrayAdapter;
import com.rbooks.AppVolley.SingletonVolley;
import com.rbooks.Model.Country;
import com.rbooks.MyReceiver.ConnectivityReceiver;
import com.rbooks.R;
import com.rbooks.Utility.UrlConstant;
import com.victor.loading.book.BookLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyUser extends Fragment{

    private static final int RESOLVE_HINT = 5034;
    private TextView verify_login;
    private EditText verify_number;
    private Button verify_button;
    private String email, name;
    private GoogleApiClient apiClient;
    private Spinner countrySpinner;
    private SpinnerCountryArrayAdapter adapter;
    private List<Country> countryList;
    private TextView countryCodeTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_user, container, false);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        LinearLayout login_layout = (LinearLayout) view.findViewById(R.id.mobile_verify_layout);
        ViewGroup.LayoutParams params = login_layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = 0;
        params.width = (int)((float)displayMetrics.widthPixels * 0.75);
        login_layout.setLayoutParams(params);

        countryList = new ArrayList<>();

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.country_codes);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            JSONArray jsonArray = new JSONArray(new String(b));

            for (int i = 0 ; i< jsonArray.length() ; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.e("country", jsonObject.getString("dial_code"));
                countryList.add(new Country(jsonObject.getString("name"), jsonObject.getString("dial_code")));
            }

        } catch (JSONException e) {
            Log.e(this.getClass().getName(), e.toString());
        } catch (IOException e){
            Log.e(this.getClass().getName(), e.toString());
        }

        countryCodeTextView = (TextView) view.findViewById(R.id.countryCodeTextView);
        verify_button = (Button) view.findViewById(R.id.verify_button);
        verify_login = (TextView) view.findViewById(R.id.verify_login);
        verify_number = (EditText) view.findViewById(R.id.verify_number);
        countrySpinner = (Spinner) view.findViewById(R.id.countrySpinner);
        adapter = new SpinnerCountryArrayAdapter(getContext(), android.R.layout.simple_spinner_item, countryList);
        countrySpinner.setAdapter(adapter);

        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryCodeTextView.setText(countryList.get(position).getCountryCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            email = bundle.getString("email");
            name = bundle.getString("name");
        }


        /*

        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();

        apiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.CREDENTIALS_API)
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, "Client connection failed: " + connectionResult.getErrorMessage());
                    }
                }).build();


        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
                apiClient, hintRequest);

        try {
            startIntentSenderForResult(intent.getIntentSender(),
                    RESOLVE_HINT, null, 0, 0, 0, null);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

        */

        verify_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify_number.getText().length() < 3){
                    verify_number.setError("Enter valid Number");
                    return;
                }

                if (ConnectivityReceiver.isConnected()) {
                    verifyUser(countryCodeTextView.getText() + verify_number.getText().toString(), email);
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

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (apiClient != null){
            apiClient.stopAutoManage(getActivity());
            apiClient.disconnect();
        }
    }

    private void verifyUser(final String mobile, final String email){

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.SEND_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            getOtp(mobile);
                            progressDialog.dismiss();
                        }else if (response.trim().equals("unsuccessful")){
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),"Wrong Email",Toast.LENGTH_LONG).show();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),response, Toast.LENGTH_LONG).show();
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
        SingletonVolley.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void getOtp(String mobile){
        getActivity().getSupportFragmentManager().popBackStack();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment fragment = new OtpVerify();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("name", name);
        bundle.putString("mobile", mobile);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.login_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
