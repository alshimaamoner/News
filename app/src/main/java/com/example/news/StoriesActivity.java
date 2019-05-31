package com.example.news;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class StoriesActivity extends AppCompatActivity {

    ImageButton mBack;
    Toolbar toolbar;
    private AppBarLayout mAppBarLayout;
    private ImageView mUrl;
    private TextView mTitle;
    private TextView mDate;
    private TextView mDash;
    private TextView mAuthor;
    private TextView mDesc;
    private View mView;
    private TextView mContent;
    private ScrollView mScrollable;
    private TextView mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        toolbar = findViewById(R.id.toolbar);
        mAppBarLayout = findViewById(R.id.appBarLayout);
        setSupportActionBar(toolbar);
        init();
        mBack.setOnClickListener(onClickListener);
        Glide.with(this).load(getIntent().getStringExtra("url"))
                .into(mUrl);
        mTitle.setText(getIntent().getStringExtra("title"));
        mDate.setText(Html.fromHtml(getIntent().getStringExtra("date") + " | " + "<font color=#B00227>"+getIntent().getStringExtra("author")
                +"</font>"));
        mDesc.setText(getIntent().getStringExtra("desc"));
        mContent.setText(Html.fromHtml(getIntent().getStringExtra("content")+"<br>"+"<font color=#B00227>"+"For More Information:"
                        +"</font>"));
        mLink.setText(getIntent().getStringExtra("link"));
    }

  View.OnClickListener onClickListener=  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // startActivity(new Intent(getBaseContext(), Home.class));
            finish();
        }
    };

    public void init() {
        mBack = findViewById(R.id.back);
        mUrl = findViewById(R.id.url);
        mTitle = findViewById(R.id.title);
        mDate = findViewById(R.id.date);
        mLink = findViewById(R.id.Link);
        mDesc = findViewById(R.id.desc);
        mView = findViewById(R.id.view);
        mScrollable = findViewById(R.id.scrollable);
        mContent = findViewById(R.id.content);
    }
}
