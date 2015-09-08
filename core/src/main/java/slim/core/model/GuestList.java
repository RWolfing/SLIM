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

/**
 *
 * @author Robert
 */
@XmlRootElement(name = "guestlist")
@XmlAccessorType(XmlAccessType.FIELD)
@DatabaseTable(tableName = "guestlist")
public class GuestList {

    @XmlAttribute(name = "id")
    @DatabaseField(generatedId = true)
    private int mID;

    @XmlElement(name = "name")
    @ForeignCollectionField(eager = false)
    private ForeignCollection<User> mGuests;
    
    public GuestList(){
        
    }
    
    public int getmID(){
        return mID;
    }
    
    public ForeignCollection getmGuests(){
        return  mGuests;
    }
}
