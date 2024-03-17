package com.example.androidapp.firebase;

public class FirebaseManager {

    private static Firebase firebase;

    public static Firebase getInstance() {
        if (firebase == null) {
            firebase = new Firebase();
        }
        return firebase;
    }
}
