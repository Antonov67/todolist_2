package com.arhiser.todolist.screens.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.window.SplashScreen;

import com.arhiser.todolist.R;
import com.arhiser.todolist.screens.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    Thread splashTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 2500) {
                        sleep(100);
                        waited += 100;
                    }
                    goToMainMenu();
                } catch (InterruptedException ignored) {}
            }
        };
        splashTread.start();
    }

    private void goToMainMenu() {
        Intent infoDialog = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(infoDialog);
        SplashActivity.this.finish();
    }
}