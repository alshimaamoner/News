package com.example.news.Model.LikeResponse;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.Nullable;

import com.example.news.Model.NewsResponse.ArticlesItem;
import com.example.news.Model.SourcesResponse.SourcesItem;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = ArticlesItem.class,
        parentColumns = "url",
        childColumns = "url",
        onDelete = CASCADE))
public class LikesResponse {
    @PrimaryKey
    boolean likes;
    String url;

    public LikesResponse(boolean likes, String url) {
        this.likes = likes;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }




    public boolean isLikes() {
        return likes;
    }

    public void setLikes(boolean likes) {
        this.likes = likes;
    }


}
