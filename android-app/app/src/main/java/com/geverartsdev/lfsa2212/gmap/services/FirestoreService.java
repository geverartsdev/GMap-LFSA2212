package com.geverartsdev.lfsa2212.gmap.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.geverartsdev.lfsa2212.gmap.objects.Pothole;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreService {

    private static final String TAG = "FirestoreService";

    private static final String COLLECTION_POTHOLES = "potholes";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirestoreService() {

    }

    public void addPothole(Pothole pothole) {
        db.collection(COLLECTION_POTHOLES)
                .document()
                .set(pothole.toHashMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "addPothole : Successfully added document");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "addPothole : Error writing document : " + e);
                    }
                });
    }
}
