package com.rorycd.sportnewsfeed;

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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookmarksFragment extends Fragment {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private LinearLayout llBookmarks;

    public BookmarksFragment() {
        // Required empty public constructor
    }

    public static BookmarksFragment newInstance() {
        return new BookmarksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set articles
        llBookmarks = view.findViewById(R.id.llBookmarks);

        BookmarkDatabase database = BookmarkDatabase.getInstance(requireContext());

        executorService.execute(() -> {
            int[] bookmarks = database.bookmarkDao().getAllBookmarks();
            getActivity().runOnUiThread(() -> setArticles(bookmarks));
        });
    }

    private void setArticles(int[] bookmarks) {
        for (int id : bookmarks) {
            // Get the corresponding article
            Article a = ArticleDataSource.getArticleById(id);
            if (a == null) return;

            // Add a card
            View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.article_card_horizontal, llBookmarks, false);

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

            llBookmarks.addView(itemView);
        }
    }
}
