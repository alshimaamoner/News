package com.example.news.API;




import com.example.news.Model.NewsResponse.NewsResponse;
import com.example.news.Model.SourcesResponse.SourcesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Mohamed Nabil Mohamed (Nobel) on 2/15/2019.
 * byte code SA
 * m.nabil.fci2015@gmail.com
 */
public interface Services {

    @GET("sources")
    Call<SourcesResponse> getNewsSources(@Query("apiKey") String apikey,
                                         @Query("language") String language);


    @GET("everything")
    Call<NewsResponse> getNewsBySourceId(@Query("apiKey") String apikey,
                                         @Query("language") String language,
                                         @Query("sources") String sources);
}
