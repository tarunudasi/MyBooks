package debugbridge.mybooks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import debugbridge.mybooks.R;

public class Splash_screen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash_screen.this, Login_screen.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_enter, R.anim.slide_out);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
