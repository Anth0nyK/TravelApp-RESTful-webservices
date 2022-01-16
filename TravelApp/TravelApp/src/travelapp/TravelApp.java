/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelapp;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;
import org.json.JSONObject;

/**
 *
 * @author Anthony
 */
public class TravelApp {

    public static String ac = null;
    public static String pw = null;
    public static Scanner input = new Scanner(System.in);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
        // TODO code application logic here
        int choice;
        boolean loginSuccessfully = false;
        
        while(loginSuccessfully == false) {
            System.out.println("-----Welcome-----");
            System.out.println("1: Login");
            System.out.println("2: Register");
            System.out.println("0: Exit");
            System.out.print("Choice: ");
            choice = input.nextInt();
            switch(choice) {
                case 1:
                    loginSuccessfully = login();
                    break;
                case 2:
                    register();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(">>>>>Invalid choice<<<<<");
                    System.out.println();
            }
        }
        
        while(true) {
            System.out.println("-----Main menu-----");
            System.out.println("1: Submit proposal");
            System.out.println("2: Query");
            System.out.println("3: Submit intent");
            System.out.println("4: Get intent");
            System.out.println("5: List all or search proposals");
            System.out.println("6: List intents");
            System.out.println("0: Exit");
            System.out.print("Choice: ");
            choice = input.nextInt();
            switch(choice) {
                case 1:
                    submitProposal();
                    break;
                case 2:
                    query();
                    break;
                case 3:
                    submitIntent();
                    break;
                case 4:
                    getIntent();
                    break;
                case 5:
                    while(true){
                        System.out.println("1: List all");
                        System.out.println("2: Search");
                        System.out.println("3: Exit");
                        System.out.print("Choice: ");
                        choice = input.nextInt();

                        if(choice == 1){
                            listallProposal();
                            break;
                        }else if(choice == 2){
                            searchProposal();
                            break;
                        }else{
                            break;
                        }
                    }
                    break;
                    
                case 6:
                    listIntent();
                    break;
                case 0:
                    return;
                default:
                    System.out.println(">>>>>Invalid choice<<<<<");
                    System.out.println();
            }
        }
        
    }
    
    public static void register() {
        System.out.println();
        System.out.println("-----Register-----");
        String acInput = null;
        String pwInput = null;
        String pwInput2 = null;
        
        while(true){
            System.out.print("Account: ");
            acInput = input.next();
            System.out.print("Password: ");
            pwInput = input.next();
            System.out.print("Confirm password: ");
            pwInput2 = input.next();
            
            if(pwInput.equals(pwInput2) == true){
                break;
            }
            else{
                System.out.print("Passwords do not match. Please try again.");
                System.out.print("");
            }
            
        }
        

        String response = null;
        
        try{
            String query = "http://localhost:8080/RESTService/webresources/travelProposal/createAccount/";
            String jsonBody = "{\"username\":\"" + acInput + "\",\"userPW\":\"" + pwInput + "\"}";

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
                 //System.out.println(line);
                 result.append(line);
            }

            bfreader.close();
            //response = result.toString();
            //Turn the string into JSONObject for further action
            try{
                JSONObject jsonobj = new JSONObject(result.toString());
                response = jsonobj.getString("error");
            }catch(Exception e){
                response = result.toString();
            }
        }
        catch(Exception e){
            response = "Travel App API is offline. Please try this again later.";
        };
        
        
        //System.out.println(response);
        
        if(response.equals("User name already exists. Please choose another one.") == true) {
            System.out.println("User name already exists. Please choose another one.");
            System.out.println();
        }
        else if(response.equals("Account created.") == true){
            System.out.println("Account created successfully.");
            System.out.println();
        }
        else {
            System.out.println("Error. Please update your client.");
            System.out.println();
        }
    }
    
    
    public static boolean login() {
        System.out.println();
        System.out.println("-----Login-----");
        System.out.print("Account: ");
        String acInput = input.next();
        System.out.print("Password: ");
        String pwInput = input.next();
        
        
        String response = null;
        
        try{
            String query = "http://localhost:8080/RESTService/webresources/travelProposal/login/";
            String jsonBody = "{\"username\":\"" + acInput + "\",\"userPW\":\"" + pwInput + "\"}";

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
                 //System.out.println(line);
                 result.append(line);
            }

            bfreader.close();
            //response = result.toString();
            //Turn the string into JSONObject for further action
            
            //Get the needed part, the UUID, from the json in String format
            try{
                JSONObject jsonobj = new JSONObject(result.toString());
                response = jsonobj.getString("error");
            }catch(Exception e){
                response = result.toString();
            }

        }
        catch(Exception e){
            response = "Travel App API is offline. Please try this again later.";
        }
        
        
        //System.out.println(response);
        
        if(response.equals("OK") == true) {
            System.out.println("*****Login Successfully*****");
            ac = acInput;
            pw = pwInput;
            System.out.println();
            return true;
        }else {
            System.out.println(">>>>>Incorrect account or password<<<<<");
            System.out.println();
            return false;
        }
        
        
