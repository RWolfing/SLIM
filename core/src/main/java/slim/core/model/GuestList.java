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

/**
 *
 * @author Robert
 */
@DatabaseTable(tableName = "guestlist")
public class GuestList {

    @DatabaseField(generatedId = true)
    private int mID;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<User> mGuests;
    
    public GuestList(){
        
    }
}
