/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONObject;

/**
 *
 * @author Anthony
 */
public class RandomAPI {

        public String generateUUID() throws MalformedURLException, ProtocolException, UnsupportedEncodingException, IOException{
        String UUID = null;
        
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
        catch(Exception e){
            boolean hasLine = false;
            try {
            File myObj = new File("UUIDBackup/", "UUIDBackup" + ".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String UUIDinFile = myReader.nextLine();
              //System.out.println(data);
              UUID = UUIDinFile;
              hasLine = true;
              break;
            }
            myReader.close();
            } catch (FileNotFoundException x) {
                //return "Receiver does not exist. Please double check the username";
                //return message = "{\"error\":\"Receiver does not exist. Please double check the username.\"}";
            }
            
            if(hasLine == true){
                File path = new File("UUIDBackup/UUIDBackup.txt");
                Scanner scanner = new Scanner(path);
                ArrayList<String> coll = new ArrayList<String>();
                scanner.nextLine();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    coll.add(line);
                }

                scanner.close();

                FileWriter writer = new FileWriter(path);
                for (String line : coll) {
                    writer.write(line);
                    writer.write(System.getProperty( "line.separator" ));
                }

                writer.close();
            }
            
            
            
            if(hasLine == false){
                return null;
            }
            return UUID;
        };
        
        
        return UUID;
    }
        
}
