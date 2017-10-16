package com.rbooks.Activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.rbooks.MyReceiver.ConnectivityReceiver;
import com.rbooks.R;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!ConnectivityReceiver.isConnected()){
            final Dialog alert = new Dialog(this, R.style.full_screen_dialog);
            alert.setContentView(R.layout.internet_customize_alert);
            alert.setCancelable(true);
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Splash.this.finish();
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
                        splashScreen();
                        alert.dismiss();
                    }
                }
            });

            alert.show();

        } else {
            splashScreen();
        }
    }

    private void splashScreen(){
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Splash.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.right_enter, R.anim.slide_out);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

}
