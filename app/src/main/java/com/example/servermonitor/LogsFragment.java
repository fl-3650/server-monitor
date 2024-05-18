package com.example.servermonitor;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LogsFragment extends Fragment {
    private LogsAdapter adapter;

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
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLogs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LogsAdapter();
        recyclerView.setAdapter(adapter);
        displayLogs();
        return view;
    }

    private void displayLogs() {
        // Get the current user's email address
        String userEmail = Objects.requireNonNull
                (FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        // Determine whether the current user is an admin
        boolean isAdmin = false;
        for (String adminEmail : MainActivity.getAdmins()) {
            if (adminEmail.equals(userEmail)) {
                isAdmin = true;
                break;
            }
        }

        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance(MainActivity.getFirebaseDatabaseUrl()).getReference("logs");

        boolean finalIsAdmin = isAdmin;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LogInfo> logs = new ArrayList<>();
                for (DataSnapshot logSnapshot : snapshot.getChildren()) {
                    LogInfo logInfo = logSnapshot.getValue(LogInfo.class);
                    if (logInfo != null) {
                        // Only add the log to the list if it meets the criteria
                        if (finalIsAdmin || logInfo.getLevel().equals("ERROR")) {
                            logs.add(logInfo);
                        }
                    }
                }
                adapter.setLogs(logs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(MainActivity.getTag(), "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigationView(true);
    }
}