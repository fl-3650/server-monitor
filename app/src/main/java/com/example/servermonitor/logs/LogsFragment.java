package com.example.servermonitor.logs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servermonitor.MainActivity;
import com.example.servermonitor.R;
import com.example.servermonitor.logs.LogInfo;
import com.example.servermonitor.logs.LogsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        Button buttonScheduleAction = view.findViewById(R.id.buttonScheduleAction);
        buttonScheduleAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScheduleActionDialog();
            }
        });


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

    private void showScheduleActionDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_schedule_action, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("Schedule Action");

        // Initialize the input fields in the dialog
        EditText editTextTimestamp = dialogView.findViewById(R.id.editTextTimestamp);
        Spinner spinnerLevel = dialogView.findViewById(R.id.spinnerLevel);
        EditText editTextMessage = dialogView.findViewById(R.id.editTextMessage);
        EditText editTextServerId = dialogView.findViewById(R.id.editTextServerId);

        // Set up the level spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.log_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapter);

        // Set up the positive and negative buttons for the dialog
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Extract the user input from the dialog
            String timestamp = editTextTimestamp.getText().toString();
            String level = spinnerLevel.getSelectedItem().toString();
            String message = editTextMessage.getText().toString();
            String serverId = editTextServerId.getText().toString();

            // Create a LogInfo object
            LogInfo logInfo = new LogInfo(timestamp, level, message, serverId);

            // Get a reference to the RTDB
            DatabaseReference databaseReference = FirebaseDatabase
                    .getInstance(MainActivity.getFirebaseDatabaseUrl())
                    .getReference("logs");

            // Push the LogInfo object to the RTDB
            databaseReference.push().setValue(logInfo).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Log entry scheduled successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to schedule log entry: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Display the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigationView(true);
    }
}