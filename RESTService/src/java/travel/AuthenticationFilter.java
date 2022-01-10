/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travel;

 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
 
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
 
import org.glassfish.jersey.internal.util.Base64;
 
/**
 * This filter verify the access permissions for a user
 * based on username and password provided in request
 * */
@Provider
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter
{
     
    @Context
    private ResourceInfo resourceInfo;
     
    private static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
      
    @Override
    public void filter(ContainerRequestContext requestContext)
    {
        Method method = resourceInfo.getResourceMethod();
        //Check that if it allowed for all to access
        if( ! method.isAnnotationPresent(PermitAll.class))
        {
            //If it is denied for all
            if(method.isAnnotationPresent(DenyAll.class))
            {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("Access blocked for all users.").build());                
                return;
            }
              
            //Get request headers
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
              
            //Fetch authorization header and put them into a list
            final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
              
            //If no authorization information present; block access
            if(authorization == null || authorization.isEmpty())
            {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("You cannot access this resource").build());
                return;
            }
              
            //Get encoded username and password
            final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
              
            //Decode username and password
            String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;
  
            //Split username and password tokens
            final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            final String username = tokenizer.nextToken();
            final String password = tokenizer.nextToken();
              
            //Verifying Username and password
//            System.out.println(username);
//            System.out.println(password);
              
            //Verify user access
            if(method.isAnnotationPresent(RolesAllowed.class))
            {
                RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
                  
                //Is user valid?
                if( ! isUserAllowed(username, password, rolesSet))
                {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("You cannot access this resource").build());
                    return;
                }
            }
        }
    }
    
    //Check that if the user is allowed
    private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet)
    {
        boolean isAllowed = false;
        String UsersDirName = "userPW";
        //File pwFile = new File(UsersDirName, username + ".txt");
        String pwInFile = null;
        boolean accountExist = false;
        if(username.equals("admin") && password.equals("admin"))
        {
            String userRole = "admin";
            isAllowed = true;
            //Verify user role
//            if(rolesSet.contains(userRole)){
//                isAllowed = true;
//            }
            
        }
        
        try {
            File myObj = new File(UsersDirName, username + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              pwInFile = myReader.nextLine();
              System.out.println(pwInFile);
         }
        
        myReader.close();

         //System.out.println(pwInFile);
         //System.out.println(userPW);
         if(password.equals(pwInFile)){
             accountExist = true;
         }

        } catch (FileNotFoundException e) {
            //System.out.println("An error occurred.");
            //e.printStackTrace();
        }
        
        if(accountExist == true){
            String userRole = "user";

            if(rolesSet.contains(userRole)){
                isAllowed = true;
            }
        }

        
        return isAllowed;
    }
}