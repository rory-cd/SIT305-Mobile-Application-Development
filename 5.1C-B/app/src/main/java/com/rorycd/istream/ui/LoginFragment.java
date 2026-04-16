package com.rorycd.istream.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rorycd.istream.MainActivity;
import com.rorycd.istream.Navigator;
import com.rorycd.istream.R;
import com.rorycd.istream.data.User;
import com.rorycd.istream.data.UserRepository;

public class LoginFragment extends Fragment {

    private TextInputLayout tilUsername, tilPassword;
    private TextInputEditText etUsername, etPassword;
    private Navigator nav;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Text fields
        tilUsername = view.findViewById(R.id.tilUsername);
        tilPassword = view.findViewById(R.id.tilPassword);

        etUsername = view.findViewById(R.id.etUsername);
        etUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilUsername.setError(null);
                tilUsername.setErrorEnabled(false);
            }
        });

        etPassword = view.findViewById(R.id.etPassword);
        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilPassword.setError(null);
                tilPassword.setErrorEnabled(false);
            }
        });

        // Buttons
        MaterialButton btnLogin = view.findViewById(R.id.btnLogin);
        MaterialButton btnSignup = view.findViewById(R.id.btnSignup);
        nav = (Navigator) requireActivity();

        // Log in functionality
        btnLogin.setOnClickListener((v) -> {
            String usernameInput = String.valueOf(etUsername.getText());
            String passwordInput = String.valueOf(etPassword.getText());

            // Validate
            if (usernameInput.isEmpty()) {
                tilUsername.setError("Username must be provided.");
                return;
            }
            if (passwordInput.isEmpty()) {
                tilPassword.setError("Password must be provided.");
                return;
            }

            // Attempt login
            UserRepository repo = UserRepository.getInstance(requireContext());
            repo.login(usernameInput, passwordInput, (user) -> {
                if (user == null) {
                    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
                } else {
                    nav.navigateTo(new HomeFragment(), false);
                }
            });
        });

        // Sign up functionality
        btnSignup.setOnClickListener((v) -> {
            nav.navigateTo(new SignupFragment(), true);
        });
    }
}
