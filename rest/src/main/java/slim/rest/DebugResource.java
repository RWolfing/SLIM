/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import slim.core.model.Event;
import slim.rest.impl.SlimResource;

/**
 * Only for debugging purposes.
 * Function to clear the database
 * 
 * @author Robert
 */
@Path("debug")
public class DebugResource extends SlimResource{
    /**
     * Deletes the {@link Event} with the given id
     *
     * @return 200 OK if the user was deleted Else appropriate status code
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
