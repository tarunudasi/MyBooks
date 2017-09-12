package debugbridge.mybooks.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
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
import debugbridge.mybooks.MyReceiver.ConnectivityReceiver;
import debugbridge.mybooks.R;
import debugbridge.mybooks.Utility.UrlConstant;

public class SignUp extends android.support.v4.app.Fragment {

    private EditText register_name, register_email, register_password;
    private AppCompatButton register_button;
    private TextView login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        register_name = (EditText) view.findViewById(R.id.register_name);
        register_email = (EditText) view.findViewById(R.id.register_email);
        register_password = (EditText) view.findViewById(R.id.register_password);
        register_button = (AppCompatButton) view.findViewById(R.id.register_button);
        login = (TextView) view.findViewById(R.id.login_register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (register_name.getText().length() < 1){
                    register_name.setError("Invalid Name");
                    return;
                }

                if (!(isValidEmail(register_email.getText()) && register_email.getText().toString().length() > 0)){
                    register_email.setError("Invalid Email Address");
                    return;
                }

                if (register_password.getText().length() < 1){
                    register_password.setError("Invalid Password");
                    return;
                }

                if (ConnectivityReceiver.isConnected()) {
                    registerUser(register_name.getText().toString(), register_email.getText().toString(), register_password.getText().toString());
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

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void registerUser(final String name, final String email, final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.USER_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("success")){
                            verifyUser(email, name);
                            progressDialog.dismiss();
                            return;
                        }else if (response.trim().equals("unsuccessful")){
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),"Email Already Exist",Toast.LENGTH_LONG).show();
                            return;
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_LONG).show();
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
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
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

    private void verifyUser(String email, String  name){
        getActivity().getSupportFragmentManager().popBackStack();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment fragment = new VerifyUser();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.login_content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

