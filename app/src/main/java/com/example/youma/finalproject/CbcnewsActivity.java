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
 * @author Kevin Tran
 *
 * Main activity in CBC News Reader
 */
public class CbcnewsActivity extends Activity {

    ArrayList<String> chatmessage = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cbcnews);

        Toolbar toolbar = (Toolbar) findViewById(R.id.news_toolbar);
        setActionBar(toolbar);
        final EditText searchedittext = (EditText) findViewById(R.id.new_search_edittext);
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

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.cbc_menu_toolbar, m);
        return true;

    }

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

    public Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CbcnewsActivity.this);
        LayoutInflater inflater = CbcnewsActivity.this.getLayoutInflater();
        final View inflated = inflater.inflate(R.layout.custom_layout2, null);
        builder.setView(inflated);
        return builder.create();
    }

    private class CbcnewsQuery extends AsyncTask<String, Integer, String> {
        private HttpURLConnection conn;
        private String newsTitle = "";
        private String newsLink = "";

        @Override
        protected String doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL("https://www.cbc.ca/cmlink/rss-world");
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
                        if (parser.getName().equalsIgnoreCase("rss")) {
                            set = true;
                        } else if (temp.equalsIgnoreCase("channel") && set) {
                            newsTitle = parser.getAttributeValue(null, "title");
                            publishProgress(25);
                            chatmessage.add(newsTitle);
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        private Context ctx;

        public ChatAdapter(ArrayList<String> list, Context ctx) {
            super(ctx, 0);
        }

        @Override
        public int getCount() {
            return chatmessage.size();
        }

        @Override
        public String getItem(int position) {
            return chatmessage.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



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
