package com.example.androidapp.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.firebase.Firebase;
import com.example.androidapp.R;
import com.example.androidapp.firebase.FirebaseManager;

public class LeaderboardActivity extends AppCompatActivity {

    private Firebase firebase;
    TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        firebase = FirebaseManager.getInstance();

        score = (TextView) findViewById(R.id.score);
        firebase.getScores(score);
    }
}
