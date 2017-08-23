package debugbridge.mybooks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import debugbridge.mybooks.Fragments.SignUp;
import debugbridge.mybooks.R;


public class Login_screen extends AppCompatActivity {
    EditText mobno, pass;
    TextView skip;
    AppCompatButton login, signup;
    String LOGIN_URL;
    String mob_no, password;
    public static final String KEY_MOBILE="usermobile";
    public static final String KEY_PASSWORD="password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setTitle("LOGIN");

            skip = (TextView) findViewById(R.id.skip);
            mobno = (EditText) findViewById(R.id.input_number);
            mob_no = mobno.getText().toString();


            pass = (EditText) findViewById(R.id.input_pass);
            password = pass.getText().toString();

            signup = (AppCompatButton) findViewById(R.id.sign_up);
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.content, new SignUp())
                            .addToBackStack(null)
                            .commit();
                }
            });
            login = (AppCompatButton) findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    userlogin();
                }
            });
            skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent login = new Intent(Login_screen.this, GetLocation.class);
                    startActivity(login);
                    Login_screen.this.finish();
                }
            });
        
    }

    private void userlogin() {StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.trim().equals("success")){
                        openProfile();
                    }else{
                        Toast.makeText(Login_screen.this,response,Toast.LENGTH_LONG).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Login_screen.this,error.toString(),Toast.LENGTH_LONG ).show();
                }
            }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> map = new HashMap<String,String>();
            map.put(KEY_MOBILE,mob_no);
            map.put(KEY_PASSWORD,password);
            return map;
        }
    };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openProfile(){
        Intent login = new Intent(Login_screen.this, GetLocation.class);
        startActivity(login);
    }
    }



