package com.example.meditation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {
    EditText duration, loginid, track;
    Button Insert, ViewAll, btnBackToMain;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        duration = findViewById(R.id.etDuration);
        loginid = findViewById(R.id.etId);
        track = findViewById(R.id.etTrack);
        Insert = findViewById(R.id.btnEnter);
        ViewAll = findViewById(R.id.btnView);
        btnBackToMain = findViewById(R.id.btnBackToMain);

        db = openOrCreateDatabase("MeditationDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS session(id VARCHAR,duration VARCHAR,track VARCHAR);");

        Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("INSERT INTO session VALUES('" + duration.getText() + "','" + loginid.getText() +
                        "','" + track.getText() + "');");
                showMessage("", "Record Added Successfully.");
            }
        });

        ViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = db.rawQuery("SELECT * FROM session", null);
                if (c.getCount() == 0) {
                    showMessage("Error", "No records found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (c.moveToNext()) {
                    buffer.append("ID: " + c.getString(1) + "\n");
                    buffer.append("Duration: " + c.getString(0) + "\n");
                    buffer.append("Track: " + c.getString(2) + "\n\n");
                }
                showMessage("Session Details:", buffer.toString());
            }
        });

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        duration.setText("");
        loginid.setText("");
        track.setText("");
        duration.requestFocus();
    }
}
