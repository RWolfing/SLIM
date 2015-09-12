package slim.core.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Model for a event
 *
 * @author Robert Wolfinger
 */
@XmlRootElement(name = "event")
@XmlAccessorType(XmlAccessType.FIELD)
@DatabaseTable(tableName = "events")
public class Event {

    //Column names to query for values
    public static final String LOCATION_FIELD_NAME = "location_id";
    public static final String ORGANIZER_FIELD_NAME = "organizer_id";

    @XmlAttribute(name = "id")
    @DatabaseField(generatedId = true)
    private int mID;
    @XmlElement(name = "name")
    @DatabaseField
    private String mName;
    @XmlElement(name = "location")
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = LOCATION_FIELD_NAME)
    private Location mLocation;
    @XmlElement(name = "eventbegin")
    @DatabaseField
    private long mEventBegin;
    @XmlElement(name = "eventend")
    @DatabaseField
    private long mEventEnd;
    @XmlElement(name = "description")
    @DatabaseField
    private String mDescription;
    @XmlElement(name = "organizer")
    @DatabaseField(foreign = true, columnName = ORGANIZER_FIELD_NAME)
    private User mOrganizer;

    @XmlElement(name = "guests")
    private List<User> mGuests;

    public Event() {
        mGuests = new ArrayList<>();
    }

    public Event(String name, Location location, long eventBegin, long eventEnd, String description, User organizer) {
        mName = name;
        mLocation = location;
        mEventBegin = eventBegin;
        mEventEnd = eventEnd;
        mDescription = description;
        mGuests = new ArrayList();
        mOrganizer = organizer;
    }
    
    public void setmID(int id){
        mID = id;
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

    public User getmOrganizer() {
        return mOrganizer;
    }

    public void setGuests(List<User> guests) {
        mGuests.clear();
        mGuests.addAll(guests);
    }

    public void addGuest(User guest) {
        if (!mGuests.contains(guest)) {
            mGuests.add(guest);
        }
    }

    public void removeGuest(User guest) {
        mGuests.remove(guest);
    }

    public List<User> getGuests() {
        return mGuests;
    }

    @Override
    public String toString() {
        return "Event{" + "mID=" + mID + ", mName=" + mName + ", mLocation=" + mLocation + ", mEventBegin=" + mEventBegin + ", mEventEnd=" + mEventEnd + ", mDescription=" + mDescription + ", mOrganizer=" + mOrganizer + '}';
    }

    @Override
    public int hashCode() {
        return mID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return ((Event) obj).getmID() == mID;
    }
}
