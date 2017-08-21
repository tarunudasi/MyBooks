package debugbridge.mybooks.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import debugbridge.mybooks.R;

/**
 * Created by ram on 17/8/17.
 */

    public class SignUp extends android.support.v4.app.Fragment {
    EditText inpu_name,input_ph,input_pass,input_email;
    String name,phone,pass,email;
    AppCompatButton save;
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
            }
        });

        return view;
    }
}
