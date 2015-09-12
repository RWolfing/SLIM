package slim.core.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Model for a location
 *
 * @author Robert Wolfinger
 */
@XmlRootElement(name = "location")
@XmlAccessorType(XmlAccessType.FIELD)
@DatabaseTable(tableName = "locations")
public class Location {

    //Column names need to access the values
    public final static String LATTITUDE_FIELD_NAME = "lattitude";
    public final static String LONGITUDE_FIELD_NAME = "longitude";
    public final static String NAME_FIELD_NAME = "name";

    @XmlAttribute(name = "id")
    @DatabaseField(generatedId = true)
    private int mID;

    @XmlTransient
    @ForeignCollectionField
    private ForeignCollection<Event> mEvents;

    @XmlElement(name = "name")
    @DatabaseField(columnName = NAME_FIELD_NAME)
    private String mName;

    @XmlElement(name = "lattitude")
    @DatabaseField(columnName = LATTITUDE_FIELD_NAME)
    private long mLattitude;

    @XmlElement(name = "longitude")
    @DatabaseField(columnName = LONGITUDE_FIELD_NAME)
    private long mLongitude;

    public Location() {

    }

    public Location(String name, long lattitude, long longitude) {
        mName = name;
        mLattitude = lattitude;
        mLongitude = longitude;
    }

    public void setID(int id) {
        mID = id;
    }

    public int getID() {
        return mID;
    }

    public ForeignCollection<Event> getEvents() {
        return mEvents;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public long getLattitude() {
        return mLattitude;
    }

    public void setLattitude(long lattitude) {
        this.mLattitude = lattitude;
    }

    public long getLongitude() {
        return mLongitude;
    }

    public void setLongitude(long longitude) {
        this.mLongitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" + "mID=" + mID + ", mEvents=" + mEvents + ", mName=" + mName + ", mLattitude=" + mLattitude + ", mLongitude=" + mLongitude + '}';
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
        return ((Location) obj).getID() == mID;
    }
}
