package com.rorycd.sportnewsfeed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView rvTopStories, rvNews;
    NewsViewAdapter rvNewsAdapter;
    TextView tvNoResults;
    EditText etFilter;
    TopStoriesViewAdapter rvTopStoriesAdapter;

    private List<Article> articleList = new ArrayList<>();
    private final List<Article> filteredList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articleList = ArticleDataSource.getArticles();
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

        // Set "top stories" view adapter
        rvTopStories = view.findViewById(R.id.rvTopStories);
        rvTopStoriesAdapter = new TopStoriesViewAdapter(articleList, requireContext());
        rvTopStories.setAdapter(rvTopStoriesAdapter);
        rvTopStories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        // Set "news" view adapter
        rvNews = view.findViewById(R.id.rvNews);
        rvNewsAdapter = new NewsViewAdapter(articleList, requireContext());
        rvNews.setAdapter(rvNewsAdapter);
        rvNews.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        // News filter
        tvNoResults = view.findViewById(R.id.tvNoResults);
        etFilter = view.findViewById(R.id.etFilter);
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Article> results = filterArticles(s.toString());
                // Show "no results found" text
                if (results.isEmpty()) {
                    tvNoResults.setVisibility(View.VISIBLE);
                } else {
                    tvNoResults.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private List<Article> filterArticles(String query) {
        filteredList.clear();
        for (Article article : articleList) {

            String title = article.getTitle().toLowerCase();
            String category = article.getCategory().toLowerCase();
            String content = article.getContent().toLowerCase();

            if (title.contains(
                    query.toLowerCase()) ||
                    category.contains(query.toLowerCase()) ||
                    content.contains(query.toLowerCase())
            ) {
                filteredList.add(article);
            }
            rvNewsAdapter.updateList(filteredList);
        }
        return filteredList;
    }
}