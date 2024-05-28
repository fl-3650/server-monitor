package com.example.servermonitor.monitoring;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.servermonitor.MainActivity;
import com.example.servermonitor.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MonitorFragment extends Fragment {

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
        //textView = view.findViewById(R.id.textViewMonitor);

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
                .getInstance(MainActivity.getFirebaseRealtimeDatabaseUrl()).getReference("stats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ServerInfo serverInfo = snapshot.getValue(ServerInfo.class);
                if (serverInfo != null) {
                    TextView textViewTotalDiskSpace = requireView()
                            .findViewById(R.id.textViewTotalDiskSpace);
                    TextView textViewOccupiedDiskSpace = requireView()
                            .findViewById(R.id.textViewOccupiedDiskSpace);
                    TextView textViewFreeDiskSpacePercentage = requireView()
                            .findViewById(R.id.textViewFreeDiskSpacePercentage);
                    TextView textViewCpuLoad = requireView()
                            .findViewById(R.id.textViewCpuLoad);
                    TextView textViewRamUsage = requireView()
                            .findViewById(R.id.textViewRamUsage);
                    TextView textViewUptime = requireView()
                            .findViewById(R.id.textViewUptime);
                    TextView textViewLoggedInUsers = requireView()
                            .findViewById(R.id.textViewLoggedInUsers);

                    textViewTotalDiskSpace.setText(String.valueOf(serverInfo
                            .getTotal_disk_space()));
                    textViewOccupiedDiskSpace.setText(String.valueOf(serverInfo
                            .getOccupied_disk_space()));
                    textViewFreeDiskSpacePercentage.setText(String.valueOf(serverInfo
                            .getFree_disk_space_percentage()));
                    textViewCpuLoad.setText(String.valueOf(serverInfo.getCpu_load()));
                    textViewRamUsage.setText(String.valueOf(serverInfo.getRam_usage()));
                    textViewUptime.setText(String.valueOf(serverInfo.getUptime()));
                    textViewLoggedInUsers.setText(String.valueOf(serverInfo.getLogged_in_users()));

                } else {
                    Toast.makeText(requireContext(), "Something went wrong",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Failed to read value.", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}