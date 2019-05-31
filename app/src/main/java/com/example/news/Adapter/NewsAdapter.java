package com.example.news.Adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.news.DataBase.NewsDataBase;
import com.example.news.Model.LikeResponse.LikesResponse;
import com.example.news.Model.NewsResponse.ArticlesItem;
import com.example.news.R;
import com.example.news.Repo.NewsRepository;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<ArticlesItem> newsList;
    List<LikesResponse> like=new ArrayList<>();
    OnItemClickListener onItemClickListener;

    public void setOnItemsClicksListener(NewsAdapter.OnItemsClicksListener onItemsClicksListener) {
        OnItemsClicksListener = onItemsClicksListener;
    }

    OnItemsClicksListener OnItemsClicksListener;

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }

    OnButtonClickListener onButtonClickListener;
  LikesResponse likes;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NewsAdapter(List<ArticlesItem> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_news, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final ArticlesItem news = newsList.get(i);

        viewHolder.title.setText(news.getTitle());
        if (news.getSource() != null) {
            viewHolder.sourceName.setText(news.getSource().getName());
        } else {
            viewHolder.sourceName.setText(news.getSourceName());
        }
        viewHolder.time.setText(news.getPublishedAt());
        Glide.with(viewHolder.itemView).load(news.getUrlToImage())
                .into(viewHolder.imageView);

        if (onItemClickListener != null) {
            viewHolder.likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    onItemClickListener.onItemClick(i,likeButton,new LikesResponse(true,news.getUrl()),news);

                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    onItemClickListener.onItemClick(i,likeButton,new LikesResponse(false,news.getUrl()),news);


                }
            });
        }
        if(onButtonClickListener!=null){
            viewHolder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClickListener.onButtonClick(news.getUrl());
                }
            });
        }
    if(OnItemsClicksListener!=null){
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnItemsClicksListener.onItemClicks(news);
            }
        });
    }
    }


    public void changeData(List<ArticlesItem> newsList){
        this.newsList=newsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(newsList==null) return 0;
        return newsList.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        TextView title,sourceName,time;
        ImageView imageView;
       LikeButton likeButton;
       ImageButton share;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.sourceTitle);
            sourceName=itemView.findViewById(R.id.sourceName);
            time=itemView.findViewById(R.id.newsTime);
            imageView=itemView.findViewById(R.id.imageView);
            likeButton=itemView.findViewById(R.id.star_button);
            share=itemView.findViewById(R.id.share);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int pos,LikeButton likeButton,LikesResponse likesResponse,ArticlesItem articlesItem);
    }
    public interface OnButtonClickListener{
        void onButtonClick(String articlesItem);
    }
    public  interface  OnItemsClicksListener{
        void onItemClicks(ArticlesItem articlesItem);
    }

}

