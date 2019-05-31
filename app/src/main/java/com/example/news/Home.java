package com.example.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.news.Adapter.NewsAdapter;
import com.example.news.Base.BaseActivity;
import com.example.news.DataBase.NewsDataBase;
import com.example.news.Model.LikeResponse.LikesResponse;
import com.example.news.Model.NewsResponse.ArticlesItem;
import com.example.news.Model.SourcesResponse.SourcesItem;
import com.example.news.Repo.NewsRepository;
import com.like.LikeButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends BaseActivity {

    private TabLayout mChannelLayout;
    private RecyclerView mNewsRecyler;
    NewsRepository newsRepositry;
    SearchView searchView;
    List<ArticlesItem> news;
   // List<LikesResponse> like;
    NewsAdapter newsAdapter;
    RecyclerView.LayoutManager layoutManager;
  //public static  List<LikesResponse> buttons=new ArrayList<>();
    String Lang = Locale.getDefault().getLanguage();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mChannelLayout = findViewById(R.id.channelLayout);
        mNewsRecyler = findViewById(R.id.newsRecyler);
        layoutManager=new LinearLayoutManager(activity);
        newsAdapter=new NewsAdapter(null);
        mNewsRecyler.setAdapter(newsAdapter);
        mNewsRecyler.setLayoutManager(layoutManager);
        newsRepositry = new NewsRepository(Lang);
        newsRepositry.getNewsSources(onSourcePreparedListener);

        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, LikeButton likeButton, LikesResponse likesResponse, ArticlesItem articlesItem) {
                newsRepositry.setLikes(likesResponse);
                likeButton.setTag(R.drawable.thumb_on);
                likeButton.setLikeDrawableRes(R.drawable.thumb_on);


            }
        });
 newsAdapter.setOnButtonClickListener(new NewsAdapter.OnButtonClickListener() {
     @Override
     public void onButtonClick(String articlesItem) {
         Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
         sharingIntent.setType("text/plain");
         String shareBody = articlesItem;
         sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
         sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
         startActivity(Intent.createChooser(sharingIntent, "Share via"));
     }
 });
    newsAdapter.setOnItemsClicksListener(onItemsClicksListener);
    }
    NewsRepository.OnSourcesPreparedListener onSourcePreparedListener = new NewsRepository.OnSourcesPreparedListener(){
        @Override
        public void onSourcePrepared(final List<SourcesItem> sourcesItems) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showSourceInTabLayout(sourcesItems);
                }
            });

        }
    };
  NewsAdapter.OnItemsClicksListener onItemsClicksListener=  new NewsAdapter.OnItemsClicksListener() {
        @Override
        public void onItemClicks(ArticlesItem articlesItem) {
            Intent intent=new Intent(getBaseContext(),StoriesActivity.class);
            intent.putExtra("url",articlesItem.getUrlToImage());
            intent.putExtra("title",articlesItem.getTitle());
            intent.putExtra("date",articlesItem.getPublishedAt());
            intent.putExtra("author",articlesItem.getAuthor());
            intent.putExtra("desc",articlesItem.getDescription());
            intent.putExtra("content",articlesItem.getContent());
            intent.putExtra("link",articlesItem.getUrl());
            startActivityForResult(intent,0);
        }
    };
static String id;
    public void showSourceInTabLayout(List<SourcesItem> sourcesItems) {
        if (sourcesItems == null || sourcesItems.size()==0) return;
        for (SourcesItem sourcesItem : sourcesItems) {
            TabLayout.Tab tab = mChannelLayout.newTab();
            tab.setText(sourcesItem.getName());
            tab.setTag(sourcesItem);
            mChannelLayout.addTab(tab);

        }
        mChannelLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
             SourcesItem source = ((SourcesItem) tab.getTag());
             id=source.getId();
                newsRepositry.getNewsBySourceId(source.getId(), onNewsPreparedListener);



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                SourcesItem source = ((SourcesItem)tab.getTag());

                newsRepositry.getNewsBySourceId(source.getId(), onNewsPreparedListener);



            }
        });
       mChannelLayout.getTabAt(0).select();
    }

static List<ArticlesItem> article=null;

    NewsRepository.OnNewsPreparedListener onNewsPreparedListener=new NewsRepository.OnNewsPreparedListener()

    {
        @Override
        public void onNewsPrepared (final List < ArticlesItem > newsList) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    article=new ArrayList<>(newsList);
                    newsAdapter.changeData(newsList);

                }
            });

    }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(
                android.support.v7.appcompat.R.id.search_src_text)).
                setHintTextColor(getResources().getColor(R.color.colorPrimary));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
    }

            @Override
            public boolean onQueryTextChange(String newText) {
                final  List<ArticlesItem> filtermodelist=filter(article,newText);
                newsAdapter.setfilter(filtermodelist);
                return true;
            }
        });
        return true;
    }
    private List<ArticlesItem> filter(List<ArticlesItem> pl,String query)
    {
        query=query.toLowerCase();
        final List<ArticlesItem> filteredModeList=new ArrayList<>();
        for (ArticlesItem model:pl)
        {
            final String text=model.getTitle().toLowerCase();
            if (text.contains(query))
            {
                filteredModeList.add(model);
            }
        }
        return filteredModeList;
    }
    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }
    /*
NewsRepository.OnLikesPreparedListener onLikesPreparedListener=new NewsRepository.OnLikesPreparedListener() {
    @Override
    public void OnLikesPrepared(String url, LikesResponse likes) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {


            }
        });
    }

};
*/

}
