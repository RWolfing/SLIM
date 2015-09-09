/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Robert
 */
@XmlRootElement(name = "location")
@XmlAccessorType(XmlAccessType.FIELD)
@DatabaseTable(tableName = "locations")
public class Location {

    public final static String LATTITUDE_FIELD_NAME = "lattitude";
    public final static String LONGITUDE_FIELD_NAME = "longitude";

    @XmlAttribute(name = "id")
    @DatabaseField(generatedId = true)
    private int mID;

    @XmlTransient
    @ForeignCollectionField
    private ForeignCollection<Event> mEvents;

    @XmlElement(name = "lattitude")
    @DatabaseField(columnName = LATTITUDE_FIELD_NAME)
    private long mLattitude;

    @XmlElement(name = "longitude")
    @DatabaseField(columnName = LONGITUDE_FIELD_NAME)
    private long mLongitude;

    public Location() {

    }

    public Location(long lattitude, long longitude) {
        mLattitude = lattitude;
        mLongitude = longitude;
    }

    public int getmID() {
        return mID;
    }

    public long getmLattitude() {
        return mLattitude;
    }

    public void setmLattitude(long mLattitude) {
        this.mLattitude = mLattitude;
    }

    public long getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(long mLongitude) {
        this.mLongitude = mLongitude;
    }

    public ForeignCollection<Event> getmEvents() {
        return mEvents;
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
        return ((Location) obj).getmID() == mID;
    }
}
