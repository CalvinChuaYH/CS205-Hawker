package com.example.androidapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.Game;
import com.example.androidapp.GameLoop;

public class GameActivity extends AppCompatActivity {

    private GameLoop gameLoop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Game game = new Game(this, this);
        setContentView(game);
    }
}
