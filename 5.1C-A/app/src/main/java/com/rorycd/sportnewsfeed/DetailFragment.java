package com.rorycd.sportnewsfeed;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailFragment extends Fragment {

    private ImageView ivHeader;
    private TextView tvTitle, tvContent, tvRelatedStories;
    private LinearLayout llRelatedStories;
    private MaterialButton btnBookmark;
    private BookmarkDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

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
        database = BookmarkDatabase.getInstance(requireContext());
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

        // Bookmark
        btnBookmark = view.findViewById(R.id.btnBookmark);

        executorService.execute(() -> {
            boolean isBookmarked = database.bookmarkDao().isBookmarked(article.getId());

            getActivity().runOnUiThread(() -> {
                setBookmarkIconStatus(isBookmarked);
            });
        });

        // Set bookmark listener
        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = article.getId();

                executorService.execute(() -> {
                    boolean isBookmarked = database.bookmarkDao().isBookmarked(id);

                    if (isBookmarked) {
                        database.bookmarkDao().delete(id);
                        getActivity().runOnUiThread(() -> setBookmarkIconStatus(false));
                    } else {
                        database.bookmarkDao().insert(new Bookmark(id));
                        getActivity().runOnUiThread(() -> setBookmarkIconStatus(true));
                    }
                });
            }
        });

        // Set "related stories"
        llRelatedStories = view.findViewById(R.id.llRelatedStories);

        int relatedStoryCount = 0;

        for (Article a : ArticleDataSource.getArticles()) {
            // If a different article has the same category
            if (a.getId() != article.getId() && a.getCategory().equals(article.getCategory())) {
                // Add a card
                View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.article_card_horizontal, llRelatedStories, false);

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
                relatedStoryCount++;
            }
        }

        if (relatedStoryCount == 0) {
            tvRelatedStories = view.findViewById(R.id.tvRelatedStories);
            tvRelatedStories.setVisibility(View.INVISIBLE);
        }
    }

    public void setBookmarkIconStatus(boolean isBookmarked) {
        int iconId = isBookmarked ? R.drawable.ic_bookmark_added : R.drawable.ic_bookmark;
        Drawable icon = ContextCompat.getDrawable(requireContext(), iconId);
        btnBookmark.setIcon(icon);
    }
}
