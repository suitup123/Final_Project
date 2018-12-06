package com.example.youma.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MovieInformation extends Activity {

    /**
     * @author Nguyen Gia Nguyen
     *
     * @param savedInstanceState
     */

    protected static final String ACTIVITY_NAME = "Movie";
    ListView movieView;
    Button searchBtn;
    EditText searchMovie;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);
        progress = findViewById(R.id.progress);
        movieView = findViewById(R.id.movieListView);
        searchBtn = findViewById(R.id.searchButton);
        searchMovie = findViewById(R.id.movieSearch);

        /*
         *Description: User click Search button after input a string of movie they want to search.
         */
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = searchMovie.getText().toString();
                Log.i(ACTIVITY_NAME, search);
                Intent intent = new Intent(MovieInformation.this, MovieInformationFragment.class);
                intent.putExtra("Search", search);
                startActivity(intent);
                Toast.makeText(MovieInformation.this, R.string.searching, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
