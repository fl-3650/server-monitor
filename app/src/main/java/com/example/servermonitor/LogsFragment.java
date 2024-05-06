package com.example.servermonitor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogsFragment extends Fragment {
    private static final String TAG = "RRR";
    private static final String URL =
            "https://server-monitor-e6bb3-default-rtdb.europe-west1.firebasedatabase.app/";
    private TextView textView;

    public LogsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        textView = view.findViewById(R.id.textViewLogs);

        displayLogs();

        return view;
    }

    private void displayLogs() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(URL)
                .getReference("logs");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder data = new StringBuilder();
                for (DataSnapshot logSnapshot : snapshot.getChildren()) {
                    LogInfo logInfo = logSnapshot.getValue(LogInfo.class);
                    if (logInfo != null) {
                        data.append("Timestamp: ").append(logInfo.getTimestamp()).append('\n');
                        data.append("Level: ").append(logInfo.getLevel()).append('\n');
                        data.append("Message: ").append(logInfo.getMessage()).append('\n');
                        data.append("Server ID: ").append(logInfo.getServer_id()).append('\n');
                        data.append("\n");
                    }
                }
                textView.setText(data.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
                textView.setText(error.getMessage());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigationView(true);
    }
}