package com.njohn.phunware.homework.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by njohn on 10/3/16.
 */

public class Mission implements Parcelable {
    public final String image;
    public final String locationLine1;
    public final String locationLine2;
    public final String title;
    public final String description;
    public final String id;
    public final String dateString;
    public final String timestampString;

    public Date date = null;
    public Date timestamp = null;

    public Mission(String dateString, String description, String id, String image, String locationLine1, String locationLine2, String timestampString, String title)  {
        this.image = image;
        this.id = id;
        this.title = title;
        this.locationLine1 = locationLine1;
        this.locationLine2 = locationLine2;
        this.description = description;

        if (dateString != null) {
            this.dateString = dateString;
            this.date = getDate(dateString);
        } else {
            this.dateString = "";
        }

        if (timestampString != null) {
            this.timestampString = timestampString;
            this.timestamp = getDate(timestampString);
        } else {
            this.timestampString = "";
        }
    }

    private Date getDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // Parcelling part
    public Mission(Parcel in){
        String[] data = new String[8];

        in.readStringArray(data);
        this.dateString = data[0];
        if (this.dateString != null) {
            this.date = getDate(this.dateString);
        }
        this.description = data[1];
        this.id = data[2];
        this.image = data[3];
        this.locationLine1 = data[4];
        this.locationLine2 = data[5];
        this.timestampString = data[6];
        this.title = data[7];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.dateString,
                this.description,
                this.id,
                this.image,
                this.locationLine1,
                this.locationLine2,
                this.timestampString,
                this.title
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Mission createFromParcel(Parcel in) {
            return new Mission(in);
        }

        public Mission[] newArray(int size) {
            return new Mission[size];
        }
    };
}
