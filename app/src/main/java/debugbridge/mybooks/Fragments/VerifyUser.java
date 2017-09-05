package debugbridge.mybooks.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.victor.loading.book.BookLoading;

import java.util.HashMap;
import java.util.Map;

import debugbridge.mybooks.AppVolley.SingletonVolley;
import debugbridge.mybooks.R;
import debugbridge.mybooks.Utility.UrlConstant;

public class VerifyUser extends Fragment{

    private TextView verify_login;
    private EditText verify_number;
    private Button verify_button;
    private String email, name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_user, container, false);
        verify_button = (Button) view.findViewById(R.id.verify_button);
        verify_login = (TextView) view.findViewById(R.id.verify_login);
        verify_number = (EditText) view.findViewById(R.id.verify_number);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            email = bundle.getString("email");
            name = bundle.getString("name");
        }

        verify_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify_number.getText().length() != 10){
                    verify_number.setError("Enter valid Number");
                    return;
                }
                verifyUser(verify_number.getText().toString(), email);
            }
        });

        return view;
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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.SEND_OTP,
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
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
