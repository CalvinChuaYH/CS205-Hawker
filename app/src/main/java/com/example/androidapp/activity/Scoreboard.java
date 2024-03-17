package com.example.androidapp.activity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scoreboard {
    private ArrayList<ScoreEntry> scores;

    public Scoreboard() {
        this.scores = new ArrayList<>();
    }

    public void addScore(String playerName, int score) {
        scores.add(new ScoreEntry(playerName, score));
        scores.sort(Comparator.comparing(ScoreEntry::getScore).reversed());
    }

    public List<ScoreEntry> getHighScores() {
        return scores;
    }

    public static class ScoreEntry {
        private String playerName;
        private int score;

        public ScoreEntry(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }
    }
}
