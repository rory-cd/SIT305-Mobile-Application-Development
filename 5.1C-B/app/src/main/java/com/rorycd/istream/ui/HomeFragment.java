package com.rorycd.istream.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.rorycd.istream.Navigator;
import com.rorycd.istream.R;
import com.rorycd.istream.data.UserRepository;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnSignOut = view.findViewById(R.id.btnSignOut);
        Navigator nav = (Navigator) requireActivity();

        btnSignOut.setOnClickListener((v) -> {
            UserRepository repo = UserRepository.getInstance(requireContext());
            repo.logout();
            nav.navigateTo(new LoginFragment(), false);
        });
    }
}