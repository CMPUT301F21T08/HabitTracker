package com.example.habittracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;


// Here we use Parcelable interface to achieve passing event object between activities
public class HabitEvent implements Parcelable {
    private String eventTitle;
    private String comment;
    private String location;
    private String imageFilePath;


    public static final Parcelable.Creator<HabitEvent> CREATOR = new Parcelable.Creator<HabitEvent>() {

        @Override
        public HabitEvent createFromParcel(Parcel parcel) {
            return new HabitEvent(parcel);
        }

        @Override
        public HabitEvent[] newArray(int i) {
            return new HabitEvent[i];
        }
    };

    public HabitEvent(String habitName, String comment, String location, String imageFilePath) {
        String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        this.eventTitle = habitName +": "+ date; // create unique event name
        this.comment = comment;
        this.location = location;
        this.imageFilePath = imageFilePath;
    }

    public HabitEvent(Parcel in) {
        this.eventTitle = in.readString();
        this.comment = in.readString();
        this.location = in.readString();
        this.imageFilePath = in.readString();
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.eventTitle);
        parcel.writeString(this.comment);
        parcel.writeString(this.location);
        parcel.writeString(this.imageFilePath);
    }
}
