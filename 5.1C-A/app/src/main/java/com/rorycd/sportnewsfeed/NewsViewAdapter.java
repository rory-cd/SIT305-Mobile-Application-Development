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

public class NewsViewAdapter extends RecyclerView.Adapter<NewsViewAdapter.ViewHolder> {

    private List<Article> articleList;
    private Context context;

    public NewsViewAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.article_card_vertical, parent, false);

        return new ViewHolder(itemView, articleList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivNewsItem.setImageResource(articleList.get(position).getImgResId());
        holder.tvNewsItemTitle.setText(articleList.get(position).getTitle());

        // Trim article content
        int max_content_length = 65;
        String content = articleList.get(position).getContent();
        String trimmed = content.substring(0, max_content_length) + "...";
        holder.tvNewsItemContent.setText(trimmed);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNewsItem;
        TextView tvNewsItemTitle, tvNewsItemContent;

        public ViewHolder(@NonNull View itemView, List<Article> articleList) {
            super(itemView);
            ivNewsItem = itemView.findViewById(R.id.ivNewsItem);
            tvNewsItemTitle = itemView.findViewById(R.id.tvNewsItemTitle);
            tvNewsItemContent = itemView.findViewById(R.id.tvNewsItemContent);

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

    public void updateList(List<Article> newList) {
        this.articleList = newList;
        notifyDataSetChanged();
    }
}
