package com.example.servermonitor.logs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

        FloatingActionButton buttonScheduleAction = view.findViewById(R.id.buttonScheduleAction);
        buttonScheduleAction.setOnClickListener(v -> showScheduleActionDialog());

        displayLogs();

        return view;
    }

    private void displayLogs() {
        String userEmail = Objects.requireNonNull
                (FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        boolean isAdmin = false;
        for (String adminEmail : MainActivity.getAdmins()) {
            if (adminEmail.equals(userEmail)) {
                isAdmin = true;
                break;
            }
        }

        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance(MainActivity.getFirebaseRealtimeDatabaseUrl())
                .getReference("logs");

        Query query = databaseReference.orderByChild("timestamp");

        boolean finalIsAdmin = isAdmin;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<LogInfo> logs = new ArrayList<>();
                for (DataSnapshot logSnapshot : snapshot.getChildren()) {
                    LogInfo logInfo = logSnapshot.getValue(LogInfo.class);
                    if (logInfo != null) {
                        if (finalIsAdmin || logInfo.getLevel().equals("ERROR")) {
                            logs.add(logInfo);
                        }
                    }
                }
                logs.sort((logInfo1, logInfo2) -> logInfo2.getTimestamp()
                        .compareTo(logInfo1.getTimestamp()));
                adapter.setLogs(logs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to read value.", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private void showScheduleActionDialog() {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_schedule_action, null);

        Button buttonTimestamp = dialogView.findViewById(R.id.buttonTimestamp);
        EditText editTextTimestamp = dialogView.findViewById(R.id.editTextTimestamp);
        EditText finalEditTextTimestamp1 = editTextTimestamp;
        buttonTimestamp.setOnClickListener(v -> showTimestampPicker(buttonTimestamp,
                finalEditTextTimestamp1));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        builder.setTitle("Schedule Action");

        editTextTimestamp = dialogView.findViewById(R.id.editTextTimestamp);
        Spinner spinnerLevel = dialogView.findViewById(R.id.spinnerLevel);
        EditText editTextMessage = dialogView.findViewById(R.id.editTextMessage);
        EditText editTextServerId = dialogView.findViewById(R.id.editTextServerId);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireActivity(), R.array.log_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapter);

        EditText finalEditTextTimestamp = editTextTimestamp;
        builder.setPositiveButton("OK", (dialog, which) -> {
            String timestamp = finalEditTextTimestamp.getText().toString();
            String level = spinnerLevel.getSelectedItem().toString();
            String message = editTextMessage.getText().toString();
            String serverId = editTextServerId.getText().toString();

            if (timestamp.isEmpty() || level.isEmpty() || message.isEmpty() || serverId.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            timestamp += " SCHEDULED";

            LogInfo logInfo = new LogInfo(timestamp, level, message, serverId);

            DatabaseReference databaseReference = FirebaseDatabase
                    .getInstance(MainActivity.getFirebaseRealtimeDatabaseUrl())
                    .getReference("logs");

            databaseReference.push().setValue(logInfo).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Log entry scheduled successfully",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to schedule log entry: " + Objects
                                    .requireNonNull(task.getException()).getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });

            dialog.dismiss();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showTimestampPicker(Button buttonTimestamp, EditText editTextTimestamp) {
        Calendar timestamp = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    timestamp.set(Calendar.YEAR, year);
                    timestamp.set(Calendar.MONTH, monthOfYear);
                    timestamp.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            requireActivity(),
                            (view1, hourOfDay, minute) -> {
                                timestamp.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                timestamp.set(Calendar.MINUTE, minute);

                                SimpleDateFormat sdf = new SimpleDateFormat(
                                        "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String timestampString = sdf.format(timestamp.getTime());

                                buttonTimestamp.setText(timestampString);

                                editTextTimestamp.setText(timestampString);
                            },
                            timestamp.get(Calendar.HOUR_OF_DAY),
                            timestamp.get(Calendar.MINUTE),
                            true
                    );
                    timePickerDialog.show();
                },
                timestamp.get(Calendar.YEAR),
                timestamp.get(Calendar.MONTH),
                timestamp.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigationView(true);
    }
}