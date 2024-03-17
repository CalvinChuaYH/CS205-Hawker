package com.example.androidapp.firebase;

import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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
                    StringBuilder scoresBuilder = new StringBuilder();
                    for (DataSnapshot curSnapshot : snapshot.getChildren()) {
                        String username = curSnapshot.getKey(); // Get the username
                        Integer score = curSnapshot.getValue(Integer.class);
                        if (username != null && score != null) {
                            scoresBuilder.append(username).append(": ").append(score).append("\n");
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