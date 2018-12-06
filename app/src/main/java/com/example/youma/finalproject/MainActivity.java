package com.example.youma.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Main class contains 3 buttons for 3 activities: Food Information, Movie Information, CBC News Reader.
 */
public class MainActivity extends Activity {

    Button foodInfo, movieInfo, cbcInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodInfo = findViewById(R.id.food_author);
        movieInfo = findViewById(R.id.movie_author);
        cbcInfo = findViewById(R.id.news_author);

        foodInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent food_view = new Intent(MainActivity.this, FoodInformation.class);
                startActivity(food_view);
                Log.i("test", "onCreate");
            }
        });

        movieInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent movie_view = new Intent(MainActivity.this, MovieInformation.class);
                startActivity(movie_view);
            }
        });

        cbcInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent news_view = new Intent(MainActivity.this, CbcnewsActivity.class);
                startActivity(news_view);
            }
        });
    }
}