//        if(acInput.compareTo(ac) == 0 && pwInput.compareTo(pw) == 0) {
//            System.out.println("*****Login Successfully*****");
//            System.out.println();
//            return true;
//        }else {
//            System.out.println(">>>>>Incorrect account or password<<<<<");
//            System.out.println();
//            return false;
//        }
    }
    
    public static void submitProposal() throws IOException {
        BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
                
        System.out.println();
        System.out.println("-----Submit a travel proposal-----");
        System.out.print("Title: ");
        //String title = input.nextLine();
        String title = br.readLine();
        System.out.print("Place: ");
        String place = br.readLine();
        System.out.print("date within 14 days (YYYY-MM-DD): ");
        String date = input.next();
        
        
        String response = null;
        
        
        try{

            String userCredentials = ac+":"+pw;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

            
            String query = "http://localhost:8080/RESTService/webresources/travelProposal/proposal/";
            String jsonBody = "{\"userID\":\"" + ac + "\",\"title\":\"" + title + "\"," + "\"place\":\"" + place + "\"," + "\"date\":\"" + date + "\"}";

            URL url = new URL(query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty ("Authorization", basicAuth);
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
                 //System.out.println(line);
                 result.append(line);
            }

            bfreader.close();
            //response = result.toString();
            //Turn the string into JSONObject for further action
            try{
                JSONObject jsonobj = new JSONObject(result.toString());
                response = jsonobj.getString("error");
            }catch(Exception e){
                response = result.toString();
            }
        }
        catch(Exception e){
            response = "Travel App API is offline. Please try this again later.";
        }
        
        System.out.println(response);
    }
    
    public static void query() throws IOException {
        
        boolean upToDate = false;
        int counter = 0;
        
        while(upToDate == false){
            String response = null;

            String userID = null;
            String title = null;
            String location = null;
            String messageID = null;
            String date = null;
            String weather = null;

            try{

                String userCredentials = ac+":"+pw;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

                URL url = new URL("http://localhost:8080/RESTService/webresources/travelProposal/proposal/?userID="+ac);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty ("Authorization", basicAuth);
                con.setRequestMethod("GET");

                //int status = con.getResponseCode();

                BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();


                System.out.println(content.toString());

                //Turn the string into JSONObject for further action
                JSONObject jsonobj = new JSONObject(content.toString());

                userID = jsonobj.getString("userID");
                title = jsonobj.getString("title");
                location = jsonobj.getString("location");
                messageID = jsonobj.getString("messageID");
                date = jsonobj.getString("date");
                weather = jsonobj.getString("weather");

                try {
                    String dirName = "UserData/"+ ac + "/proposal/"+date + "/" + location.replaceAll(" ", "_").toLowerCase();;
                    //Make a folder to hold user account and password
                    File dir = new File(dirName);
                    if (dir.exists()) {
                        //System.out.println("dir already exists");
                    }
                    if (dir.mkdirs()) {
                        //System.out.println(dir);
                    }

                    File data = new File(dirName, messageID + ".json");
                    //File tokenFile = new File (TokenDirName, userName + ".txt");

                    //if ((pwFile.createNewFile()) && (uuidFile.createNewFile()) && (tokenFile.createNewFile())) {
                    if (data.createNewFile()) {         
                        //System.out.println("File created: " + pwFile.getName());
                        //Write the userPW to the file
                        FileWriter FWriter = new FileWriter(data);
                        FWriter.write(content.toString());
                        FWriter.close();
                    }

                    counter = counter + 1;

                  } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

            }
            catch(Exception e){
                upToDate = true;
            };
        }
        if(counter == 0){
            System.out.println("You do not have any new proposal.");
        }
        else{
            System.out.println("You have " + counter + " new proposal(s).");
        }
        
        System.out.println("You are up-to-date now.");
        System.out.println();
    }
    
    public static void submitIntent() throws IOException {
        BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
                
        System.out.println();
        System.out.println("-----Submit a travel intent to an user-----");
        System.out.print("Receiver: ");
        //String title = input.nextLine();
        String receiverID = br.readLine();
        System.out.print("Message: ");
        String message = br.readLine();
        
        
        String response = null;
        
        
        try{

            String userCredentials = ac+":"+pw;
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

            
            String query = "http://localhost:8080/RESTService/webresources/travelProposal/intent/";
            String jsonBody = "{\"senderID\":\"" + ac + "\",\"receiverID\":\"" + receiverID + "\"," + "\"message\":\"" + message + "\"}";

            URL url = new URL(query);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty ("Authorization", basicAuth);
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
                 //System.out.println(line);
                 result.append(line);
            }

            bfreader.close();
            //response = result.toString();
            //Turn the string into JSONObject for further action
            try{
                JSONObject jsonobj = new JSONObject(result.toString());
                response = jsonobj.getString("error");
            }catch(Exception e){
                response = result.toString();
            }

        }
        catch(Exception e){
            response = "Travel App API is offline. Please try this again later.";
        }
        
        System.out.println(response);
    }
    
    public static void getIntent() {

        boolean upToDate = false;
        int counter = 0;
        
        while(upToDate == false){
            String response = null;

            String senderID = null;
            String receiverID = null;
            String message = null;
            String messageID = null;
            String date = null;

            try{

                String userCredentials = ac+":"+pw;
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

                URL url = new URL("http://localhost:8080/RESTService/webresources/travelProposal/intent/?userID="+ac);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty ("Authorization", basicAuth);
                con.setRequestMethod("GET");

                //int status = con.getResponseCode();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();


                System.out.println(content.toString());

                //Turn the string into JSONObject for further action
                JSONObject jsonobj = new JSONObject(content.toString());

                senderID = jsonobj.getString("senderID");
                receiverID = jsonobj.getString("receiverID");
                message = jsonobj.getString("message");
                date = jsonobj.getString("date");
                messageID = jsonobj.getString("messageID");
                

                try {
                    String dirName = "UserData/"+ac+"/intent/" + date;
                    File dir = new File(dirName);
                    if (dir.exists()) {
                        //System.out.println("dir already exists");
                    }
                    if (dir.mkdirs()) {
                        //System.out.println(dir);
                    }

                    File data = new File(dirName, messageID + ".json");
                    //File tokenFile = new File (TokenDirName, userName + ".txt");

                    //if ((pwFile.createNewFile()) && (uuidFile.createNewFile()) && (tokenFile.createNewFile())) {
                    if (data.createNewFile()) {         
                        //System.out.println("File created: " + pwFile.getName());
                        //Write the userPW to the file
                        FileWriter FWriter = new FileWriter(data);
                        FWriter.write(content.toString());
                        FWriter.close();
                    }

                    counter = counter + 1;

                  } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

            }
            catch(Exception e){
                upToDate = true;
            };
        }
        if(counter == 0){
            System.out.println("You do not have any new intent.");
        }
        else{
            System.out.println("You have " + counter + " new intent(s).");
        }
        
        System.out.println("You are up-to-date now.");
        System.out.println();
        
    }
    
    public static void listallProposal() throws ParseException, FileNotFoundException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = dtf.format(now).toString();
        Date date1 = sdf.parse(todayDate);
        Date date2;
        File dir = new File("UserData/"+ac+"/proposal/");
        File[] directoryListing = dir.listFiles();
        int counter = 0;
        
        //Remove out dated proposals
        if (directoryListing != null) {
          for (File child : directoryListing) {
            //System.out.println(child.getName().toString());
            date2 = sdf.parse(child.getName().toString());
            
            //compare with today's date to check if the file is older than today
            int result = date2.compareTo(date1);
            if(result < 0){
                child.delete();
                System.out.println("Removed the out-dated proposals");
            }
            
          }
        } else {
          //System.out.println("You do not have the data.");
        }
        
        //Get the proposal
        if (directoryListing != null) {
          for (File child : directoryListing) {
            //System.out.println(child.getName().toString());
            File[] directoryListing2 = child.listFiles();
            for (File child2 : directoryListing2){
                File[] directoryListing3 = child2.listFiles();
                
                for (File json : directoryListing3){
                    //System.out.println(json.getName().toString());
                    BufferedReader br = new BufferedReader(new FileReader(json));
                    TripProposal proposal = new Gson().fromJson(br, TripProposal.class);
                    System.out.println();
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("userID: " + proposal.getUserID());
                    System.out.println("title: " + proposal.getTitle());
                    System.out.println("date: " + proposal.getDate());
                    System.out.println("weather: " + proposal.getWeather());
                    System.out.println("location: " + proposal.getLocation());
                    System.out.println("messageID: " + proposal.getMessageID());
                    System.out.println("----------------------------------------------------------------");
                    counter = counter + 1;
                }
            }
          }
        } else {
          //System.out.println("You do not have the data.");
        }
        
        if(counter == 0){
            System.out.println("Do not have any proposal in the file.");
        }
        else{
            System.out.println("Listed " + counter + " proposal(s).");
        }
        
        
    }
    
    
    public static void searchProposal() throws ParseException, FileNotFoundException, IOException{
        
        BufferedReader reader = new BufferedReader( new InputStreamReader(System.in));
        System.out.print("Place: ");
        String location = reader.readLine();
        String locationF = location.replaceAll(" ", "_").toLowerCase();
        int counter = 0;
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = dtf.format(now).toString();
        Date date1 = sdf.parse(todayDate);
        Date date2;
        File dir = new File("UserData/"+ac+"/proposal/");
        File[] directoryListing = dir.listFiles();
        
        //Remove out dated proposals
        if (directoryListing != null) {
          for (File child : directoryListing) {
            //System.out.println(child.getName().toString());
            date2 = sdf.parse(child.getName().toString());
            
            //compare with today's date to check if the file is older than today
            int result = date2.compareTo(date1);
            if(result < 0){
                child.delete();
                System.out.println("Removed the out-dated proposals.");
            }
            
          }
        } else {
          //System.out.println("You do not have the data.");
        }
        
        //Get the proposal
        if (directoryListing != null) {
          for (File child : directoryListing) {
            //System.out.println(child.getName().toString());
            File[] directoryListing2 = child.listFiles();
            for (File child2 : directoryListing2){
                if(locationF.equals(child2.getName().toString()) != true){
                    continue;
                }
                
                File[] directoryListing3 = child2.listFiles();
                
                for (File json : directoryListing3){
                    //System.out.println(json.getName().toString());
                    BufferedReader br = new BufferedReader(new FileReader(json));
                    TripProposal proposal = new Gson().fromJson(br, TripProposal.class);
                    System.out.println();
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("userID: " + proposal.getUserID());
                    System.out.println("title: " + proposal.getTitle());
                    System.out.println("date: " + proposal.getDate());
                    System.out.println("weather: " + proposal.getWeather());
                    System.out.println("location: " + proposal.getLocation());
                    System.out.println("messageID: " + proposal.getMessageID());
                    System.out.println("----------------------------------------------------------------");
                    
                    counter = counter + 1;
                }
            }
          }
        } else {
          System.out.println("You do not have the data.");
        }
        
        if(counter == 0){
            System.out.println("Do not have the proposal on the location.");
        }
        else{
            System.out.println("Listed " + counter + " proposal(s) in the location:" + location );
        }
        
    }
    
    
    public static void listIntent() throws ParseException, FileNotFoundException{
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
        LocalDateTime now = LocalDateTime.now();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = dtf.format(now).toString();
        Date date1 = sdf.parse(todayDate);
        Date date2;
        File dir = new File("UserData/"+ac+"/intent/");
        File[] directoryListing = dir.listFiles();
        
        int counter = 0;
        
        //Remove out dated proposals
        if (directoryListing != null) {
          for (File child : directoryListing) {
            //System.out.println(child.getName().toString());
            date2 = sdf.parse(child.getName().toString());
            
            //compare with today's date to check if the file is older than today
            int result = date2.compareTo(date1);
            if(result < 0){
                child.delete();
                System.out.println("Removed the out-dated intents.");
            }
            
          }
        } else {
          //System.out.println("You do not have the data.");
        }
        
        //Get the proposal
        if (directoryListing != null) {
          for (File child : directoryListing) {
            //System.out.println(child.getName().toString());
            File[] directoryListing2 = child.listFiles();
            for (File child2 : directoryListing2){

                    //System.out.println(child2.getName().toString());
                    BufferedReader br = new BufferedReader(new FileReader(child2));
                    TripIntent intent = new Gson().fromJson(br, TripIntent.class);
                    System.out.println();
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("senderID: " + intent.getsenderID());
                    System.out.println("receiverID: " + intent.getreceiverID());
                    System.out.println("message: " + intent.getmessage());
                    System.out.println("date: " + intent.getDate());
                    System.out.println("messageID: " + intent.getmessageID());
                    System.out.println("----------------------------------------------------------------");
                
                    counter = counter + 1;
                    
            }
          }
        } else {
          System.out.println("You do not have the data.");
        }
        
        if(counter == 0){
            System.out.println("Yo do not have any intent received.");
        }else{
            System.out.println("Listed " + counter + " intent(s).");
        }
        
    }
    
}
