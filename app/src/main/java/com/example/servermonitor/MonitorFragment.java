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

public class MonitorFragment extends Fragment {
    private TextView textView;

    public MonitorFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        textView = view.findViewById(R.id.textViewMonitor);

        displayStatus();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigationView(true);
    }

    private void displayStatus() {
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance(MainActivity.getFirebaseDatabaseUrl()).getReference("stats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServerInfo serverInfo = snapshot.getValue(ServerInfo.class);
                if (serverInfo != null) {
                    String data = "Total Disk Space: " + serverInfo.getTotal_disk_space() + "\n\n" +
                            "Occupied Disk Space: " + serverInfo.getOccupied_disk_space() + "\n\n" +
                            "Free Disk Space Percentage: " + serverInfo
                            .getFree_disk_space_percentage() + "\n\n" +
                            "CPU Load: " + serverInfo.getCpu_load() + "\n\n" +
                            "RAM Usage: " + serverInfo.getRam_usage() + "\n\n" +
                            "Uptime: " + serverInfo.getUptime() + "\n\n" +
                            "Logged In Users: " + serverInfo.getLogged_in_users().toString();
                    textView.setText(data);

                } else {
                    textView.setText(R.string.no_data_available);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(MainActivity.getTag(), "Failed to read value.", error.toException());
                textView.setText(error.getMessage());
            }
        });
    }
}