package debugbridge.mybooks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import debugbridge.mybooks.Fragments.Login;
import debugbridge.mybooks.R;
import debugbridge.mybooks.SharedPrefs.UserData;


public class LoginActivity extends AppCompatActivity {

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

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.login_content, new Login());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void openProfile(){
        Intent login = new Intent(LoginActivity.this, GetLocation.class);
        startActivity(login);
        overridePendingTransition(R.anim.right_enter, R.anim.slide_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }else{
            super.onBackPressed();
        }
    }
}



