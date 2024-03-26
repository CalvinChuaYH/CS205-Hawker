package com.example.androidapp.firebase;

import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.example.androidapp.gamelogic.Score;
import com.google.firebase.FirebaseApp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.*;

public class Firebase {

    DatabaseReference myRef;

    public Firebase () {
        initialiseFirebase();
    }

    private void initialiseFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://cs205project-fd4bf-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("scores");
    }
    public void setScore(String username, int score){
        // Write a message to the database
        myRef.child(username).setValue(score); // Set the score under the username node
    }

    public void getScores(final TextView textView){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    List<Score> allScores = new ArrayList<>(); // Use a List to store scores

                    for (DataSnapshot curSnapshot : snapshot.getChildren()) {
                        String username = curSnapshot.getKey(); // Get the username
                        Integer score = curSnapshot.getValue(Integer.class);

                        if (username != null && score != null) {
                            allScores.add(new Score(username, score)); // Create Score objects
                        }

                        // Sort the scores in ascending order
                        allScores.sort(Comparator.comparingInt(Score::getValue));

                        // Get the top 3 (or less if fewer than 3 exist)
                        int topCount = Math.min(allScores.size(), 3);
                        StringBuilder scoresBuilder = new StringBuilder();
                        for (int i = 0; i < topCount; i++) {
                            Score currentScore = allScores.get(i);
                            scoresBuilder.append(currentScore.getUsername())
                                    .append(": ")
                                    .append(currentScore.getValue())
                                    .append("\n");
                        }

                        textView.setText(scoresBuilder.toString());

                        Log.d("MainActivity", "Username: " + username + ", Score: " + score);

                        // Do something with the username and score, such as displaying them
                        Log.d("MainActivity", "Username: " + username + ", Score: " + score);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MainActivity", "Error fetching scores: " + error.getMessage());
            }
        });
    }
}