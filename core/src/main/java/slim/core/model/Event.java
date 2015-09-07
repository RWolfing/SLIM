/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 *
 * @author Robert
 */
@Entity
public class Event {
    @Column(name = "id")
    private int mID;
    @Column(name = "name")
    private String mName;
    @Column(name = "location")
    private Location mLocation;
    @Column(name = "event_begin")
    private String mEventBegin;
    @Column(name = "event_end")
    private String mEventEnd;
    @Column(name = "description")
    private String mDescription;
    private List<User> mGuestList;
    @Column(name = "organizer")
    private User mOrganizer;
    
    public Event(){
    }
    
    public Event(String name, Location location, String eventBegin, String eventEnd, String description, List<User> guestList, User organizer){
        mName = name;
        mLocation = location;
        mEventBegin = eventBegin;
        mEventEnd = eventEnd;
        mDescription = description;
        mGuestList = guestList;
        mOrganizer = organizer;
    }

    /**
     * @return the mID
     */
    public int getmID() {
        return mID;
    }

    /**
     * @return the mName
     */
    public String getmName() {
        return mName;
    }

    /**
     * @return the mLocation
     */
    public Location getmLocation() {
        return mLocation;
    }

    /**
     * @return the mEventBegin
     */
    public String getmEventBegin() {
        return mEventBegin;
    }

    /**
     * @return the mEventEnd
     */
    public String getmEventEnd() {
        return mEventEnd;
    }

    /**
     * @return the mDescription
     */
    public String getmDescription() {
        return mDescription;
    }

    /**
     * @return the mGuestList
     */
    public List<User> getmGuestList() {
        return mGuestList;
    }

    /**
     * @return the mOrganizer
     */
    public User getmOrganizer() {
        return mOrganizer;
    }
}
