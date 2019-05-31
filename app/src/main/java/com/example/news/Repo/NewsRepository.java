package com.example.news.Repo;

import android.util.Log;

import com.example.news.API.ApiManager;
import com.example.news.DataBase.NewsDataBase;
import com.example.news.Model.LikeResponse.LikesResponse;
import com.example.news.Model.NewsResponse.ArticlesItem;
import com.example.news.Model.NewsResponse.NewsResponse;
import com.example.news.Model.SourcesResponse.SourcesItem;
import com.example.news.Model.SourcesResponse.SourcesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsRepository {
    String Lang;
    public static final String APIKEY = "b157611e9895467ebfb67ff2c10f9c5b";
//  public static List<LikeButton> likes=new ArrayList<>();


    public NewsRepository(String Lang){
        this.Lang = Lang;
    }

    public void getNewsSources(final OnSourcesPreparedListener
                                       onSourcesPreparedListener){
        ApiManager
                .getAPIs()
                .getNewsSources(APIKEY,Lang)
                .enqueue(new Callback<SourcesResponse>() {
                    @Override
                    public void onResponse(Call<SourcesResponse> call,
                                           Response<SourcesResponse> response) {
                        if(response.isSuccessful()){
                            if(onSourcesPreparedListener!=null) {
                                List<SourcesItem> list =
                                        response.body().getSources();
                                onSourcesPreparedListener.onSourcePrepared(list);
                                //open dB insert sources
                                InsertSourcesThread th = new InsertSourcesThread(list);
                                th.start();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<SourcesResponse> call, Throwable t) {
                        RetrieveSourcesThread th =
                                new RetrieveSourcesThread(onSourcesPreparedListener);
                        th.start();

                    }
                });
    }
      // List<String> url=new ArrayList<>() ;
    List<ArticlesItem> newsList=new ArrayList<>();
    public void getNewsBySourceId(final String sourceId,
                                  final OnNewsPreparedListener onNewsPreparedListener) {
        ApiManager.getAPIs()
                .getNewsBySourceId(APIKEY, Lang, sourceId)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call,
                                           Response<NewsResponse> response) {
                        if (response.isSuccessful()) {
                               newsList = response.body().getArticles();

                            if (onNewsPreparedListener != null) {
                                onNewsPreparedListener.onNewsPrepared(newsList);
                                InsertArticlesThread th = new InsertArticlesThread(newsList);
                                th.start();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        RetrieveNewsBySourceIdThread th =
                                new RetrieveNewsBySourceIdThread(sourceId, onNewsPreparedListener);
                        th.start();
                        for (ArticlesItem articlesItem : newsList) {
                            NewsDataBase.getInstance().LikesDao().getLikeByUrl(articlesItem.getUrl());
                        }




                    }
                });

    }
 /*  public  void getLikes(String url,OnLikesPreparedListener onLikesPreparedListener){
        RetrieveLikesThread th =
                new RetrieveLikesThread(url,onLikesPreparedListener);
        th.start();

    }
    */
    public  void setLikes(LikesResponse likes){
        InsertLikesThread th =
                new InsertLikesThread(likes);
        th.start();

    }

    public interface OnSourcesPreparedListener{
        void onSourcePrepared(List<SourcesItem> sources);
    }
    public interface OnNewsPreparedListener{
        void onNewsPrepared(List<ArticlesItem> newsList);
    }
  /*  public interface  OnLikesPreparedListener{
        void  OnLikesPrepared(String url,LikesResponse likes);
    }

*/
    class InsertSourcesThread extends Thread{

        List<SourcesItem> sourcesList;
        public InsertSourcesThread(List<SourcesItem> sourcesItems){
            this.sourcesList = sourcesItems;
        }
        @Override
        public void run() {
            NewsDataBase.getInstance()
                    .sourcesDao()
                    .insertSourcesList(sourcesList);
        }
    }
   static class InsertLikesThread extends Thread{
        LikesResponse likesResponses;
        public InsertLikesThread(LikesResponse likesResponses){
            this.likesResponses = likesResponses;

        }
        @Override
        public void run() {
            NewsDataBase.getInstance()
                    .LikesDao()
                    .insertLikeList(likesResponses);
        }
    }

    class RetrieveSourcesThread extends Thread{
        OnSourcesPreparedListener onSourcesPreparedListener;
        RetrieveSourcesThread(OnSourcesPreparedListener onSourcesPreparedListener){
            this.onSourcesPreparedListener = onSourcesPreparedListener;
        }
        @Override
        public void run() {
            List<SourcesItem> sources = NewsDataBase.getInstance()
                    .sourcesDao()
                    .getAllSources();
            onSourcesPreparedListener.onSourcePrepared(sources);
        }
    }


   /*class RetrieveLikesThread extends Thread {
        OnLikesPreparedListener onLikesPreparedListener;
         String url;
        RetrieveLikesThread(String url,OnLikesPreparedListener onLikesPreparedListener) {
            this.url=url;
            this.onLikesPreparedListener = onLikesPreparedListener;

        }


        @Override
        public void run() {
            List<LikesResponse> likes = NewsDataBase.getInstance()
                    .LikesDao()
                    .getLikeByUrl(url);
            Log.e("list size", likes.size() + "");

        }
    }
*/

        class InsertArticlesThread extends Thread {

            List<ArticlesItem> newsList;

            public InsertArticlesThread(List<ArticlesItem> list) {
                this.newsList = list;
            }

            @Override
            public void run() {
                for (ArticlesItem item : newsList) {
                    item.setSourceId(item.getSource().getId());
                    item.setSourceName(item.getSource().getName());
                   // item.setLikes(item.getLikes());
                }

                NewsDataBase.getInstance()
                        .newsDao()
                        .insertNewsList(newsList);

            }
        }

        class RetrieveNewsBySourceIdThread extends Thread {

            String sourceId;
            OnNewsPreparedListener onNewsPreparedListener;

            public RetrieveNewsBySourceIdThread(String sourceId, OnNewsPreparedListener listener) {
                this.sourceId = sourceId;
                onNewsPreparedListener = listener;

            }

            @Override
            public void run() {

                List<ArticlesItem> newsList = NewsDataBase.getInstance().newsDao()
                        .getNewsBySourceId(sourceId);
                Log.e("list size", newsList.size() + "");
                onNewsPreparedListener.onNewsPrepared(newsList);
            }
        }


    }
