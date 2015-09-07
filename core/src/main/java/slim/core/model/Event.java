/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author Robert
 */
@DatabaseTable(tableName = "events")
public class Event {

    @DatabaseField(generatedId = true)
    private int mID;
    @DatabaseField
    private String mName;
    @DatabaseField(foreign = true)
    private Location mLocation;
    @DatabaseField
    private long mEventBegin;
    @DatabaseField
    private long mEventEnd;
    @DatabaseField
    private String mDescription;
    @DatabaseField(foreign = true)
    private GuestList mGuestList;
    @DatabaseField(foreign = true)
    private User mOrganizer;

    public Event() {
    }

    public Event(String name, Location location, long eventBegin, long eventEnd, String description, User organizer) {
        mName = name;
        mLocation = location;
        mEventBegin = eventBegin;
        mEventEnd = eventEnd;
        mDescription = description;
        mGuestList = new GuestList();
        mOrganizer = organizer;
    }

    public int getmID() {
        return mID;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public Location getmLocation() {
        return mLocation;
    }

    public long getmEventBegin() {
        return mEventBegin;
    }

    public void setmEventBegin(long mEventBegin) {
        this.mEventBegin = mEventBegin;
    }

    public long getmEventEnd() {
        return mEventEnd;
    }

    public void setmEventEnd(long mEventEnd) {
        this.mEventEnd = mEventEnd;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public GuestList getmGuestList() {
        return mGuestList;
    }

    public User getmOrganizer() {
        return mOrganizer;
    }    
}
