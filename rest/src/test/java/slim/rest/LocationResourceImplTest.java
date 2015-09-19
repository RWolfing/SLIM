package slim.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import slim.core.model.Location;
import slim.rest.impl.LocationResourceImpl;

/**
 * Class to test the functionality of the {@link LocationResource}
 * 
 * @author Robert Wolfinger
 */
public class LocationResourceImplTest extends RestBaseTest {

    private LocationResourceImpl mLocationResource;
    private UriInfo mUriInfo;

    @Before
    public void prepareResourcesTest() {
        mLocationResource = new LocationResourceImpl();
        mLocationResource.setSlimService(mSlimService);
    }

    @Before
    public void mockUriInfo() {
        mUriInfo = mock(UriInfo.class);
        final UriBuilder fromResource = UriBuilder.fromResource(LocationResourceImpl.class);
        when(mUriInfo.getAbsolutePathBuilder()).thenAnswer((InvocationOnMock invocation) -> fromResource);
    }

    @Test
    public void createLocationTest() {
        //Create location
        Response response = mLocationResource.createLocation(mUriInfo, "location", 50000, 50000);
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        //Check response
        Location location = (Location) response.getEntity();
        assertThat(location, notNullValue());
        
        //Check if location really exists
        response = mLocationResource.fetchLocationById(location.getID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(location.getID(), is(((Location) response.getEntity()).getID()));
    }
    
    @Test
    public void deleteLocationTest() {
        //Delete location & check response
        Response response = mLocationResource.deleteLocation(mTestLocation.getID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void fetchLocationByLongLat() {
        //Test found
        final long lattitude = mTestLocation.getLattitude();
        final long longitude = mTestLocation.getLongitude();
        Response response = mLocationResource.fetchLocationByLongLat(lattitude, longitude);
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        mLocationResource.deleteLocation(mTestLocation.getID());
        //Test not found
        response = mLocationResource.fetchLocationByLongLat(lattitude, longitude);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void updateLocation() {
        //Update location & check response
        Response response = mLocationResource.updateLocation(mTestLocation.getID(), "updated-location");
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

}
