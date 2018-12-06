package com.example.youma.finalproject;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
/*Reference: https://www.youtube.com/watch?v=SK98ayjhk1E*/
import java.util.ArrayList;

public class Favourite_list extends Activity {

    FoodDatabaseHelper foodDB;

    /**
     *
     * @param saveInstanceState
     */
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_favouritelist);

        ListView listView= (ListView)findViewById(R.id.food_favourite_listview);
        foodDB= new FoodDatabaseHelper(this);

        ArrayList<String> foodList= new ArrayList<>();
        Cursor data= foodDB.getListContents();

        /**
         * This place is created to check that database in Listview that is empty or not empty
         * if not count the data
         */
        if (data.getCount()==0){
            Toast.makeText(Favourite_list.this,"Database is empty",Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                foodList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,foodList);
                listView.setAdapter(listAdapter);
            }
        }
    }


}
