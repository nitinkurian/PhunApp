package com.njohn.phunware.homework.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.njohn.phunware.homework.BitmapHelpers;
import com.njohn.phunware.homework.R;
import com.njohn.phunware.homework.model.Mission;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by njohn on 10/4/16.
 */
public class MissionsAdapter extends ArrayAdapter<Mission> {
    private HashMap<String, Drawable> mBitmaps;
    private Resources mResources;

    public HashMap<String, Drawable> getBitmaps() {
        return mBitmaps;
    }

    public MissionsAdapter(Context context, List<Mission> missions) {
        this(context, missions, new HashMap<String, Drawable>());
    }

    public MissionsAdapter(Context context, List<Mission> missions, HashMap<String, Drawable> bitmaps) {
        super(context, -1, missions);
        mResources = context.getResources();
        mBitmaps = bitmaps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get mission object for this specified position
        Mission mission = getItem(position);

        // Object that references the Grid items views
        ViewHolder viewHolder;

        // Check for a reusable ViewHolder from a GridView item that scrolled
        // offscreen else create a new ViewHolder
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.missionImageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.title);
            viewHolder.locationView = (TextView) convertView.findViewById(R.id.location);
            viewHolder.descriptionView = (TextView) convertView.findViewById(R.id.description);
            viewHolder.position = position;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleView.setText(mission.title);
        viewHolder.locationView.setText(mission.locationLine1 + ", " + mission.locationLine2);
        viewHolder.descriptionView.setText(mission.description);

        // If mission image already downloaded use it, else download
        if (mission.image != null && mission.image != "null") {
            if (mBitmaps.containsKey(mission.image)) {
                viewHolder.missionImageView.setImageDrawable(mBitmaps.get(mission.image));
            } else {
                new LoadImageTask(viewHolder).execute(mission.image);
            }
        } else {
            // If no image is avaiable set to default image
            viewHolder.missionImageView.setImageDrawable(mResources.getDrawable(R.drawable.placeholder_nomoon));
        }
        return convertView;
    }

    /**
     * A View Holder class to cache views for each grid item
     */
    private static class ViewHolder {
        ImageView missionImageView;
        TextView titleView;
        TextView descriptionView;
        TextView locationView;
        int position;
    }

    /**
     * A Image Loader AsyncTask for this Activity
     * On Activity creation an instance of this AsyncTask is created and image is fetched from network.
     */
    private class LoadImageTask extends AsyncTask<String, Void, Drawable> {
        private ViewHolder viewHolder;
        private int position;

        // Store ImageView on which to set the image bitmap
        public LoadImageTask(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
            this.position = viewHolder.position;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            RoundedBitmapDrawable drawable = null;
            HttpURLConnection httpURLConnection = null;

            try {
                // Create url for image
                URL url = new URL(params[0]);

                // open an httpurlconnection, get its input stream and download the image
                httpURLConnection = (HttpURLConnection)url.openConnection();
                try {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    // Cache for later use
                    if (bitmap != null) {
                        // Convert the ImageView image to a rounded corners image with border
                        drawable = BitmapHelpers.createRoundedBitmapDrawable(mResources, bitmap);

                        // Set the ImageView image as drawable object
                        mBitmaps.put(params[0], drawable);
                    }
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

            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);

            if (viewHolder.position == this.position) {
                if (drawable != null) {
                    viewHolder.missionImageView.setVisibility(View.VISIBLE);
                    viewHolder.missionImageView.setImageDrawable(drawable);
                } else {
                    viewHolder.missionImageView.setImageDrawable(mResources.getDrawable(R.drawable.placeholder_nomoon));
                }

            }
        }
    }
}
