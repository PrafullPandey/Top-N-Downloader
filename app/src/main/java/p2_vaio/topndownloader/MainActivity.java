package p2_vaio.topndownloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listView;
    private String feedurl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedlimit =10;
    private String feedCachedURL = "INVALIDATED";
    public static final String STATE_URL ="feedUrl";
    public static final String STATE_LIMIT = "feedlimit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.xmlListView);
        if(savedInstanceState!=null){
            feedlimit = savedInstanceState.getInt(STATE_LIMIT);
            feedurl=savedInstanceState.getString(STATE_URL);
        }
//        String top25moview = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml";

       /* Log.d(TAG, "onCreate: starting AsyncTask");
        DownloadData downloaddata = new DownloadData();
        downloaddata.execute(top25moview);
        Log.d(TAG, "onCreate: done");*/

       downloadURL(String.format(feedurl,feedlimit));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu , menu);
        if(feedlimit==10){
            menu.findItem(R.id.top10).setCheckable(true);
        }else{
            menu.findItem(R.id.top25).setCheckable(true);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       // String feedurl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
        switch(id){
            case R.id.freeapps:
                feedurl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
                break;
            case R.id.paidapps:
                feedurl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=%d/xml";
                break;
            case R.id.songs:
                feedurl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml";
                break;
            case R.id.movies:
                feedurl="http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topMovies/xml";
                break;
            case R.id.top10:
                break;
            case R.id.top25:
                if(!item.isChecked()){
                    item.setChecked(true);
                    feedlimit = 35-feedlimit;
                    Log.d(TAG, "onOptionsItemSelected: "+item.getTitle()+" setting feedlimit to "+feedlimit);
                }else{
                    Log.d(TAG, "onOptionsItemSelected: "+item.getTitle()+" feedlimit unchanged");
                }
                break;
            case R.id.refresh:
                feedCachedURL="INVALIDATED";
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        downloadURL(String.format(feedurl,feedlimit));
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_URL,feedurl);
        outState.putInt(STATE_LIMIT,feedlimit);
        super.onSaveInstanceState(outState);
    }


/*    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        feedurl=savedInstanceState.getString(STATE_URL);
        feedlimit=savedInstanceState.getInt(STATE_LIMIT);
    }*/

    private void downloadURL(String feedurl) {
        if(!feedurl.equalsIgnoreCase(feedCachedURL)) {

            Log.d(TAG, "downloadURL: starting AsyncTask");
            DownloadData downloaddata = new DownloadData();
            downloaddata.execute(feedurl);
            Log.d(TAG, "downloadURL: done");
            feedCachedURL=feedurl;
        }else
        {
            Log.d(TAG, "downloadURL: URL Not Changed");
        }
    }

    //<input type (url , for progress bar , return type of result)>
    private class DownloadData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null) {
                Log.e(TAG, "doInBackground: Error Downloading");
            }
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, "onPostExecute: "+s);
            Parsing parse = new Parsing();
            parse.parse(s);
           /* ArrayAdapter<FeedEntry> arrayadapter = new ArrayAdapter<FeedEntry>(
                    MainActivity.this ,R.layout.list_item,parse.getList());
            listView.setAdapter(arrayadapter);*/
            FeedAdapter<FeedEntry> feedAdapter = new FeedAdapter<FeedEntry>(
                    MainActivity.this, R.layout.list_record, parse.getList()
            );
            listView.setAdapter(feedAdapter);

        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The response code is : " + response);
//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader reader = new BufferedReader(inputStreamReader);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                int charsRead;
                char inputBuffer[] = new char[500];
                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();
                return xmlResult.toString();

            } catch (MalformedURLException e) {
                Log.d(TAG, "downloadXML:Invalid Url" + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "downloadXML: IO Exception reading data" + e.getMessage());
            } catch (SecurityException e) {
                Log.d(TAG, "downloadXML: Security exception " + e.getMessage());
//                e.printStackTrace();
            }
            return null;
        }
    }
}
