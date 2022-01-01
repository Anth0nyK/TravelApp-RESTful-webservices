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
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;
import java.math.BigDecimal;

import java.nio.charset.StandardCharsets;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import java.io.File; 
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;  
import java.util.Scanner;

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


    
    /**
     * Retrieves representation of an instance of travel.TravelServices
     * @return an instance of java.lang.String
     */
    //@PermitAll
    //@RolesAllowed("ADMIN")
    //@GET
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/createAccount/")
    public String getUserID(String user) throws MalformedURLException, IOException {
        RandomAPI random = new RandomAPI();
        String TheUserID = random.generateUUID();
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String UsersDirName = "userPW";
        String IDDirName = "userUUID";
        String TokenDirName = "tokens";
        
        String userName = null;
        String userPW = null;
        
        try{
            JSONObject obj = new JSONObject(user);
            userName = obj.getString("userName");
            userPW = obj.getString("userPW");
            
            
        }catch(Exception e){
            
        }
        
        
        if((userName != null) && (userPW != null)){
            
            try {
                //Make a folder to hold user account and password
                File dir = new File(UsersDirName);
                if (dir.exists()) {
                    //System.out.println("dir already exists");
                }
                if (dir.mkdirs()) {
                    //System.out.println(dir);
                }


                File myObj = new File(UsersDirName, userName + ".txt");
                if (myObj.createNewFile()) {
                    
                    System.out.println("File created: " + myObj.getName());
                    FileWriter myWriter = new FileWriter(myObj);
                    myWriter.write(userPW);
                    myWriter.close();
                    //System.out.println("Successfully wrote to the file.");
                
                } else {
                    //return myObj.getName().replaceFirst("[.][^.]+$", "");
                    return "User name already exists. Please choose another one.";
                    //System.out.println("File already exists.");
                }
                
                
              } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                return "An error occured.";
            }
            
        }
        else{
            return "Wrong JSON format";
        }
        
        
//         try {
//            File myObj = new File(UsersDirName, userName + ".txt");
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//              String data = myReader.nextLine();
//              System.out.println(data);
//              return data;
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }

//        if(TheUserID != "null"){
//            DirectExchangeBinder DirectBinder = new DirectExchangeBinder();
//            DirectBinder.createExchangeAndQueue(Direct_EXCHANGE_NAME, TheUserID+"D");
//
//            FanoutExchangeBinder FanoutBinder = new FanoutExchangeBinder();
//            FanoutBinder.createExchangeAndQueue(Fanout_EXCHANGE_NAME, TheUserID+"F");
//        }

        
        //return TheUserID;
        return "Account created";
    }
   
  
    

    
    
    
    
    
    //@RolesAllowed({"TEST","TEST2","ADMIN"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/query/")
    public String getqueryJson(@QueryParam("userID")String userID) throws IOException {
        String proposal = "";
        
        try{
            Connection connection = RabbitMQConnection.getConnection();
            if(connection != null){
                Channel channel = connection.createChannel();
                //channel.exchangeDeclare(Fanout_EXCHANGE_NAME,"fanout");
                //channel.queueDeclare(userID+"F",true,false,false,null);
                GetResponse response = channel.basicGet(userID+"F",true);
                
                if(response != null){
                    proposal = new String(response.getBody());
                }
                channel.close();
                connection.close();
            }
            
        }catch(Exception e){
            
        }
        
        
        return proposal;
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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/submit/")
    public String postProposal(String pFc) throws IOException {
        String status = "Incorrect JSON format";
        String messageID = "";
        RandomAPI random = new RandomAPI();
        messageID = random.generateUUID();
        
        String message = null;
        
        String theUserID = null;
        BigDecimal theLat = null;
        BigDecimal theLong = null;
        String theDate = null;
        String theWeather = null;
        
        
        
        
        
        try{
            JSONObject obj = new JSONObject(pFc);
            theUserID = obj.getString("userID");
            theLat = obj.getJSONObject("place").getBigDecimal("latitude");
            theLong = obj.getJSONObject("place").getBigDecimal("longitude");
            theDate = obj.getString("date");
            message = theLong.toString();
            
            WeatherAPI weather = new WeatherAPI();
            theWeather = weather.getWeather(theLat.toString(), theLong.toString(), theDate);
            
        }catch(Exception e){
            
        }
        
        
        if((theUserID != null) && (theLat != null) && (theLong != null) && (theDate != null) && (messageID != "") && theWeather != null){
            
            TripProposal proposal = new TripProposal();
            coordinates theCoord = new coordinates();
            theCoord.setlat(theLat);
            theCoord.setlong(theLong);
            
            proposal.setUserID(theUserID);
            proposal.setCoord(theCoord);
            proposal.setMessageID(messageID);
            proposal.setDate(theDate);
            proposal.setWeather(theWeather);
            
            message = new Gson().toJson(proposal);
            
        }
        else{
            return message = "Wrong JSON format";
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
    
    
    
    
    
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/submitIntent/")
    public String sendIntent(String intent) throws IOException {
        String status = "Incorrect JSON format";
        String messageID = "";
        
        RandomAPI random = new RandomAPI();
        messageID = random.generateUUID();
        
        String message = null;
        
        String theSenderID = null;
        String theReceiverID = null;
        String theMessageID = null;
        String theProposalID  = null;
        
        
        
        
        
        try{
            JSONObject obj = new JSONObject(intent);
            theSenderID = obj.getString("senderID");
            theReceiverID = obj.getString("receiverID");
            //theMessageID = obj.getString("messageID");
            theMessageID = messageID;
            theProposalID = obj.getString("proposalID");
            //message = theProposalID;
        }catch(Exception e){
            
        }
        
        
        if((theSenderID != null) && (theReceiverID != null) && (theMessageID != null) && (theProposalID != null)){
            
            //TripProposal proposal = new TripProposal();
            TripIntent TripIntent = new TripIntent();
            
            TripIntent.setproposalID(theProposalID);
            TripIntent.setsenderID(theSenderID);
            TripIntent.setreceiverID(theReceiverID);
            TripIntent.setmessageID(theMessageID);
            
            message = new Gson().toJson(TripIntent);
            
        }
        else{
            return message = "Wrong JSON format";
        }
                
      
       
        
        try{
            Connection connection = RabbitMQConnection.getConnection();
            if(connection != null){
                Channel channel = connection.createChannel();
                channel.basicPublish(Direct_EXCHANGE_NAME, 
                    theReceiverID+"D", // This parameter is used for the routing key, which is usually used for direct or topic queues.
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
    
    
    
    
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getIntent/")
    public String getIntentJson(@QueryParam("userID")String userID) throws IOException {
        String intent = "";
        
        try{
            Connection connection = RabbitMQConnection.getConnection();
            if(connection != null){
                Channel channel = connection.createChannel();
                //channel.exchangeDeclare(Fanout_EXCHANGE_NAME,"fanout");
                //channel.queueDeclare(userID+"F",true,false,false,null);
                GetResponse response = channel.basicGet(userID+"D",true);
                
                if(response != null){
                    intent = new String(response.getBody());
                }
                channel.close();
                connection.close();
            }
            
        }catch(Exception e){
            
        }
        
        
        return intent;
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
