package com.example.news.DataBase.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.news.Model.LikeResponse.LikesResponse;
import com.example.news.Model.NewsResponse.ArticlesItem;

import java.util.List;
@Dao
public interface LikesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void  insertLikeList(LikesResponse items);
    @Query("select * from LikesResponse where url=:url")
    List<LikesResponse> getLikeByUrl(String url);
    @Query("select * from LikesResponse")
    List<LikesResponse> getLike();
}

