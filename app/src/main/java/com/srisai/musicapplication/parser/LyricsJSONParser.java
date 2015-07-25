package com.srisai.musicapplication.parser;

import android.util.Log;

import com.srisai.musicapplication.HttpManager;
import com.srisai.musicapplication.model.Album;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lakshmi on 7/25/2015.
 * Class used to parse lyrics from WIKI JSON feed
 */
public class LyricsJSONParser {

    // Method gets the URL sting as parameter then fetch and parse the data
    public static String parseFeed(String url) {
        Log.d("LyricsParser", url);
        String content = HttpManager.getData(url);
        JSONObject jsonObj;

        try {
            jsonObj = new JSONObject(content);
            // JSONObject song = jsonObj.getJSONObject("song");

            return jsonObj.getString("lyrics");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("LyricsParser", "returning null");
            return null;
        }

    }
}
