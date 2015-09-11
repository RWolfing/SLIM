/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import java.sql.SQLException;
import java.util.List;
import org.junit.Test;
import slim.core.model.Location;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import slim.core.model.Event;

/**
 *
 * @author Robert
 */
public class LocationTest extends BaseTest {

    private final String mName = "auto-generated";
    private final long mLattitude = 123151231;
    private final long mLongitude = 123423512;

    @Test
    public void createLocation() {
        Location location = new Location(mName, mLattitude, mLongitude);
        Location createdLocation = mSlimDatabase.createLocation(location);
        assertThat(createdLocation, notNullValue());

        Location fetchedLocation = mSlimDatabase.getLocation(createdLocation.getID());
        assertThat(fetchedLocation, notNullValue());
        assertThat(fetchedLocation.getName(), equalTo(mName));
        assertThat(fetchedLocation.getLattitude(), is(mLattitude));
        assertThat(fetchedLocation.getLongitude(), is(mLongitude));
    }

    @Test
    public void getLocInBoundsTest() throws SQLException {
        mSlimDatabase.clearAllTables();

        Location locOutMinLat = new Location(mName, 99, 100);
        Location locOutMinLong = new Location(mName, 100, 99);

        Location locEdgeMin = new Location(mName, 100, 100);
        Location locInBounds = new Location(mName, 500, 500);
        Location locEdgeMax = new Location(mName, 1000, 1000);

        Location locOutMaxLat = new Location(mName, 1001, 500);
        Location locOutMaxLong = new Location(mName, 500, 1001);

        locOutMinLat = mSlimDatabase.createLocation(locOutMinLat);
        locOutMinLong = mSlimDatabase.createLocation(locOutMinLong);
        locEdgeMin = mSlimDatabase.createLocation(locEdgeMin);
        locInBounds = mSlimDatabase.createLocation(locInBounds);
        locEdgeMax = mSlimDatabase.createLocation(locEdgeMax);
        locOutMaxLat = mSlimDatabase.createLocation(locOutMaxLat);
        locOutMaxLong = mSlimDatabase.createLocation(locOutMaxLong);

        List<Location> result = mSlimDatabase.getLocationWithinBounds(100, 100, 1000, 1000);
        assertThat(result.contains(locEdgeMin), is(true));
        assertThat(result.contains(locInBounds), is(true));
        assertThat(result.contains(locEdgeMax), is(true));

        assertThat(result.contains(locOutMinLat), is(false));
        assertThat(result.contains(locOutMinLong), is(false));
        assertThat(result.contains(locOutMaxLat), is(false));
        assertThat(result.contains(locOutMaxLong), is(false));
    }
    
    @Test
    public void updateLocation(){
        Location location = new Location(mName, 900000, 100000);
        location = mSlimDatabase.createLocation(location);
        
        location.setName("updated-name");
        location.setLattitude(1);
        location.setLongitude(1);
        mSlimDatabase.saveLocation(location);
        
        location = mSlimDatabase.getLocation(location.getID());
        assertThat(location.getName(), equalTo("updated-name"));
        assertThat(location.getLattitude(), is(900000l));
        assertThat(location.getLongitude(), is(100000l));
        
    }

    @Test
    public void deleteLocation() {
        //Without event
        Location location = createRandomLocation();
        assertThat(location, notNullValue());

        boolean success = mSlimDatabase.deleteLocation(location.getID());
        assertThat(success, is(true));

        //With event
        Event event = createRandomEvent(null, null);
        success = mSlimDatabase.deleteLocation(event.getmLocation().getID());
        assertThat(success, is(false));
    }
}
