package com.example.servermonitor;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "RRR";

    private static final String FIREBASE_DATABASE_URL =
            "https://server-monitor-e6bb3-default-rtdb.europe-west1.firebasedatabase.app/";

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                Navigation.findNavController(this, R.id.fragment_container)
                        .navigate(R.id.homeFragment);
                return true;
            } else if (item.getItemId() == R.id.navigation_monitor) {
                Navigation.findNavController(this, R.id.fragment_container)
                        .navigate(R.id.monitorFragment);
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                Navigation.findNavController(this, R.id.fragment_container)
                        .navigate(R.id.profileFragment);
                return true;
            } else {
                return false;
            }
        });
    }

    public void showBottomNavigationView(boolean show) {
        if (show) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    public static String getFirebaseDatabaseUrl() {
        return FIREBASE_DATABASE_URL;
    }

    public static String getTag() {
        return TAG;
    }
}
