package com.example.meditation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final TextView textView1 = findViewById(R.id.textView1);
        final TextView textView2 = findViewById(R.id.textView2);
        final TextView textView3 = findViewById(R.id.textView3);

        textView1.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        textView3.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textView1.animate().alpha(1f).setDuration(2000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        textView1.animate().alpha(0f).setDuration(2000);
                    }
                });
            }
        }, 0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textView2.animate().alpha(1f).setDuration(2000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        textView2.animate().alpha(0f).setDuration(2000);
                    }
                });
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textView3.animate().alpha(1f).setDuration(2000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        textView3.animate().alpha(0f).setDuration(2000).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
            }
        }, 4000);
    }
}
