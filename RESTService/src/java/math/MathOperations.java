/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Anthony
 */
@Path("/math")
public class MathOperations {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MathOperations
     */
    public MathOperations() {
    }

    /**
     * Retrieves representation of an instance of math.MathOperations
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("add")
    public String add(@QueryParam("x")int x, @QueryParam("y")int y) {
        //TODO return proper representation object
        return "" + (x + y);

    }
   
    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("minus")
    public String minus(@QueryParam("x")int x, @QueryParam("y")int y) {
        //TODO return proper representation object
        return "" + (x - y);

    }


        /**
     * PUT method for updating or creating an instance of MathOperations
     * @param content representation for the resource
     */
    
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }
    
}
