package slim.rest.impl;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import slim.core.model.Location;
import slim.rest.LocationResource;

/**
 * Implementation of the {@link LocationResource}
 * 
 * @author Robert
 */
@Path("locations")
public class LocationResourceImpl extends SlimResource implements LocationResource {

    @Override
    public Response createLocation(UriInfo uriInfo, String name, long lattitude, long longitude) {
        if (uriInfo == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (name == null || name.equals("")) {
            name = "auto-generated";
        }

        Location location = mSlimService.createLocation(name, lattitude, longitude);
        if (location != null) {
            return Response.status(Response.Status.CREATED).entity(location).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response updateLocation(int locationId, String name) {
        if (name == null || name.equals("")) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Location location = mSlimService.getLocationById(locationId);
        if (location != null) {
            location.setName(name);
            if (mSlimService.updateLocation(location)) {
                return Response.ok(location).build();
            } else {
                return Response.serverError().build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response deleteLocation(int id) {
        if (mSlimService.deleteLocation(id)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @Override
    public Response fetchLocationById(int id) {
        Location location = mSlimService.getLocationById(id);
        if (location != null) {
            return Response.ok(location).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response fetchLocationByLongLat(long lattitude, long longitude) {
        Location location = mSlimService.retrieveLocationByLongLat(lattitude, longitude);
        if (location != null) {
            return Response.ok(location).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
