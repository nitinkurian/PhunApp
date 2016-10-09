package com.njohn.phunware.homework;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.njohn.phunware.homework.model.Mission;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by njohn on 10/8/16.
 */
/**
 * The detail activity showing the mission detail.
 */
public class MissionDetailActivity extends AppCompatActivity {
    /**
     * The Mission object for this activity
     */
    private Mission mission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mission_detail);

        // Load the mission object from the intent
        mission = getIntent().getExtras().getParcelable(MainActivity.EXTRA_MISSION_DATA);

        // Set the toolbar as action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(mission.title);
        setSupportActionBar(toolbar);
        // Show the up button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the description text
        TextView textView = (TextView) findViewById(R.id.main_description);
        textView.setText(mission.description);

        // Set the date text using custom date format
        if (mission.date != null) {
            TextView dateTextView = (TextView) findViewById(R.id.main_date);
            DateFormat df = new SimpleDateFormat("MMM d, yy 'at' h:mm aaa");
            df.setTimeZone(TimeZone.getDefault());
            dateTextView.setText(df.format(mission.date));
        }

        ImageView imageView = (ImageView) findViewById(R.id.main_backdrop);
        new LoadImageTask(imageView).execute(mission.image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_phone) {
            makePhoneCall();
        } else if (item.getItemId() == R.id.menu_share) {
            shareMission();
        }
        return super.onOptionsItemSelected(item);
    }

    // Allow user to share the mission data using native bottom share sheet
    public void shareMission() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mission.title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mission.description);
        shareIntent.setType("text/plain");

        // display apps that can share plain text
        startActivity(shareIntent);
    }

    // Make a call
    public void makePhoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        try {
            startActivity(intent);
        }
        catch (Exception e) {
            Snackbar.make(findViewById(R.id.main_coordinatorLayout), R.string.feature_unavailable, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * Create a new AsyncTask. This will load an image associated to a mission.
     */
    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        private int position;

        // Store ImageView on which to set the image bitmap
        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection httpURLConnection = null;

            try {
                // Create url for image
                URL url = new URL(params[0]);

                // open an httpurlconnection, get its input stream
                // and download the image
                httpURLConnection = (HttpURLConnection)url.openConnection();
                try {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            imageView.setImageBitmap(bitmap);
        }
    }
}
