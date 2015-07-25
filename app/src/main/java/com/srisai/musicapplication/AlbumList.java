package com.srisai.musicapplication;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.srisai.musicapplication.model.Album;
import com.srisai.musicapplication.parser.AlbumJSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lakshmi on 7/25/2015.
 * AlbumList is a listActivity class which calls the AsynTask to make network calls and
 * fetch the fetch the JSON data by using given URL and query from previous Activity.
 */
public class AlbumList extends ListActivity {

    //Actual base URL based on appleAPI of music tracks
    public static final String ALBUM_BASE_URL ="https://itunes.apple.com/search?term=";

    // Views declaration
    TextView tx;
    ProgressBar pb;
    ListView listView;
    List<Album> albumList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_albums);

        // Progressbar initialization and visibility setting
        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.INVISIBLE);


        String query = null;

        //read the intent to get the search string
        Intent intent = getIntent();
        query =intent.getExtras().getString("user_query");

        //Method call to get data through webservice
        requestData(ALBUM_BASE_URL+query);
    }

    //Asynctask call by sending uri
    private void requestData(String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    protected void updateDisplay() {
        //Use AlbumAdapter to display data
        AlbumAdapter adapter = new AlbumAdapter(this, R.layout.list_item, albumList);
        setListAdapter(adapter);
    }

    // Passing an intent to new detailed Activity to display selected album details
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Album album = albumList.get(position);

        Intent intent = new Intent(AlbumList.this, LiricDisplayActivity.class);
        intent.putExtra("albumName" , album.getAlbumName());
        intent.putExtra("trackName", album.getTrackName());
        intent.putExtra("artistName", album.getArtistName());
        intent.putExtra("albumImageUrl", album.getAlbumImageUrl());
        startActivity(intent);
    }

    private class MyTask extends AsyncTask<String, String, List<Album>> {

        @Override
        protected void onPreExecute() {
            //progressbar must be visible when task is going to start
                pb.setVisibility(View.VISIBLE);
        }

        //It does the task execution in background to fetch data
        @Override
        protected List<Album> doInBackground(String... params) {

            String content = HttpManager.getData(params[0]);
            albumList = AlbumJSONParser.parseFeed(content);

            return albumList;
        }

        @Override
        protected void onPostExecute(List<Album> result) {

                pb.setVisibility(View.INVISIBLE);

            if (result == null) {
                Toast.makeText(AlbumList.this, "Web service not available", Toast.LENGTH_LONG).show();
                return;
            }

           albumList = result;
            //update list with new data items
            updateDisplay();

        }

    }

}
