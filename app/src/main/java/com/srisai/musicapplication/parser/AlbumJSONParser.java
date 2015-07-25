package com.srisai.musicapplication.parser;

import android.util.Log;

import com.srisai.musicapplication.model.Album;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srisain on 7/24/2015.
 */
public class AlbumJSONParser {
    public static List<Album> parseFeed(String content) {

        try {
            JSONArray trackAr = null;

            List<Album> albumList = new ArrayList<>();
            JSONObject jsonObj = new JSONObject(content);

            trackAr = jsonObj.getJSONArray("results");


            for (int i = 0; i < trackAr.length(); i++) {

                JSONObject obj  = trackAr.getJSONObject(i);
                Album album = new Album();

                Log.d("AlbumParser", obj.getString("trackName"));

//                if(obj.getString("collectionName")== null) {
//                    album.setAlbumName("No Name");
//                } else {
//                    album.setAlbumName(obj.getString("collectionName"));
//
//                }

                album.setTrackName(obj.getString("trackName"));
                album.setArtistName(obj.getString("artistName"));
                album.setAlbumImageUrl(obj.getString("artworkUrl30"));
                albumList.add(album);
            }

            return albumList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("AlbumParser", "returning null");
            return null;

        }

    }
}
