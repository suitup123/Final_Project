package com.example.youma.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.design.widget.Snackbar;
import android.widget.Toolbar;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static com.example.youma.finalproject.MovieInformation.ACTIVITY_NAME;


/**
 * @author Nguyen Gia Nguyen
 *
 * Method member: movie_title, movie_description, movie_year, movie_rating, movie_actors, movie_length, movie_genre, posterImage, addButton
 * favButton, other, progressBar.
 * Description: Add funtion for addButton, favButton, other.
 * Get input from user through Bundle.
 *
 */
public class MovieInformationFragment extends Activity {

    protected static final String URL_STRING = "http://www.omdbapi.com/?apikey=6c9862c2&r=xml&t=";
    protected ImageView posterImage;
    TextView movie_title, movie_description, movie_year, movie_rating, movie_actors, movie_length, movie_genre;
    Button addButton, favButton, other;
    ProgressBar progressBar;
    Toolbar toolbar;
    MovieDataBaseHelper moviedb;
    public String search_movie = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information_fragment);

        movie_title = findViewById(R.id.title);
        movie_description = findViewById(R.id.description);
        movie_year  = findViewById(R.id.year);
        movie_rating= findViewById(R.id.rating);
        movie_actors= findViewById(R.id.actors);
        movie_length= findViewById(R.id.length);
        movie_genre = findViewById(R.id.genre);
        posterImage = findViewById(R.id.posterImage);
        addButton   = findViewById(R.id.addButton);
        favButton   = findViewById(R.id.favouriteButton);
        other       = findViewById(R.id.other);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);

        moviedb = new MovieDataBaseHelper(this);

        Bundle extras = getIntent().getExtras();
        search_movie = extras.getString("Search");

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieInformationFragment.this, ToolbarActivities.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieInformationFragment.this);
                builder.setMessage(R.string.add_list)
                       .setTitle(R.string.dialog_title)
                       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int id) {
                               Snackbar.make(addButton, R.string.message, Snackbar.LENGTH_LONG).show();
//                               String allText = new String();
//                               allText = movie_title.getText().toString() +"\n"+ movie_description.getText().toString() +"\n"+ movie_year.getText().toString() +"\n"+
//                                       movie_rating.getText().toString() +"\n"+ movie_actors.getText().toString() +"\n"+ movie_length.getText().toString() +"\n"+
//                                       movie_genre.getText().toString();
//                               addData(allText);
                           }
                       })
                       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int id) {

                           }
                       })
                       .show();
            }
        });

        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ForecastQuery forecast = new ForecastQuery();
        forecast.execute();
    }

//    public void addData(String save){
//        boolean saveData = moviedb.addMovie(save);
//    }

    class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String title;
        private String description;
        private String year;
        private String rating;
        private String actors;
        private String length;
        private String genre;
        private String poster;
        private Bitmap bitmap;
        private HttpURLConnection connection;

        @Override
        protected String doInBackground(String... strings) {
            InputStream stream;

            /**
             * Connect to URL and read data
             */
            try {
                URL url = new URL(URL_STRING + URLEncoder.encode(search_movie, "UTF-8"));
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000); //in milliseconds
                connection.setConnectTimeout(15000); //in milliseconds
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                stream = connection.getInputStream();

                XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                parserFactory.setNamespaceAware(true);
                XmlPullParser parser = parserFactory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(stream, null);

                int eventType = parser.getEventType();
                boolean set = false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("root")) {
                            set = true;
                        } else if (name.equalsIgnoreCase("movie") && set) {
                            title = parser.getAttributeValue(null, "title");
                            description = parser.getAttributeValue(null, "plot");
                            year = parser.getAttributeValue(null, "year");
                            rating = parser.getAttributeValue(null, "imdbRating");
                            actors = parser.getAttributeValue(null, "actors");
                            length = parser.getAttributeValue(null, "runtime");
                            genre = parser.getAttributeValue(null, "genre");
                            poster = parser.getAttributeValue(null, "poster");
                            int lastSlash = poster.lastIndexOf("/");
                            String filename = poster.substring(lastSlash + 1);
                            if (!fileExistence(filename)) {//Check to see if poster exist
                                Log.i(ACTIVITY_NAME, "Poster exists, read file");
                                bitmap = getImage(poster);
                                FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                            }
                            FileInputStream fis = null;

                            try {
                                fis = openFileInput(filename);

                            } catch (FileNotFoundException e) {
                                Log.i(ACTIVITY_NAME, actors + "is not found, need to download");
                            }
                            bitmap = BitmapFactory.decodeStream(fis);
                            publishProgress(100);
                        }
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Make progress bar visible.
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String args) {
            super.onPostExecute(args);
            movie_title.setText("Title: " + title);
            movie_description.setText("Description: " + description);
            movie_actors.setText("Actor: " + actors);
            movie_year.setText("Year: " + year);
            movie_genre.setText("Genre: " + genre);
            movie_length.setText("Length: " + length);
            movie_rating.setText("Rating: " + rating);
            posterImage.setImageBitmap(bitmap);
        }

        public boolean fileExistence(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        /**
         * Get image from URL
         * @param url
         * @return
         */
        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        public Bitmap getImage(String fname) {
            try {
                URL url = new URL(fname);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

    }
}
