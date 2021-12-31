/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

/**
 *
 * @author Anthony
 */
public class WeatherAPI {
    
    public String getWeather(){
        String output = null;
        String coord = "51.509865,-0.118092";
        
        try{
            URL url = new URL("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=dcf1d98034d14a32b35202955213012&q=" + coord + "&format=json&date=2021-12-31&cc=no&mca=no&fx24=no&tp=6");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
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

            //Turn the string into JSONObject for further action
            JSONObject jsonobj = new JSONObject(content.toString());
            //Get the needed part, the UUID, from the json in String format
            //output = jsonobj.getJSONObject("result").getJSONObject("random").getJSONArray("data").getString(0);
            output = jsonobj.getJSONObject("data").getJSONArray("weather").getJSONObject(0).getJSONArray("hourly").getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).getString("value");
            
        }
        catch(IOException e){
        };
        
        
        return output;
    }
    
}
