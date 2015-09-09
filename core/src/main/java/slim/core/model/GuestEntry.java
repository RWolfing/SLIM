/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Robert
 */
@XmlRootElement(name = "guestlist")
@XmlAccessorType(XmlAccessType.FIELD)
@DatabaseTable(tableName = "guestlist")
public class GuestEntry {

    public final static String USER_ID_FIELD_NAME = "user_id";
    public final static String EVENT_ID_FIELD_NAME = "event_id";
     
    @DatabaseField(generatedId = true)
    private int mID;
    
    @DatabaseField(foreign = true, columnName = USER_ID_FIELD_NAME)
    private User mGuest;
    
    @DatabaseField(foreign = true, columnName = EVENT_ID_FIELD_NAME)
    private Event mEvent;
    
    public GuestEntry(){
        
    }
    
    public GuestEntry(Event event, User user){
        mEvent = event;
        mGuest = user;
    }

    public int getmID() {
        return mID;
    }

    public User getmGuest() {
        return mGuest;
    }

    public Event getmEvent() {
        return mEvent;
    }
}
