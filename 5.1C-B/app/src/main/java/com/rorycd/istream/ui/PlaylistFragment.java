package com.rorycd.istream.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rorycd.istream.Navigator;
import com.rorycd.istream.R;
import com.rorycd.istream.data.UserRepository;

public class PlaylistFragment extends Fragment {

    public PlaylistFragment() {
        // Required empty public constructor
    }

    public static PlaylistFragment newInstance() {
        return new PlaylistFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Navigator nav = (Navigator) requireActivity();
        UserRepository repo = UserRepository.getInstance(requireContext());
        LinearLayout llPlaylist = view.findViewById(R.id.llPlaylist);

        // Get the playlist and inflate the list of URLs
        repo.getPlaylist((playlist) -> {
            for (String url : playlist) {
                // Add a url
                TextView textView = new TextView(requireContext());
                textView.setText(url);
                textView.setTextSize(16);
                textView.setPadding(32, 32, 32, 32);

                // Add onClick
                textView.setOnClickListener((v) -> {
                    String videoId = url.substring(url.length() - 11);
                    nav.navigateTo(PlayFragment.newInstance(videoId), true);
                });

                llPlaylist.addView(textView);
            }
        });
    }
}
