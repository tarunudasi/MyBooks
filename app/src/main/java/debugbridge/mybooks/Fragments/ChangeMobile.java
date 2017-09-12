package debugbridge.mybooks.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import debugbridge.mybooks.Adapter.SpinnerCountryArrayAdapter;
import debugbridge.mybooks.AppVolley.SingletonVolley;
import debugbridge.mybooks.MainActivity;
import debugbridge.mybooks.Model.Country;
import debugbridge.mybooks.MyReceiver.ConnectivityReceiver;
import debugbridge.mybooks.R;
import debugbridge.mybooks.SharedPrefs.UserData;
import debugbridge.mybooks.Utility.UrlConstant;

public class ChangeMobile extends Fragment {

    private EditText change_number;
    private Button change_number_button;
    private Spinner countrySpinner;
    private SpinnerCountryArrayAdapter adapter;
    private List<Country> countryList;
    private TextView countryCodeTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_mobile, container, false);

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

        countryCodeTextView = (TextView) view.findViewById(R.id.country_code_text_view);
        countrySpinner = (Spinner) view.findViewById(R.id.country_spinner);
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


        change_number = (EditText) view.findViewById(R.id.change_number);
        change_number_button = (Button) view.findViewById(R.id.change_number_button);

        change_number_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change_number.getText().length() != 10){
                    change_number.setError("Enter valid Number");
                    return;
                }

                if (ConnectivityReceiver.isConnected()){
                    changeMobile(countryCodeTextView.getText() + change_number.getText().toString());
                }else{

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

        return view;
    }

    private void changeMobile(final String mobile){
        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.full_screen_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.getWindow().setContentView(R.layout.progress_dialog_sell_books);
        progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        BookLoading bookLoading = (BookLoading) progressDialog.getWindow().findViewById(R.id.book_loading_progress);
        bookLoading.start();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlConstant.CHANGE_OTP,
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
                params.put("email", UserData.getInstance(getContext()).getUser().getEmail());
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
        getActivity().onBackPressed();
        Fragment fragment = new NewOtpVerify();
        Bundle bundle = new Bundle();
        bundle.putString("mobile", mobile);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.add(R.id.profile_content, fragment);
        fragmentTransaction.addToBackStack(Profile.class.getName());
        fragmentTransaction.commit();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Change Mobile");
        ((MainActivity)getActivity()).getSupportActionBar().setSubtitle(null);

        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_back_arrow);
        upArrow.setColorFilter(getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }


}
