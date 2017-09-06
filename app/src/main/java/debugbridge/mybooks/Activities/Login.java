package debugbridge.mybooks.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.victor.loading.book.BookLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import debugbridge.mybooks.AppVolley.SingletonVolley;
import debugbridge.mybooks.Fragments.SignUp;
import debugbridge.mybooks.Fragments.VerifyUser;
import debugbridge.mybooks.Model.User;
import debugbridge.mybooks.R;
import debugbridge.mybooks.SharedPrefs.UserData;
import debugbridge.mybooks.Utility.UrlConstant;


public class Login extends AppCompatActivity {
    private EditText email, password;
    private TextView signup;
    private AppCompatButton login;

    @Override
    protected void onStart() {
        super.onStart();
        if (UserData.getInstance(this).checkLogin()){
            openProfile();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_password);
        signup = (TextView) findViewById(R.id.sign_up);
        login = (AppCompatButton) findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.right_enter, R.anim.slide_out);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                fragmentTransaction.replace(R.id.login_content, new SignUp());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(isValidEmail(email.getText()) && email.getText().toString().length() > 0)){
                        email.setError("Invalid Email Address");
                        return;
                    }

                    if (password.getText().length() < 1){
                        password.setError("Invalid Password");
                        return;
                    }

                    userLogin(email.getText().toString(), password.getText().toString());
                }
        });

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void userLogin(final String email, final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.USER_LOGIN,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("unsuccessful")){
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("user");
                            JSONObject object = jsonArray.getJSONObject(0);

                            if (object.getString("verified").trim().equals("0")){
                                verifyUser(object.getString("email"), object.getString("name"));
                                progressDialog.dismiss();
                                return;
                            }

                            User user = new User(object.getString("email"), object.getString("name"), object.getString("mobile"), object.getString("verified"));
                            UserData.getInstance(Login.this).setLogin(user);
                            openProfile();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG ).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> map = new HashMap<>();
                    map.put("email", email);
                    map.put("password", password);
                    return map;
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
        SingletonVolley.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void openProfile(){
        Intent login = new Intent(Login.this, GetLocation.class);
        startActivity(login);
        overridePendingTransition(R.anim.right_enter, R.anim.slide_out);
        finish();
    }

    private void verifyUser(String email, String  name){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        Fragment fragment = new VerifyUser();
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("name", name);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}



