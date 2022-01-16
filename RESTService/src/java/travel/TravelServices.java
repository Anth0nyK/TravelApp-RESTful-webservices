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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.HttpHeaders;
import org.glassfish.jersey.internal.util.Base64;

/**
 * REST Web Service
 *
 *
 * @author Anthony
 */
@Path("travelProposal")
public class TravelServices {

//    private final static String Direct_EXCHANGE_NAME = "testDirect";
//    private final static String Fanout_EXCHANGE_NAME = "testFanout";
    private final static String Direct_EXCHANGE_NAME = "TRAVEL_INTENT";
    private final static String Fanout_EXCHANGE_NAME = "TRAVEL_OFFERS";
    
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
    @PermitAll
    //@RolesAllowed("ADMIN")
    //@GET
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //@Path("/createAccount/")
    @Path("account")
    public String getUserID(String user) throws MalformedURLException, IOException {
        RandomAPI random = new RandomAPI();
        String TheUserID = null;
        String TheToken = null;
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String UsersDirName = "userPW";
        String IDDirName = "userUUID";
        //String TokenDirName = "tokens";
        
        String userName = null;
        String userPW = null;
        
        try{
            JSONObject obj = new JSONObject(user);
            userName = obj.getString("username");
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
                
                //Make a folder to hold user UUID
                File dir2 = new File(IDDirName);
                if (dir2.exists()) {
                    //System.out.println("dir already exists");
                }
                if (dir2.mkdirs()) {
                    //System.out.println(dir);
                }
                
                
//                //Make a folder to hold user Token
//                File dir3 = new File(TokenDirName);
//                if (dir3.exists()) {
//                    //System.out.println("dir already exists");
//                }
//                if (dir3.mkdirs()) {
//                    //System.out.println(dir);
//                }

                

                File pwFile = new File(UsersDirName, userName + ".txt");
                File uuidFile = new File(IDDirName, userName + ".txt");
                //File tokenFile = new File (TokenDirName, userName + ".txt");
                
                //if ((pwFile.createNewFile()) && (uuidFile.createNewFile()) && (tokenFile.createNewFile())) {
                if ((pwFile.createNewFile()) && (uuidFile.createNewFile())) {         
                    //System.out.println("File created: " + pwFile.getName());
                    //Write the userPW to the file
                    FileWriter pwWriter = new FileWriter(pwFile);
                    pwWriter.write(userPW);
                    pwWriter.close();
                    
                    
                    //Generate a random user UUID for the user and write to the file
                    TheUserID = random.generateUUID();
                    if(TheUserID == null){
                        return "random API is offline. Please try again later";
                        
                    }
                    FileWriter uuidWriter = new FileWriter(uuidFile);
                    uuidWriter.write(TheUserID);
                    uuidWriter.close();
                    
                    
//                    //Generate a random Token for the user and write to the file
//                    TheToken = random.generateUUID();
//                    if(TheToken == null){
//                        return "random API is offline. Please try again later";
//                    }
//                    FileWriter tokenWriter = new FileWriter(tokenFile);
//                    tokenWriter.write(TheToken);
//                    tokenWriter.close();
                    
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

        if(TheUserID != null){
            DirectExchangeBinder DirectBinder = new DirectExchangeBinder();
            DirectBinder.createExchangeAndQueue(Direct_EXCHANGE_NAME, TheUserID+"D");

            FanoutExchangeBinder FanoutBinder = new FanoutExchangeBinder();
            FanoutBinder.createExchangeAndQueue(Fanout_EXCHANGE_NAME, TheUserID+"F");
        }

        
        //return TheUserID;
        return "Account created.";
    }
   
    
    
  
    //{"userName":"123","userPW":"123"}
    @PermitAll
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login/")
    public String loginAccount(String user) throws MalformedURLException, IOException {
        
        String UsersDirName = "userPW";
        String TokenDirName = "tokens";
        String userName = null;
        String userPW = null;
        String token = null;
        String pwInFile = null;
        Boolean correctPW = false;
        
        
        
        try{
            JSONObject obj = new JSONObject(user);
            userName = obj.getString("username");
            userPW = obj.getString("userPW");
            
            
        }catch(Exception e){
            
        }
        
        
        if((userName != null) && (userPW != null)){
            
            try {
                File myObj = new File(UsersDirName, userName + ".txt");
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                  pwInFile = myReader.nextLine();
                  //System.out.println(data);

                }
                myReader.close();
                
                //System.out.println(pwInFile);
                //System.out.println(userPW);
                if(userPW.equals(pwInFile)){
                    correctPW = true;
                }
                
            } catch (FileNotFoundException e) {
                //System.out.println("An error occurred.");
                //e.printStackTrace();
            }
            
        }
        else{
            return "Wrong JSON format";
        }
        
        
//        //If pw is correct, get the token for the user for future actions
//        if(correctPW == true){
//            try {
//                File myObj = new File(TokenDirName, userName + ".txt");
//                Scanner myReader = new Scanner(myObj);
//                while (myReader.hasNextLine()) {
//                  String tokenInFile = myReader.nextLine();
//                  //System.out.println(data);
//                  token = tokenInFile;
//                }
//                myReader.close();
//            } catch (FileNotFoundException e) {
//                System.out.println("An error occurred.");
//                e.printStackTrace();
//            }
//            
//        }
//        
//
//        if(correctPW == true && token != null){
//            return token;
//        }
        if(correctPW == true){
            return "OK";
        }


        return "Wrong username or password.";
    }
    
    
    
    
    
    @RolesAllowed("user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //@Path("/query/")
    @Path("proposal")
    public String getqueryJson(@Context HttpHeaders httpHeaders, @QueryParam("userID")String userID) throws IOException {
        
        String auth = httpHeaders.getRequestHeader("Authorization").get(0);
        //System.out.println("Auth: "+auth);
        final String encodedUserPassword = auth.replaceFirst("Basic" + " ", "");
              
        //Decode username and password
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        
        if(userID.equals(username) == false){
            //return "Usernames in Auth and request body are not the same. Action denied.";
            return "{\"error\":\"Usernames in Auth and request body are not the same. Action denied.\"}";
        }
        
        String proposal = "";
        String IDDirName = "userUUID";
        String userUUID = null;
        
        //Get UUID from the file
        try {
            File myObj = new File(IDDirName, userID + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String UUIDinFile = myReader.nextLine();
              //System.out.println(data);
              userUUID = UUIDinFile;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            //return "Receiver does not exist. Please double check the username";
            return "{\"error\":\"Receiver does not exist. Please double check the username.\"}";
        }
        
        try{
            Connection connection = RabbitMQConnection.getConnection();
            if(connection != null){
                Channel channel = connection.createChannel();
                //channel.exchangeDeclare(Fanout_EXCHANGE_NAME,"fanout");
                //channel.queueDeclare(userID+"F",true,false,false,null);
                GetResponse response = channel.basicGet(userUUID+"F",true);
                
                if(response != null){
                    proposal = new String(response.getBody());
                }
                channel.close();
                connection.close();
            }
            
        }catch(Exception e){
            //return "rabbitMQ offline. Please try it again later.";
            return "{\"error\":\"rabbitMQ offline. Please try it again later.\"}";
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
    
    @RolesAllowed("user")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //@Path("/submitProposal/")
    @Path("proposal")
    public String postProposal(@Context HttpHeaders httpHeaders, String pFc) throws IOException {
        
        String auth = httpHeaders.getRequestHeader("Authorization").get(0);
        //System.out.println("Auth: "+auth);
        final String encodedUserPassword = auth.replaceFirst("Basic" + " ", "");
              
        //Decode username and password
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        
        
        String status = "Incorrect JSON format";
        String messageID = "";
        RandomAPI random = new RandomAPI();
        String TokenDirName = "tokens";
        String message = null;
        
        
        String theUserID = null;
        String theTitle = null;
        //BigDecimal theLat = null;
        //BigDecimal theLong = null;
        String theDate = null;
        String theWeather = null;
        String thePlace = null;
        String theToken = null;
        
        String cacheDir = "weatherCache/";
        String regex = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
        
        Boolean tokenCorrect = false;
        
        try{
            JSONObject obj = new JSONObject(pFc);
            theUserID = obj.getString("userID");
            thePlace = obj.getString("place");
            //theLat = obj.getJSONObject("place").getBigDecimal("latitude");
            //theLong = obj.getJSONObject("place").getBigDecimal("longitude");
            theDate = obj.getString("date");
            //message = theLong.toString();
            theTitle = obj.getString("title");
            //theToken = obj.getString("token");
            
        }catch(Exception e){
            //return "Incorrect JSON format.";
            return "{\"error\":\"Incorrect JSON format.\"}";
        }
        
        
        if(theUserID.equals(username) == false){
            //return "Usernames in Auth and request body are not the same. Action denied.";
            return "{\"error\":\"Usernames in Auth and request body are not the same. Action denied.\"}";
        }
        
        
//        try {
//            File myObj = new File(TokenDirName, theUserID + ".txt");
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//              String tokenInFile = myReader.nextLine();
//              //System.out.println(data);
//              if(tokenInFile.equals(theToken)){
//                  tokenCorrect = true;
//              }
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            
//        }
//        
//        if(tokenCorrect == false){
//            return "Wrong username or token";
//        }
        
        
            
            
        //if((theUserID != null) && (theLat != null) && (theLong != null) && (theDate != null) && (messageID != "") && theWeather != null){
        //if((theUserID.isEmpty() == false) && (theTitle.isEmpty() == false) && (theDate.isEmpty() == false) && (thePlace.isEmpty() == false) && (theToken.isEmpty() == false)){
        if((theUserID.isEmpty() == false) && (theTitle.isEmpty() == false) && (theDate.isEmpty() == false) && (thePlace.isEmpty() == false)){    

            if(theDate.matches(regex) == false){
                //return "Incorrect date format. Should be in YYYY-MM-DD";
                return "{\"error\":\"Incorrect date format. Should be in YYYY-MM-DD.\"}";
            }

            try{
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
                LocalDateTime now = LocalDateTime.now();  
                String localDateSTR = dtf.format(now);
                System.out.println(localDateSTR);  

                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                //Date localDate = sdf.parse(localDateSTR);
                //Date userDate = sdf.parse(theDate);

                LocalDate from = LocalDate.parse(localDateSTR);
                LocalDate to = LocalDate.parse(theDate);

                //int result = localDate.compareTo(userDate);
                //System.out.println(result);

                long days = ChronoUnit.DAYS.between(from, to);    // 6 days

                if(days > 14){
                    //return "Cannot submit a proposal with a trip date more than 14 days in the future.";
                    return "{\"error\":\"Cannot submit a proposal with a trip date more than 14 days in the future.\"}";
                }
                else if(days < 0){
                    //return "Cannot submit a proposal with a trip date before today.";
                    return "{\"error\":\"Cannot submit a proposal with a trip date before today.\"}";
                }

            }catch(Exception e){

            }

            messageID = random.generateUUID();
            if(messageID == null){
                //return "random API offline. Please try again later.";
                return "{\"error\":\"random API offline. Please try again later.\"}";
            }
            
            
            
            //Check if the weather data already exists in the folder
            try {
                //Change the place string to allow it to create file
                String thePalceInFile = thePlace.replaceAll(" ", "_").toLowerCase();
                
                //Make a folder to hold user account and password
                File dir = new File(cacheDir+thePalceInFile+"/");
                if (dir.exists()) {
                    //System.out.println("dir already exists");
                }
                if (dir.mkdirs()) {
                    //System.out.println(dir);
                }

                
                //System.out.println(thePalceInFile);
                File cacheFile = new File(cacheDir+thePalceInFile+"/", theDate + ".txt");
                //If the weather cache is not exist, create the txt and write the weather api output into it
                if ((cacheFile.createNewFile())) {
                    
                    WeatherAPI weather = new WeatherAPI();
                    theWeather = weather.getWeather(thePalceInFile, theDate);
                    if(theWeather == null){
                        cacheFile.delete();
                        dir.delete();
                        //return "The location cannot be found.";
                        return "{\"error\":\"The location cannot be found.\"}";
                    }
                    
                    //If weather can be found, 
                    FileWriter pwWriter = new FileWriter(cacheFile);
                    
                    pwWriter.write(theWeather);
                    pwWriter.close();

                    //System.out.println("Successfully wrote to the file.");

                } else {
                    //If the weather cache for "the place" is exist, read the file to take the data, do not call weather api
                    try {
                        
                        Scanner myReader = new Scanner(cacheFile);
                        while (myReader.hasNextLine()) {
                          String weatherInFile = myReader.nextLine();
                          //System.out.println(data);
                          theWeather = weatherInFile;
                        }
                        myReader.close();
                        
                    } catch (FileNotFoundException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                }


              } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
                //return "An error occured.";
                return "{\"error\":\"An error occured.\"}";
            }
            
            
            
            
            
            
            TripProposal proposal = new TripProposal();
            coordinates theCoord = new coordinates();
//            theCoord.setlat(theLat);
//            theCoord.setlong(theLong);
            
            proposal.setUserID(theUserID);
            //proposal.setCoord(theCoord);
            proposal.setTitle(theTitle);
            proposal.setLocation(thePlace);
            proposal.setMessageID(messageID);
            proposal.setDate(theDate);
            proposal.setWeather(theWeather);
            
            message = new Gson().toJson(proposal);
            
        }
        else{
            //return message = "Fields cannot be empty.";
            return message = "{\"error\":\"Fields cannot be empty\"}";
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
            //return "rabbitMQ is offline. Please try it again later.";
            return "{\"error\":\"rabbitMQ is offline. Please try it again later.\"}";
        }
            
            
            
        //status = pFc.getCoord().getlong().toString();
        
        
        
        //JSONObject jsonobj = new JSONObject(coord);
        //String longitude = jsonobj.getBigDecimal("longitude").toString();
        
        
        
        
        return message;        
    }
    
    
    
    
    
    @RolesAllowed("user")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //@Path("/submitIntent/")
    @Path("intent")
    public String sendIntent(@Context HttpHeaders httpHeaders, String intent) throws IOException {
        
        String auth = httpHeaders.getRequestHeader("Authorization").get(0);
        //System.out.println("Auth: "+auth);
        final String encodedUserPassword = auth.replaceFirst("Basic" + " ", "");
              
        //Decode username and password
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        //System.out.println("username: "+username);
        //System.out.println("password:" + password);
        
        String status = "Incorrect JSON format";
        String messageID = "";
        
        String TokenDirName = "tokens";
        String IDDirName = "userUUID";
        
        RandomAPI random = new RandomAPI();
        
        
        String message = null;
        
        String theSenderID = null;
        String theReceiverID = null;
        String theMessageID = null;
        String theProposalID  = null;
        String theToken = null;
        String receiverUUID = null;
        String theMessage = null;
        boolean tokenCorrect = false;
        
        
        try{
            JSONObject obj = new JSONObject(intent);
            theSenderID = obj.getString("senderID");
            theReceiverID = obj.getString("receiverID");
            //theMessageID = obj.getString("messageID");
            //theMessageID = messageID;
            //theProposalID = obj.getString("proposalID");
            theMessage = obj.getString("message");
            //theToken = obj.getString("token");
            //message = theProposalID;
        }catch(Exception e){
            //return "Incorrect JSON format";
            return message = "{\"error\":\"Incorrect JSON format.\"}";
        }
        
        //Verify if the username in the header and username in the request body is the same
        if(theSenderID.equals(username) == false){
            //return "Usernames in Auth and request body are not the same. Action denied.";
            return message = "{\"error\":\"Usernames in Auth and request body are not the same. Action denied.\"}";
        }
        
        
        //Verify the token from the sender before allowing to do actions
//        try {
//            File myObj = new File(TokenDirName, theSenderID + ".txt");
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//              String tokenInFile = myReader.nextLine();
//              //System.out.println(data);
//              if(tokenInFile.equals(theToken)){
//                  tokenCorrect = true;
//              }
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            
//        }
//        
//        if(tokenCorrect == false){
//            return "Wrong username or token";
//        }
        
        
        
        
        //Get UUID from the file
        try {
            File myObj = new File(IDDirName, theReceiverID + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String UUIDinFile = myReader.nextLine();
              //System.out.println(data);
              receiverUUID = UUIDinFile;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            //return "Receiver does not exist. Please double check the username";
            return message = "{\"error\":\"Receiver does not exist. Please double check the username.\"}";
        }
        
        
        
        //if((theSenderID.isEmpty() == false) && (theReceiverID.isEmpty() == false) && (theProposalID.isEmpty() == false) && (theToken.isEmpty() == false)){
        //if((theSenderID.isEmpty() == false) && (theReceiverID.isEmpty() == false) && (theMessage.isEmpty() == false) && (theToken.isEmpty() == false)){   
        if((theSenderID.isEmpty() == false) && (theReceiverID.isEmpty() == false) && (theMessage.isEmpty() == false)){  
            
            
            theMessageID = random.generateUUID();
            if(theMessageID == null){
                //return "random API is offline. Please try again later.";
                return message = "{\"error\":\"random API is offline. Please try again later.\"}";
            }
            
            //TripProposal proposal = new TripProposal();
            TripIntent TripIntent = new TripIntent();
            
            //TripIntent.setproposalID(theProposalID);
            TripIntent.setMessage(theMessage);
            TripIntent.setsenderID(theSenderID);
            TripIntent.setreceiverID(theReceiverID);
            TripIntent.setmessageID(theMessageID);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            TripIntent.setDate(dateFormat.format(date));
            
            message = new Gson().toJson(TripIntent);
            
        }
        else{
            //return message = "Fields cannot be empty.";
            return message = "{\"error\":\"Fields cannot be empty\"}";
        }
                
      
        
        
        
       
        
        try{
            Connection connection = RabbitMQConnection.getConnection();
            if(connection != null){
                Channel channel = connection.createChannel();
                channel.basicPublish(Direct_EXCHANGE_NAME, 
                    receiverUUID+"D", // This parameter is used for the routing key, which is usually used for direct or topic queues.
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
            //return "rabbitMQ is offline. Please try this again later.";
            return message = "{\"error\":\"rabbitMQ is offline. Please try this again later.\"}";
        }
            
            
            
        //status = pFc.getCoord().getlong().toString();
        
        
        
        //JSONObject jsonobj = new JSONObject(coord);
        //String longitude = jsonobj.getBigDecimal("longitude").toString();
        
        
        
        
        return message;        
    }
    
    
    
    
    
    @RolesAllowed("user")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //@Path("/getIntent/")
    @Path("intent")
    public String getIntentJson(@Context HttpHeaders httpHeaders, @QueryParam("userID")String userID) throws IOException {

        String IDDirName = "userUUID";
        
        String auth = httpHeaders.getRequestHeader("Authorization").get(0);
        //System.out.println("Auth: "+auth);
        final String encodedUserPassword = auth.replaceFirst("Basic" + " ", "");
              
        //Decode username and password
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));;

        //Split username and password tokens
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();
        
                //Verify if the username in the header and username in the request body is the same
        if(userID.equals(username) == false){
            //return "Usernames in Auth and request body are not the same. Action denied.";
            return "{\"error\":\"Usernames in Auth and request body are not the same. Action denied.\"}";
        }
        
        String intent = "";
        String userUUID = "";
        
        //Get UUID from the file
        try {
            File myObj = new File(IDDirName, userID + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String UUIDinFile = myReader.nextLine();
              //System.out.println(data);
              userUUID = UUIDinFile;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            //return "Receiver does not exist. Please double check the username";
            return "{\"error\":\"Receiver does not exist. Please double check the username.\"}";
        }
        
        
        try{
            Connection connection = RabbitMQConnection.getConnection();
            if(connection != null){
                Channel channel = connection.createChannel();
                //channel.exchangeDeclare(Fanout_EXCHANGE_NAME,"fanout");
                //channel.queueDeclare(userID+"F",true,false,false,null);
                GetResponse response = channel.basicGet(userUUID+"D",true);
                
                if(response != null){
                    intent = new String(response.getBody());
                }
                channel.close();
                connection.close();
            }
            
        }catch(Exception e){
            //return "rabbitMQ is offline. Please try this later.";
            return "{\"error\":\"rabbitMQ is offline. Please try this later.\"}";
        }
        
        
        return intent;
    }
    
    
    
    
    @RolesAllowed("admin")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    //@Path("/getIntent/")
    @Path("account")
    public String deleteUser(@Context HttpHeaders httpHeaders, String userID) throws IOException {

        JSONObject obj = new JSONObject(userID);
        String userName = obj.getString("username");
        
        String IDDirName = "userUUID";
        String PWDirName = "userPW";
        
        String userUUID = "";
        
        //Delete the UUID file of the user from the folder
        try {
            File UUIDfile = new File(IDDirName, userName + ".txt");
            if(!UUIDfile.delete()){
                return "{\"error\":\"user does not exist.\"}";
            }
            File PWfile = new File(PWDirName, userName + ".txt");
            if(!PWfile.delete()){
                return "{\"error\":\"user does not exist.\"}";
            }
            
        } catch (Exception e) {
            //return "Receiver does not exist. Please double check the username";
            return "{\"error\":\"user does not exist.\"}";
        }
        
        

        
        
        return "{\"result\":\"Successfully removed the user account with username:"
                + userName + "\"}";
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
