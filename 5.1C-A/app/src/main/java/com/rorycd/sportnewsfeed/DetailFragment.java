package com.rorycd.sportnewsfeed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    ImageView ivHeader;
    TextView tvTitle, tvContent;
    LinearLayout llRelatedStories;

    private static final String ARG_ARTICLE_ID = "articleId";

    private Article article;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int articleId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ARTICLE_ID, articleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int id = getArguments().getInt(ARG_ARTICLE_ID);
            article = ArticleDataSource.getArticleById(id);
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
        ivHeader.setImageResource(article.getImgResId());

        // Set title
        tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getTitle());

        // Set content
        tvContent = view.findViewById(R.id.tvContent);
        tvContent.setText(article.getContent());

        // Set "related stories"
        llRelatedStories = view.findViewById(R.id.llRelatedStories);

        for (Article a : ArticleDataSource.getArticles()) {
            // If a different article has the same category
            if (a.getId() != article.getId() && a.getCategory().equals(article.getCategory())) {

                // Add a card
                View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_related_story, llRelatedStories, false);

                // Set the card's values
                ImageView ivNewsItem = itemView.findViewById(R.id.ivNewsItem);
                TextView tvNewsItemTitle = itemView.findViewById(R.id.tvNewsItemTitle);
                TextView tvNewsItemContent = itemView.findViewById(R.id.tvNewsItemContent);
                ivNewsItem.setImageResource(a.getImgResId());
                tvNewsItemTitle.setText(a.getTitle());

                // Trim article content
                int max_content_length = 100;
                String content = a.getContent();
                String trimmed = content.substring(0, max_content_length) + "...";
                tvNewsItemContent.setText(trimmed);

                // Set onclick for item
                itemView.setOnClickListener(v -> {
                    // Create the new detail fragment based on the current item
                    DetailFragment fragment = DetailFragment.newInstance(a.getId());
                    // Change the fragment
                    ((MainActivity)v.getContext()).changeToFragment(fragment);
                });

                llRelatedStories.addView(itemView);
            }
        }
    }
}