package com.example.meditation;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView timerTextView;
    private Button startButton;
    private EditText durationEditText;
    private ImageView backgroundImageView;
    private Spinner musicSpinner;
    private MediaPlayer mediaPlayer;
    private int[] musicResIds = {R.raw.music1, R.raw.music2, R.raw.music3};
    private int[] imageResIds = {R.drawable.one, R.drawable.two, R.drawable.three};

    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private long timeLeftInMillis = 600000;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        durationEditText = findViewById(R.id.durationEditText);
        backgroundImageView = findViewById(R.id.backgroundImageView);
        musicSpinner = findViewById(R.id.musicSpinner);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.music_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        musicSpinner.setAdapter(adapter);

        musicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                mediaPlayer = MediaPlayer.create(MainActivity.this, musicResIds[position]);
                backgroundImageView.setImageResource(imageResIds[position]);
                mediaPlayer.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        Button buttonExit = findViewById(R.id.buttonExit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void startTimer() {
        int durationInMinutes = Integer.parseInt(durationEditText.getText().toString());
        timeLeftInMillis = durationInMinutes * 60000;

        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                startButton.setText("Start Meditation");

                // Stop the music
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                // Transition to CooldownActivity
                Intent intent = new Intent(MainActivity.this, CooldownActivity.class);
                startActivity(intent);
            }
        }.start();

        isTimerRunning = true;
        startButton.setText("Pause Meditation");
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        startButton.setText("Resume Meditation");
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        int hours = minutes / 60;
        minutes = minutes % 60;

        String timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle home action
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
