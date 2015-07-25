package com.srisai.musicapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.srisai.musicapplication.model.Album;
import com.srisai.musicapplication.parser.LyricsJSONParser;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Lakshmi on 7/25/2015.
 * This class diplays the detailed list view with artist name, track name,
 * album name, image ans lyrics of the track.
 * It uses LyricJSONParser to parse JSON data from WIKI API and diplays the lyrics.
 */
public class LiricDisplayActivity extends Activity{

    ImageView image;
    TextView albumName;
    TextView trackName;
    TextView artistName;
    TextView lirics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liric_display);

        // view initialization
        image = (ImageView) findViewById(R.id.image);
        albumName = (TextView) findViewById(R.id.album);
        trackName = (TextView) findViewById(R.id.title);
        artistName = (TextView) findViewById(R.id.artist);
        lirics = (TextView) findViewById(R.id.lirics);

        //get the intent from previous AlbumList class and set the textViews.
        Intent intent = getIntent();
        albumName.setText(intent.getExtras().getString("albumName"));
        trackName.setText(intent.getExtras().getString("trackName"));
        artistName.setText(intent.getExtras().getString("artistName"));

        // Display the image by calling the asynctask.
        ImgLoad imgLoad = new ImgLoad();
        imgLoad.execute(intent.getExtras().getString("albumImageUrl"));

        // call the asynctask to fetch the lyrics
        GetLyricTask lyricTask = new GetLyricTask();
        try {
            URI uri = new URI("http","//lyrics.wikia.com/api.php?artist="+
                    intent.getExtras().getString("artistName")+
                    "&song="+intent.getExtras().getString("trackName")+
                    "&fmt=realjson","");
             lyricTask.execute(uri.toString());
            } catch(URISyntaxException e) {
                 e.printStackTrace();
                 Log.d("LyricDisplayActivity", e.getReason());
             }

    }

    //Async task to get the Lyrics
     private class GetLyricTask extends AsyncTask<String, String , String >{

         @Override
         protected String doInBackground(String... params) {
             return LyricsJSONParser.parseFeed(params[0].toString());
         }

         @Override
         protected void onPostExecute(String s) {
            lirics.setText(s);
         }
     }

    // Async task to get the images
    private class ImgLoad extends  AsyncTask<String,String,Bitmap> {
        Bitmap bitmap;
        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                InputStream in = (InputStream) new URL(params[0]).getContent();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
                return bitmap;
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            image.setImageBitmap(bitmap);
        }
    }
}
