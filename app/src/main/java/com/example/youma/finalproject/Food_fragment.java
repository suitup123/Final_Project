package com.example.youma.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

public class Food_fragment extends Activity {

    protected  final static String ACTIVITY_NAME = "Food_fragment";

        protected String food_name="";
    //protected final static String uml="https://api.edamam.com/api/food-database/parser?app_id=6a50376a&app_key=244ae4242068125a61412a255d4b6a26&ingr="+food_name;

    protected ProgressBar progressBar;
    protected TextView calorieTextView, proteinTextView, fatTextView, fiberTextView,foodlabelTextview;
    protected Button saveButton,viewfoodbutton;
    String alltext;
    FoodDatabaseHelper foodDB;
    @Override
    protected  void onCreate (Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.food_information_fragment);

        foodlabelTextview = (TextView)findViewById(R.id.food_label);
        calorieTextView = (TextView)findViewById(R.id.calorie) ;

        fatTextView = (TextView)findViewById(R.id.fat) ;
        saveButton=(Button) findViewById(R.id.food_save);

       Bundle intent = getIntent().getExtras();
       food_name = intent.getString("userInput");

      // final String food_name = intent.getStringExtra("userInput");

        progressBar = findViewById(R.id.food_fragment_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);

        foodDB = new FoodDatabaseHelper(this);

        ForecastQuery foodQuery = new ForecastQuery();
        foodQuery.execute();

        /**
         * By clicking on the savebutton, it will get the string of text to put in the list view
         */
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME,"SAVE BUTTON ONCLICK");
                alltext = foodlabelTextview.getText().toString() +"\n"+calorieTextView.getText().toString()+"\n"+fatTextView.getText().toString();
                AddData(alltext);
            }
        });

        viewfoodbutton = (Button) findViewById(R.id.viewfoodfavourite);
        viewfoodbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Food_fragment.this, Favourite_list.class);
                startActivity(intent);
            }
        });


    }

    /**
     * it uses the string to check that data that has been saved successfully to the list view by toasting the text
     * @param save
     */
    public void AddData(String save){
        boolean saveData=foodDB.addData(save);
      if (saveData==true){
            Toast.makeText(Food_fragment.this,getString(R.string.errormessage1),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(Food_fragment.this,getString(R.string.errormessage2),Toast.LENGTH_LONG).show();
    }
    }

    /**
     *
     */
    private class ForecastQuery extends AsyncTask<String,Integer,String>{
        //Intent intent = getIntent();
        //String food_name = intent.getStringExtra("userInput");

        String foodtile="";
        String calorie="";
        String protein="";
        String fat="";
        String fiber="";
        private HttpURLConnection conn;
        BufferedReader reader = null;

        /**
         *it uses the string of url to go to the website to get the tag of strings to display
         * @param strings
         * @return null
         */
        @Override
        protected String doInBackground(String... strings) {

            /*
            * Reference: https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android
            * */
            try {
                URL url = new URL("https://api.edamam.com/api/food-database/parser?app_id=6a50376a&app_key=244ae4242068125a61412a255d4b6a26&ingr=" + food_name);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection. getInputStream();

               reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"), 8);

                StringBuffer buffer = new StringBuffer();

                String line;
                while ((line = reader.readLine())!=null) {
                    buffer.append(line +"\n");
                }

                JSONObject jsonObject = new JSONObject(buffer.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("parsed");
                JSONObject jsonFood = jsonArray.getJSONObject(0).getJSONObject("food");
                foodtile= jsonFood.getString("label");
                JSONObject jsonNutrients =jsonFood.getJSONObject("nutrients");
                 calorie = jsonNutrients.getString("ENERC_KCAL");
                 fat = jsonNutrients.getString("FAT");

                 }catch(Exception e){
                        e.printStackTrace();
                        //  Toast.makeText(Food_fragment.this,getString(R.string.toast_nutrition), Toast.LENGTH_LONG).show();
                    }
                    return null;

            }


        /**
         * it use values to set the action of progressBar
         * @param values
         */
        @Override
                protected void onProgressUpdate (Integer...values){
                    super.onProgressUpdate(values);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(values[0]);
                }

        /**
         * it use the s type string to set all the text to display
         * @param s
         */
        @Override
                protected void onPostExecute (String s){
                    super.onPostExecute(s);
                    foodlabelTextview.setText(""+foodtile);
                    calorieTextView.setText(getString(R.string.calorie)+": " + calorie);

                    fatTextView.setText(getString(R.string.fat)+": " + fat);


                    progressBar.setVisibility(View.INVISIBLE);
                }

    }
}
