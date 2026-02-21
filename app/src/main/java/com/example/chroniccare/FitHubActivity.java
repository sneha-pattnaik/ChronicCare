package com.example.chroniccare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chroniccare.database.AppDatabase;
import com.example.chroniccare.database.User;
import com.example.chroniccare.model.Article;
import com.example.chroniccare.utils.ArticleProvider;
import com.example.chroniccare.utils.ProfileImageHelper;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

public class FitHubActivity extends BottomNavActivity {
    
    private CircleImageView profileImage;
    private LinearLayout articlesContainer;
    private AppDatabase db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        profileImage = findViewById(R.id.profile_image);
        articlesContainer = findViewById(R.id.articlesContainer);
        
        ProfileImageHelper.loadProfileImage(this, profileImage);
        profileImage.setOnClickListener(v -> ProfileImageHelper.handleProfileClick(this));
        
        findViewById(R.id.webView_gentle_yoga).setOnClickListener(v -> {
            openYouTubeVideo("EvMTrP8eRvM");
        });
        
        db = AppDatabase.getInstance(this);
        loadPersonalizedArticles();
    }
    
    private void loadPersonalizedArticles() {
        Executors.newSingleThreadExecutor().execute(() -> {
            SharedPreferences prefs = getSharedPreferences("ChronicCarePrefs", MODE_PRIVATE);
            String userId = prefs.getString("userId", null);
            
            String userConditions = "";
            if (userId != null) {
                User user = db.userDao().getUserByUserId(userId);
                if (user != null && user.conditions != null) {
                    userConditions = user.conditions;
                }
            }
            
            List<Article> articles = ArticleProvider.getPersonalizedArticles(userConditions);
            
            runOnUiThread(() -> {
                articlesContainer.removeAllViews();
                for (Article article : articles) {
                    addArticleCard(article);
                }
            });
        });
    }
    
    private void addArticleCard(Article article) {
        View cardView = LayoutInflater.from(this).inflate(R.layout.item_article_card, articlesContainer, false);
        
        ImageView articleImage = cardView.findViewById(R.id.articleImage);
        TextView articleTitle = cardView.findViewById(R.id.articleTitle);
        TextView articleReadTime = cardView.findViewById(R.id.articleReadTime);
        
        articleTitle.setText(article.title);
        articleReadTime.setText(article.readTime);
        
        Picasso.get()
            .load(article.imageUrl)
            .placeholder(R.drawable.img_article)
            .error(R.drawable.img_article)
            .into(articleImage);
        
        cardView.setOnClickListener(v -> openUrl(article.url));
        articlesContainer.addView(cardView);
    }
    
    private void openUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open link", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        ProfileImageHelper.loadProfileImage(this, profileImage);
    }
    
    private void openYouTubeVideo(String videoId) {
        try {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
            startActivity(appIntent);
        } catch (Exception e) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, 
                Uri.parse("https://www.youtube.com/watch?v=" + videoId));
            startActivity(webIntent);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fit_hub;
    }

    @Override
    protected int getBottomNavMenuItemId() {
        return R.id.nav_fithub;
    }
}
