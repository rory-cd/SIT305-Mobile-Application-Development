package com.rorycd.sportnewsfeed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailFragment extends Fragment {

    ImageView ivHeader;
    TextView tvTitle, tvContent;

    private static final String ARG_IMG_RES = "imgRes";
    private static final String ARG_TITLE = "title";
    private static final String ARG_CONTENT = "content";

    private int imgRes;
    private String title;
    private String content;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int imgRes, String title, String content) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMG_RES, imgRes);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENT, content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgRes = getArguments().getInt(ARG_IMG_RES);
            title = getArguments().getString(ARG_TITLE);
            content = getArguments().getString(ARG_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set header image
        ivHeader = view.findViewById(R.id.ivHeader);
        ivHeader.setImageResource(imgRes);

        // Set title
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        // Set content
        tvContent = view.findViewById(R.id.tvContent);
        tvContent.setText(content);
    }
}