package com.example.habittracker;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

// Here we use Parcelable interface to achieve passing event object between activities
public class HabitEvent implements Parcelable {
    private String eventTitle;
    private String comment;
    private String location;
    private String imageName;


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

    public HabitEvent(String title, String comment, String location, String imageName) {
        this.eventTitle = title +  "-" + System.currentTimeMillis(); // create unique event name
        this.comment = comment;
        this.location = location;
        this.imageName = imageName;
    }

    public HabitEvent(Parcel in) {
        this.eventTitle = in.readString();
        this.comment = in.readString();
        this.location = in.readString();
        this.imageName = in.readString();
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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
        parcel.writeString(this.imageName);
    }
}
