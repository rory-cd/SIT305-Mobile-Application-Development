package com.rorycd.sportnewsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopStoriesViewAdapter extends RecyclerView.Adapter<TopStoriesViewAdapter.ViewHolder> {

    private List<Article> articleList;
    private Context context;

    public TopStoriesViewAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public TopStoriesViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.article_card_square, parent, false);

        return new ViewHolder(itemView, articleList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivNewsItem.setImageResource(articleList.get(position).getImgResId());
        holder.tvNewsItemTitle.setText(articleList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNewsItem;
        TextView tvNewsItemTitle;

        public ViewHolder(@NonNull View itemView, List<Article> articleList) {
            super(itemView);
            ivNewsItem = itemView.findViewById(R.id.ivNewsItem);
            tvNewsItemTitle = itemView.findViewById(R.id.tvNewsItemTitle);

            // Set onclick for item
            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();

                // If it's a valid position
                if (position != RecyclerView.NO_POSITION) {
                    Article article = articleList.get(position);

                    // Create the new detail fragment based on the current item
                    DetailFragment fragment = DetailFragment.newInstance(article.getId());

                    // Change the fragment
                    ((MainActivity)v.getContext()).changeToFragment(fragment);
                }
            });
        }
    }
}
