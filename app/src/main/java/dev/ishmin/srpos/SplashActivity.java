package dev.ishmin.srpos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // set layout

        int splash_time_out = 2000; // splash screen time counter (2 sec)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() { // handler method to time out splash screen
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class); // opens up next activity
                startActivity(intent);
                finish();
            }
        }, splash_time_out);
    }
}
