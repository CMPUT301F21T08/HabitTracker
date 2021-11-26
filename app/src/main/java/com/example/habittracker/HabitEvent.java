package com.example.habittracker;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The habit event class
 * Here we use Parcelable interface to achieve passing event object between activities
 */
public class HabitEvent implements Parcelable {
    private String eventTitle;
    private String comment;
    private String location;
    private String downloadUrl; // This stores the url directing to the photo stored in firebase, not set by any constructor
    private String uuid;
    private String localImagePath;
    private String habitName;
    private String habitUUID;

    /**
     * this creates a parcel creator, which is used to pass data between events
     */
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

    /**
     * Habit Event Constructor
     * @param habitName name of the related habit
     * @param comment comment for the habit event
     * @param location location for the habit event
     * @param uuid unique id for each event, generated on the habit side
     */
    public HabitEvent(String habitName, String comment, String location, String uuid, String habitUUID) {
        String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        this.habitName = habitName;
        this.eventTitle = habitName +": "+ date; // create unique event name
        this.comment = comment;
        this.location = location;
        this.downloadUrl = null;
        this.localImagePath = null;
        this.uuid = uuid;
        this.habitUUID = habitUUID;
    }

    /**
     * Parcel procesisng function
     * @param in
     */
    public HabitEvent(Parcel in) {
        this.eventTitle = in.readString();
        this.comment = in.readString();
        this.location = in.readString();
        this.downloadUrl = in.readString();
        this.uuid = in.readString();
        this.habitName = in.readString();
        this.localImagePath = in.readString();
        this.habitUUID = in.readString();
    }

    /**
     * Null constructor (never used)
     */
    public HabitEvent(){
    }

    /**
     * Getter for event title
     * @return
     */
    public String getEventTitle() {
        return eventTitle;
    }

    /**
     * Setter for event title
     * @param eventTitle
     */
    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    /**
     * Getter for comment
     * @return
     */
    public String getComment() {
        return comment;
    }

    /**
     * Setter for comment
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Getter for location
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for url
     * @return
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * Setter for url
     * @param downloadUrl
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * Getter for uuid
     * @return
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Setter for uuid
     * @param uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Getter for local image path
     * @return
     */
    public String getLocalImagePath() {
        return localImagePath;
    }

    /**
     * Setter for local image path
     * @param localImagePath
     */
    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
    }

    /**
     * Getter for habit name
     * @return
     */
    public String getHabitName() {
        return habitName;
    }

    /**
     * Setter for habit name
     * @param habitName
     */
    public void setHabitName(String habitName) {

        this.habitName = habitName;
        int dateStartIndex = eventTitle.indexOf(":");
        String date = eventTitle.substring(dateStartIndex+2);
        this.eventTitle = this.habitName+ ": " +date;
    }

    /**
     * Getter for habit uuid
     * @return
     */
    public String getHabitUUID() {
        return habitUUID;
    }

    /**
     * Setter for habit uuid
     * @param habitUUID
     */
    public void setHabitUUID(String habitUUID) {
        this.habitUUID = habitUUID;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcel processing functions
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.eventTitle);
        parcel.writeString(this.comment);
        parcel.writeString(this.location);
        parcel.writeString(this.downloadUrl);
        parcel.writeString(this.uuid);
        parcel.writeString(this.habitName);
        parcel.writeString(this.localImagePath);
        parcel.writeString(this.habitUUID);
    }
}
