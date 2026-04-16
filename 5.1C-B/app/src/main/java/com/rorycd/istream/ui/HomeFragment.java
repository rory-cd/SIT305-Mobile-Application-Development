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

        TextInputLayout tilUrl = view.findViewById(R.id.tilUrl);
        TextInputEditText etUrl = view.findViewById(R.id.etUrl);

        etUrl.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilUrl.setError(null);
                tilUrl.setErrorEnabled(false);
            }
        });

        MaterialButton btnPlay = view.findViewById(R.id.btnPlay);
        MaterialButton btnAdd = view.findViewById(R.id.btnAdd);
        MaterialButton btnPlaylist = view.findViewById(R.id.btnPlaylist);
        MaterialButton btnSignOut = view.findViewById(R.id.btnSignOut);

        Navigator nav = (Navigator) requireActivity();
        UserRepository repo = UserRepository.getInstance(requireContext());

        // Play
        btnPlay.setOnClickListener((v) -> {
            String url = String.valueOf(etUrl.getText());
            if (isValidUrl(url)) {
                String videoId = getVideoId(url);
                nav.navigateTo(PlayFragment.newInstance(videoId), true);
            } else {
                tilUrl.setError("Invalid URL format.");
            }
        });

        // Add to playlist
        btnAdd.setOnClickListener((v) -> {
            String url = String.valueOf(etUrl.getText());
            if (isValidUrl(url)) {
                repo.addUrlToPlaylist(url);
                Toast.makeText(requireContext(), "Added to playlist", Toast.LENGTH_SHORT).show();
            } else {
                tilUrl.setError("Invalid URL format.");
            }
        });

        // My playlist
        btnPlaylist.setOnClickListener((v) -> {
            nav.navigateTo(new PlaylistFragment(), true);
        });

        // Sign out
        btnSignOut.setOnClickListener((v) -> {
            repo.logout();
            nav.navigateTo(new LoginFragment(), false);
        });
    }

    private boolean isValidUrl(String url) {
        return url.matches("^https://www\\.youtube\\.com/watch\\?v=[a-zA-Z0-9_-]{11}$");
    }

    private String getVideoId(String url) {
        return url.substring(url.length() - 11);
    }
}
