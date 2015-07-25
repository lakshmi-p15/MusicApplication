package com.srisai.musicapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.srisai.musicapplication.model.Album;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Lakshmi on 7/25/2015.
 * Custom adapter to hold data and inflate the list view with custom layout.
 * This class uses lazy loading and caching mechanisms for data display onto the device to avoid
 * memory problems.
 */
public class AlbumAdapter extends ArrayAdapter<Album> {

    private Context context;
    private  List<Album> albumList;

    public LruCache<String , Bitmap> imageCache;

    public AlbumAdapter(Context context, int resource, List<Album> objects) {
        super(context, resource,  objects);
        this.context = context;
        this.albumList = objects;

        //LRU cache initialization depending on available device memory
        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() /1024);
        final int cacheSize = maxMemory / 8;
        imageCache = new LruCache<>(cacheSize);
    }

    // setting up the each view and inflates it.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item, parent, false);

        //Display Album name in the TextView widget
       Album album = albumList.get(position);
        TextView title = (TextView) view.findViewById(R.id.album);
        title.setText(album.getAlbumName());

        //Display Track name in the TextView widget
        TextView track = (TextView) view.findViewById(R.id.title);
        track.setText(album.getTrackName());

        //Display Artist name in the TextView widget
        TextView artist = (TextView) view.findViewById(R.id.artist);
        artist.setText(album.getArtistName());

        //Display Album photo in ImageView widget
        Bitmap bitmap = imageCache.get(album.getTrackName());
        if (bitmap != null) {
            ImageView image = (ImageView) view.findViewById(R.id.list_image);
            image.setImageBitmap(album.getAlbumImage());
        }
        else {
            AlbumAndView container = new AlbumAndView();
            container.album = album;
            container.view = view;

            ImageLoader loader = new ImageLoader();
            loader.execute(container);
        }
        return view;
    }

    //Class to send and save the bitmap data from AsyncTask for each track
    class AlbumAndView {
        public Album album;
        public  View view;
        public Bitmap bitmap;
    }

    //AsyncTask class used to lazyload the image data and cache it.
    private class ImageLoader extends AsyncTask<AlbumAndView, Void , AlbumAndView> {

        @Override
        protected AlbumAndView doInBackground(AlbumAndView... params) {

           AlbumAndView container = params[0];
            Album album = container.album;

            try {
                String imageUrl =  album.getAlbumImageUrl();
                InputStream in = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                album.setAlbumImage(bitmap);
                in.close();
                container.bitmap = bitmap;
                return container;

                } catch (Exception e) {
                      e.printStackTrace();
                }
                 return null;
        }

        @Override
        protected void onPostExecute(AlbumAndView result) {
            // set the image from the result
            ImageView image = (ImageView) result.view.findViewById(R.id.list_image);
            image.setImageBitmap(result.bitmap);
           //    result.album.setAlbumImage(result.bitmap);
            imageCache.put(result.album.getTrackName(), result.bitmap);
        }
    }
}
