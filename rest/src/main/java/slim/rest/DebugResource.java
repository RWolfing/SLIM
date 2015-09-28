package slim.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import slim.rest.impl.SlimResource;

/**
 * Only for debugging purposes.
 * Function to clear the database
 * 
 * @author Robert Wolfinger
 */
@Path("debug")
public class DebugResource extends SlimResource{
  
    /**
     * Drops all tables of the database
     *
     * @return 200 OK if tables could be cleared
     */
    @DELETE
    @Path("/dropall")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
   public Response dropAllTables(){
        if(mSlimService.dropAllTables()){
            return  Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } 
    }
}
