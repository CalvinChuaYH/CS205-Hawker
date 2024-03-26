package com.example.androidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.MainActivity;
import com.example.androidapp.firebase.Firebase;
import com.example.androidapp.R;
import com.example.androidapp.firebase.FirebaseManager;

public class LeaderboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Firebase firebase = FirebaseManager.getInstance();

        TextView score = (TextView) findViewById(R.id.score);
        firebase.getScores(score);
    }

    public void viewHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        getOnBackPressedDispatcher().onBackPressed();
    }
}
