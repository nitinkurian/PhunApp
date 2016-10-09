package com.njohn.phunware.homework;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.njohn.phunware.homework.adapter.MissionsAdapter;
import com.njohn.phunware.homework.model.Mission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The main activity showing all the missions. On a small device, this appears as a list.
 * On tablet-size devices, missions are presented as a grid
 * <p>
 * Note: This activity and this application as a whole does not make  use of fragments.
 * Each mission is presented as an item in a grid. Selecting any of the item is taken to
 * the corresponding detail page {@link com.njohn.phunware.homework.MissionDetailActivity}.
 */
public class MainActivity extends AppCompatActivity {
    static final String MISSIONS = "missions";
    private ArrayList<Mission> missionList = new ArrayList<>();

    private MissionsAdapter missionsAdapter;
    private DataLoader getMissionDataTask;

    public final static String EXTRA_MISSION_DATA = "com.njohn.phunware.homework.mission.data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            // Restore state members from saved instance
            missionList = savedInstanceState.getParcelableArrayList(MISSIONS);

            // Iterate through missionList and create a drawable
            // from the bitmap object associated to mission image url
            HashMap<String, Drawable> images = new HashMap<>();
            for (Mission mission : missionList) {
                if (mission.image != null && mission.image != "null") {
                    Bitmap bitmap = savedInstanceState.getParcelable(mission.image);
                    if (bitmap != null) {
                        RoundedBitmapDrawable drawable = BitmapHelpers.createRoundedBitmapDrawable(getResources(), bitmap);;
                        images.put(mission.image, drawable);
                    }
                }
            }

            // Initialize an adapter for the view using the marshalled data
            missionsAdapter = new MissionsAdapter(this, missionList, images);

        } else {
            getMissionDataTask = new DataLoader();

            String feed = "https://raw.githubusercontent.com/phunware/dev-interview-homework/master/feed.json";
            try {
                URL url = new URL(feed);
                getMissionDataTask.execute(new URL(feed));
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            missionsAdapter = new MissionsAdapter(this, missionList);
        }

        // Default Actionbars are disabled
        // Add toolbar as actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the adapater for gridview.
        // On selection of an item in the grid view, navigate to detail view
        GridView gridView = (GridView) findViewById(R.id.grid);
        gridView.setAdapter(missionsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the GridView selected/clicked item text
                Mission selectedItem = (Mission) parent.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, MissionDetailActivity.class);
                intent.putExtra(MainActivity.EXTRA_MISSION_DATA, (Parcelable) selectedItem);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MISSIONS, missionList);

        ArrayList<Bitmap> images = new ArrayList<>();
        for (Mission mission : missionList) {
            if (mission.image != "null" && mission.image != null) {
                RoundedBitmapDrawable drawable = (RoundedBitmapDrawable)missionsAdapter.getBitmaps().get(mission.image);
                if (drawable != null) {
                    outState.putParcelable(mission.image, drawable.getBitmap());
                }
            }
        }

        super.onSaveInstanceState(outState);
    }

    // Allow user to share the mission data using native bottom share sheet
    public void shareMission(View view) {
        View parentRow = (View) view.getParent();
        CardView cardView = (CardView) parentRow.getParent().getParent();
        GridView gridView = (GridView)cardView.getParent().getParent();
        final int position = gridView.getPositionForView(cardView);

        Mission mission = missionList.get(position);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mission.title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mission.description);
        shareIntent.setType("text/plain");

        // display apps that can share plain text
        startActivity(shareIntent);
    }

    /**
     * A DataLoader AsyncTask for this Activity
     * On Activity creation an instance of this AsyncTask is created and data is fetched from network.
     */
    private class DataLoader extends AsyncTask<URL, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(URL... params) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection)params[0].openConnection();

                // If the connection is successful, parse the response
                int response = connection.getResponseCode();
                if (response == HttpURLConnection.HTTP_OK) {
                    StringBuilder builder = new StringBuilder();

                    // Cannot do try with resource for API 17
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    try {
                        String line;

                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }
                        return new JSONArray(builder.toString());
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        if (reader != null) {
                            reader.close();
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray missions) {
            // populate data
            convertJSONtoArrayList(missions);

            // notify the list view of teh data update
            missionsAdapter.notifyDataSetChanged();
        }

        private void convertJSONtoArrayList(JSONArray missions) {
            //Clear any mission data if it exists
            missionList.clear();

            try {
                for (int i = 0; i < missions.length(); i++) {
                    // Get a mission object
                    JSONObject mission = missions.getJSONObject(i);

                    // Get mission properties
                    String date = mission.getString("date");
                    String description = mission.getString("description");
                    String id = mission.getString("id");
                    String image = mission.getString("image");
                    String locationLine1 = mission.getString("locationline1");
                    String locationLine2 = mission.getString("locationline2");
                    String timestampString = mission.getString("timestamp");
                    String title = mission.getString("title");

                    // Add to Mission List
                    try {
                        Mission missionObject = new Mission(date, description, id, image, locationLine1, locationLine2, timestampString, title);
                        missionList.add(missionObject);
                    } catch (Exception e) {

                    }
                }
            }
            catch (JSONException e) {

            }

        }
    }
}
