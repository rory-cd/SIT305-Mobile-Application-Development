package com.rorycd.istream.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rorycd.istream.Navigator;
import com.rorycd.istream.R;
import com.rorycd.istream.data.UserRepository;

public class SignupFragment extends Fragment {

    private TextInputLayout tilUsername, tilFullName, tilPassword, tilConfirmPassword;
    private TextInputEditText etUsername, etFullName, etPassword, etConfirmPassword;
    private Navigator nav;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get UI references
        tilUsername = view.findViewById(R.id.tilUsername);
        tilFullName = view.findViewById(R.id.tilFullName);
        tilPassword = view.findViewById(R.id.tilPassword);
        tilConfirmPassword = view.findViewById(R.id.tilConfirmPassword);

        etUsername = view.findViewById(R.id.etUsername);
        etUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilUsername.setError(null);
                tilUsername.setErrorEnabled(false);
            }
        });

        etFullName = view.findViewById(R.id.etFullName);
        etFullName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilFullName.setError(null);
                tilFullName.setErrorEnabled(false);
            }
        });

        etPassword = view.findViewById(R.id.etPassword);
        etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilPassword.setError(null);
                tilPassword.setErrorEnabled(false);
            }
        });

        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        etConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilConfirmPassword.setError(null);
                tilConfirmPassword.setErrorEnabled(false);
            }
        });

        MaterialButton btnCreateAccount = view.findViewById(R.id.btnCreateAccount);
        nav = (Navigator) requireActivity();

        // Log in functionality
        btnCreateAccount.setOnClickListener((v) -> {
            String username = String.valueOf(etUsername.getText());
            String fullName = String.valueOf(etFullName.getText());
            String password = String.valueOf(etPassword.getText());
            String confirmPassword = String.valueOf(etConfirmPassword.getText());

            // Validate
            if (username.isEmpty()) {
                tilUsername.setError("Username must be provided.");
                return;
            }
            if (fullName.isEmpty()) {
                tilFullName.setError("Name must be provided.");
                return;
            }
            if (password.isEmpty()) {
                tilPassword.setError("Password must be provided.");
                return;
            }
            if (confirmPassword.isEmpty()) {
                tilConfirmPassword.setError("Password confirmation must be provided.");
                return;
            }
            if (!password.equals(confirmPassword)) {
                tilConfirmPassword.setError("Password confirmation must match provided password.");
                return;
            }

            // Attempt signup
            UserRepository repo = UserRepository.getInstance(requireContext());
            repo.registerUser(username, fullName, password, (userId) -> {
                if (userId < 1) {
                    Toast.makeText(requireContext(), "Signup failed", Toast.LENGTH_SHORT).show();
                } else {
                    nav.navigateTo(new HomeFragment(), false);
                }
            });
        });
    }
}
