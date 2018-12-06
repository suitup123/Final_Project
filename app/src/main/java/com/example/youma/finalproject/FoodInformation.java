package com.example.youma.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toolbar;

public class FoodInformation extends Activity  {

    protected static final String ACTIVITY_NAME = "FoodInformation";

    /**
     * create state
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_information);

        Toolbar lab8_toolbar = (Toolbar) findViewById(R.id.food_toolbar);
        setActionBar(lab8_toolbar);



        ListView f_listview= (ListView)findViewById(R.id.food_listview);
        final EditText editText= (EditText)findViewById(R.id.food_edittext) ;
        Button search = (Button)findViewById(R.id.food_button) ;

        /**
         * search buton will also get the text from edit text and past to the Food_fragment class to put into the URL for searching
         */
        search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String input = editText.getText().toString();
                if (editText.length()!=0){
                   // AddData(input);
                    Intent intent = new Intent(FoodInformation.this,Food_fragment.class );
                    intent.putExtra("userInput",input);

                    startActivity(intent);
                    editText.setText("");
                }else{
                    Toast.makeText(FoodInformation.this,getString(R.string.errormessage),Toast.LENGTH_LONG).show();
                }
               // Intent intent = new Intent(FoodInformation.this,Food_fragment.class );
                //intent.putExtra("userInput",input);

               // startActivity(intent);

            }
        });
        Toast.makeText(FoodInformation.this,getString(R.string.toast_nutrition), Toast.LENGTH_LONG).show();

    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.food_toolbar_menu,m);
        //getMenuInflater().inflate(R.menu.food_toolbar_menu, m);
        return true;

    }

    /**
     * this is the option for user for click, favourite list and information about activity
     * @param mi
     * @return
     */
    public boolean onOptionsItemSelected (MenuItem mi){
        switch (mi.getItemId()){
            case R.id.favourite:
                Intent intent= new Intent(this, Favourite_list.class);
                this.startActivity(intent);
                Log.d("Toolbar", "Favourite is selected");
                break;
            case R.id.food_about:

                createCustomDialog();
                break;
        }
        return true;
    }

    /**
     * this method is used to create a dialog to show the string when you click on the specific place
     */
    private void createCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FoodInformation.this);
        builder.setMessage(getString(R.string.myname)+"\n"+getString(R.string.foodversion)+"\n"+getString(R.string.dialog1));

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
