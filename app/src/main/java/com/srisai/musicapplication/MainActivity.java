package com.srisai.musicapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Lakshmi on 7/25/2015.
 * MainActivity is a starting Activity class in which
 * User can enter the search string to get the list of album details
 */
public class MainActivity extends ActionBarActivity {

// Variables to enter text and simple search button for Main Activity
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // attaching the XML layout file to the screen
        setContentView(R.layout.activity_main);

        // initializing the views
        editText = (EditText) findViewById(R.id.edit_query);
        button = (Button) findViewById(R.id.button_search);

        //read the search string when button clicked

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query= editText.getText().toString();
                // checking the network availability
                if(isOnline()) {
                    //Starting the new Activity to display the result set with the given query string
                    Intent intent = new Intent(MainActivity.this, AlbumList.class);
                    intent.putExtra("user_query", query);
                    startActivity(intent);
                   // Toast.makeText(MainActivity.this,"Network is available",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,"Network not available",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //service call to check the network
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
