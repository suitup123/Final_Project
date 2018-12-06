package com.example.youma.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

public class ToolbarActivities extends Activity {

    /**
     * @author Nguyen Gia Nguyen
     *
     *Description: Other Activities button when clicked on will bring to a layout from which user can choose other activity.
     * @param: savedInstanceState
     */

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_activities);

         Toolbar toolbar = (Toolbar)findViewById(R.id.movie_toolbar);
        setActionBar(toolbar);
    }

    /**
     *
     * @param m
     * @return true
     */
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    /**
     * Description: Toolbar with options for user to change to other activity.
     * @param mi
     * @return true
     */
    public boolean onOptionsItemSelected(MenuItem mi){
        int id = mi.getItemId();

        switch(id) {
            case R.id.action_one:
                Log.d("Toolbar", "Option 1 selected");
                Intent intent = new Intent(ToolbarActivities.this, MovieInformation.class);
                startActivity(intent);
                break;

//            case R.id.action_two:
//                Log.d("Toolbar", "Option 2 selected");
//                Intent intent2 = new Intent(ToolbarActivities.this, FoodInformation.class);
//                startActivity(intent2);
//
            case R.id.action_three:
                Log.d("Toolbar", "Option 3 selected");
                Intent intent3 = new Intent(ToolbarActivities.this, CbcnewsActivity.class);
                startActivity(intent3);

            case R.id.action_four:
                Log.d("Toolbar", "Option 4 selected");
                Dialog alertDialog = createDialog();
                alertDialog.show();
                break;

        }
        return true;
    }

    /**
     * Description: custom dialog.
     * @return builder.create();
     */
    public Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ToolbarActivities.this);
        LayoutInflater inflater = ToolbarActivities.this.getLayoutInflater();
        final View inflated = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(inflated);
        return builder.create();
    }

}
