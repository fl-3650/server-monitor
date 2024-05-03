package com.example.servermonitor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationFragment extends Fragment {

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private FirebaseAuth mAuth;

    public RegistrationFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_loginFragment_to_homeFragment);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);


        Button buttonLoginNow = view.findViewById(R.id.btn_login_now);
        buttonLoginNow.setOnClickListener(navigation -> {
            // Navigate to the next destination
            Navigation.findNavController(view)
                    .navigate(R.id.action_registrationFragment_to_loginFragment);
            onDestroy();
        });


        Button buttonRegister = view.findViewById(R.id.btn_register);
        buttonRegister.setOnClickListener(v -> {
            String email = String.valueOf(editTextEmail.getText()) ;
            String password = String.valueOf(editTextPassword.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Email is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Password is empty", Toast.LENGTH_SHORT).show();
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    "Authentication succeeded. You can login now",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
        });


        return view;
    }

}