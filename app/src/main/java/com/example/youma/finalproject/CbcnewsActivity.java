package com.example.youma.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Kevin Tran, Gia Nguyen
 *
 * Main activity in CBC News Reader
 */
public class CbcnewsActivity extends Activity {

    protected  final static String uml="https://www.cbc.ca/cmlink/rss-world";

    ArrayList<String> cbcnewsmessage = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcnews);

        Toolbar toolbar = (Toolbar) findViewById(R.id.news_toolbar);
        setActionBar(toolbar);
        final EditText searchedittext = (EditText) findViewById(R.id.new_search_edittext);
        final ListView newslistview = (ListView)findViewById(R.id.news_listview);
        final Button searchnews = (Button) findViewById(R.id.searchnews);
        //Search for news
        searchnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String search = searchedittext.getText().toString();
                Snackbar.make(searchnews, R.string.searching, Snackbar.LENGTH_LONG).show();
            }
        });

        new CbcnewsQuery().execute();
    }

    /**
     * return true about the toolbar
     * @param m
     * @return true
     */
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.cbc_menu_toolbar, m);
        return true;

    }

    /**
     * to make the selection of the menu items from the toolbar
     * @param mi
     * @return true
     */
    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.help:
                Toast toast = Toast.makeText(CbcnewsActivity.this, "reading news", Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.about:
                Dialog alertDialog = createDialog();
                alertDialog.show();
                Toast.makeText(CbcnewsActivity.this, "Version 1.0, by Kiet Tran", Toast.LENGTH_LONG).show();
                break;

        }
        return true;
    }


    /**
     *create the dialog
     * @return create of object Builder
     */
    public Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CbcnewsActivity.this);
        LayoutInflater inflater = CbcnewsActivity.this.getLayoutInflater();
        final View inflated = inflater.inflate(R.layout.custom_layout2, null);
        builder.setView(inflated);
        return builder.create();
    }

    /**
     * using AsyncTask to read the imformation from the website
     */
    private class CbcnewsQuery extends AsyncTask<String, Integer, String> {
        private HttpURLConnection conn;
        private String newsTitle = "";
        private String newsLink = "";

        /**
         * this is the String that read from the website
         * @param strings
         * @return null
         */
        @Override
        protected String doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(uml);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                inputStream = conn.getInputStream();

                //create pullParser
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);

                /*XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);*/
                int eventType = parser.getEventType();
               boolean set = false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        String temp = parser.getName();
                        if (temp.equalsIgnoreCase("rss")) {
                            set = true;
                        } else if (temp.equalsIgnoreCase("channel") && set) {
                            newsTitle = parser.getAttributeValue(null, "title");
                            publishProgress(25);
                            cbcnewsmessage.add(newsTitle);
                            Log.i("tiltle","link");
                            newsLink = parser.getAttributeValue(null, "link");
                            publishProgress(50);

                        }


                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {

            }
            return null;
        }

        /**
         * execute the String of data
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    /**
     * create the ArrayAdapter to contain the string to put into the list view
     */
    private class ChatAdapter extends ArrayAdapter<String> {
        private Context ctx;

        /**
         * there is ArrayList type String , the object of Context
         * @param list
         * @param ctx
         */
        public ChatAdapter(ArrayList<String> list, Context ctx) {
            super(ctx, 0);
        }

        /**
         * this will have the size of the ArrayList
         * @return size
         */
        @Override
        public int getCount() {
            return cbcnewsmessage.size();
        }

        /**
         * this will get the position of the item that is in the ArrayList using String
         * @param position
         * @return position
         */
        @Override
        public String getItem(int position) {
            return cbcnewsmessage.get(position);
        }

        /**
         *this will get the position of the item that is in the ArrayList using long
         * @param position
         * @return position
         */
        @Override
        public long getItemId(int position) {
            return position;
        }


        /**
         *this get the view and put into the listview
         * @param position
         * @param convertView
         * @param parent
         * @return object of View
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = CbcnewsActivity.this.getLayoutInflater();

            View result = inflater.inflate(R.layout.newslistview, null);

            TextView message = (TextView) result.findViewById(R.id.newstextview);
            message.setText(getItem(position));
            Log.i("test","test");
            return result;
        }
    }
}
