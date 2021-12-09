/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travel;

import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.math.BigDecimal;

import java.nio.charset.StandardCharsets;


/**
 * REST Web Service
 *
 *
 * @author Anthony
 */
@Path("travelProposal")
public class TravelServices {

    private final static String Direct_EXCHANGE_NAME = "testDirect";
    private final static String Fanout_EXCHANGE_NAME = "testFanout";
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TravelServices
     */
    public TravelServices() {
    }

    public String generateUUID() throws MalformedURLException, ProtocolException, UnsupportedEncodingException, IOException{
        String UUID = "null";
        
        try{
         String query = "https://api.random.org/json-rpc/1/invoke";
        String jsonBody = "{\"jsonrpc\":\"2.0\",\"method\":\"generateUUIDs\",\"params\":{\"apiKey\":\"b170a05d-268a-436b-b9e1-ce1a463ea38a\",\"n\":1},\"id\":0}";
        
        URL url = new URL(query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        
        OutputStream os = connection.getOutputStream();
        os.write(jsonBody.getBytes("UTF-8"));
        os.close();
        
        BufferedReader bfreader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder result = new StringBuilder();
        String line;
        
        
        while ((line = bfreader.readLine()) != null) {
             System.out.println(line);
             result.append(line);
        }
        
        bfreader.close();
        
        //Turn the string into JSONObject for further action
        JSONObject jsonobj = new JSONObject(result.toString());
        //Get the needed part, the UUID, from the json in String format
        UUID = jsonobj.getJSONObject("result").getJSONObject("random").getJSONArray("data").getString(0);
        
        
        }
        catch(IOException e){
        };
        
        
        return UUID;
    }
    
    /**
     * Retrieves representation of an instance of travel.TravelServices
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/generateUserID/")
    public String getUserID() throws MalformedURLException, IOException {
       
        String TheUserID = generateUUID();
        if(TheUserID != "null"){
            DirectExchangeBinder DirectBinder = new DirectExchangeBinder();
            DirectBinder.createExchangeAndQueue(Direct_EXCHANGE_NAME, TheUserID);

            FanoutExchangeBinder FanoutBinder = new FanoutExchangeBinder();
            FanoutBinder.createExchangeAndQueue(Fanout_EXCHANGE_NAME, TheUserID);
        }

        
        return TheUserID;
    }
   
  
    

    
    
    
    
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/query/")
    public String getqueryJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }


/*
    
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/submit/")
    public String postProposal(coordinates coord) {
        String longitude = "null";
        if(coord == null){
            longitude = "null";
        } else {
            longitude = coord.getlong().toString();
        }
        
        
        //JSONObject jsonobj = new JSONObject(coord);
        //String longitude = jsonobj.getBigDecimal("longitude").toString();
        
        return longitude;        
    }
    
   
    */
    
    
    @POST
    @Produces(MediaType.TEXT_HTML)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/submit/")
    public String postProposal(proposalFromClient pFc) throws IOException {
        String status = "Incorrect JSON format";
        String messageID = "";
        messageID = generateUUID();
        
        String theUserID = null;
        coordinates theCoord = null;
        BigDecimal theLat = null;
        BigDecimal theLong = null;
        String theDate = null;
        
        String message = "";
        
        try{
            theUserID = pFc.getUserID();
            theCoord = pFc.getCoord();
            theLat = theCoord.getlat();
            theLong = theCoord.getlong();
            theDate = pFc.getDate();
            
        }catch(NullPointerException e){
            
        }
        
        
        if((theUserID != null) && (theLat != null) && (theLong != null) && (theDate != "") && (messageID != "")){
            
            TripProposal proposal = new TripProposal();
            
            proposal.setUserID(theUserID);
            proposal.setCoord(theCoord);
            proposal.setMessageID(messageID);
            proposal.setDate(theDate);
            
            message = new Gson().toJson(proposal);
        }
       
        
        try{
            Connection connection = RabbitMQConnection.getConnection();
            if(connection != null){
                Channel channel = connection.createChannel();
                channel.basicPublish(Fanout_EXCHANGE_NAME, 
                    "", // This parameter is used for the routing key, which is usually used for direct or topic queues.
                    new AMQP.BasicProperties.Builder()
                        .contentType("text/plain")
                        .deliveryMode(2)
                        .priority(1)
                        //.expiration("60000")
                        .build(),
                    message.getBytes(StandardCharsets.UTF_8));
            //System.out.println(" [x] Sent '" + "" + ":" + message + "'");
                channel.close();
                connection.close();
            }
            
        }catch (Exception e){
            
        }
            

            
            
            
            
            
            //status = pFc.getCoord().getlong().toString();
        
        
        
        //JSONObject jsonobj = new JSONObject(coord);
        //String longitude = jsonobj.getBigDecimal("longitude").toString();
        
        return message;        
    }
    
    
    
    
    /**
     * PUT method for updating or creating an instance of TravelServices
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
