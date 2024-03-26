package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.androidapp.activity.GameActivity;
import com.example.androidapp.activity.LeaderboardActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_title_screen);
    }

    public void startGame(View view)  {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hawker Heroes");
        builder.setMessage("You are Mr Tan, a 40 year old hawker operating a carrot cake store in Singapore. " +
                "The year is 2100, robots are wandering amongst us. Surprisingly, their favourite food is carrot cake." +
                "Your mission is to serve them as fast as possible!");
        builder.setPositiveButton("Let's Start Serving!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the positive button click
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void viewLeaderboard(View view) {
        startActivity(new Intent(this, LeaderboardActivity.class));
    }

    public void showPrompt() {



    }
}