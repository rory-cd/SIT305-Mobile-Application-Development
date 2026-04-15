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
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);

        return new ViewHolder(itemView);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivNewsItem = itemView.findViewById(R.id.ivNewsItem);
            tvNewsItemTitle = itemView.findViewById(R.id.tvNewsItemTitle);
            tvNewsItemContent = itemView.findViewById(R.id.tvNewsItemContent);
        }
    }

    public void updateList(List<Article> newList) {
        this.articleList = newList;
        notifyDataSetChanged();
    }
}
