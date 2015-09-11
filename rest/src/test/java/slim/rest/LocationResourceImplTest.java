/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import slim.core.model.Location;
import slim.rest.impl.LocationResourceImpl;

/**
 *
 * @author Robert
 */
public class LocationResourceImplTest extends RestBaseTest {

    private LocationResourceImpl mLocationResource;
    private UriInfo mUriInfo;

    @Before
    public void prepareResourcesTest() {
        mLocationResource = new LocationResourceImpl();
        mLocationResource.setmSlimService(mSlimService);
    }

    @Before
    public void mockUriInfo() {
        mUriInfo = mock(UriInfo.class);
        final UriBuilder fromResource = UriBuilder.fromResource(LocationResourceImpl.class);
        when(mUriInfo.getAbsolutePathBuilder()).thenAnswer((InvocationOnMock invocation) -> fromResource);
    }

    public void createLocationTest() {
        Response response = mLocationResource.createLocation(mUriInfo, "location", 50000, 50000);
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        Location location = (Location) response.getEntity();
        assertThat(location, notNullValue());
        response = mLocationResource.fetchLocationById(location.getID());
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        assertThat(location.getID(), is(((Location) response.getEntity()).getID()));
    }

    public void deleteLocationTest() {
        Response response = mLocationResource.deleteLocation(mTestLocation.getID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    public void fetchLocationByLongLat() {
        //Test found
        final long lattitude = mTestLocation.getLattitude();
        final long longitude = mTestLocation.getLongitude();
        Response response = mLocationResource.fetchLocationByLongLat(lattitude, longitude);
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        mLocationResource.deleteLocation(mTestLocation.getID());
        //test not found
        response = mLocationResource.fetchLocationByLongLat(lattitude, longitude);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    public void updateLocation() {
        Response response = mLocationResource.deleteLocation(mTestLocation.getID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

}
