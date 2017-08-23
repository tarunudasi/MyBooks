package debugbridge.mybooks.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import debugbridge.mybooks.R;

public class SignUp extends android.support.v4.app.Fragment {

    EditText inpu_name,input_ph,input_pass,input_email;
    String name,phone,pass,email;
    AppCompatButton save;
    private static final String REGISTER_URL=null;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE="phone";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up, container, false);
        getActivity().setTitle("Signup");

        inpu_name = (EditText) view.findViewById(R.id.input_name);
        name = inpu_name.getText().toString();

        input_ph = (EditText) view.findViewById(R.id.input_phone);
        phone = input_ph.getText().toString();

        input_email = (EditText) view.findViewById(R.id.input_email);
        email = input_email.getText().toString();

        input_pass = (EditText) view.findViewById(R.id.input_pass);
        pass = input_pass.getText().toString();

        save = (AppCompatButton) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((name.equals("") || name.equals(null)) || (pass.equals("") || pass.equals(null)) || (email.equals("") || email.equals(null)) || (phone.equals("") || phone.equals(null))) {
                    Toast.makeText(getActivity(), "Please Fill All Fields..", Toast.LENGTH_SHORT).show();
                } else {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        input_email.setError("Invalid Email Address");
                    } else {
                        if (pass.trim().length() < 6) {
                            input_pass.setError("Minimum length of Password should be 6");
                        } else {
                            if (phone.trim().length() != 10) {
                                input_ph.setError("Invalid Mobile No.");
                            }


                        }
                    }
                }


                registeruser();
            }
        });

        return view;
    }

    private void registeruser() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity().getApplicationContext(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,name);
                params.put(KEY_PASSWORD,pass);
                params.put(KEY_EMAIL, email);
                params.put(KEY_PHONE,phone);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

}

