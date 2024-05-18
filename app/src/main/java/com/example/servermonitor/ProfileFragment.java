package com.example.servermonitor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Button button = view.findViewById(R.id.logout);
        TextView textView = view.findViewById(R.id.user_details);
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Navigation.findNavController(view)
                    .navigate(R.id.action_profileFragment_to_loginFragment);
        } else {
            String emailLabel = getString(R.string.email_label, user.getEmail());

            textView.setText(emailLabel);
        }

        button.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(view)
                    .navigate(R.id.action_profileFragment_to_loginFragment);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).showBottomNavigationView(true);
    }
}