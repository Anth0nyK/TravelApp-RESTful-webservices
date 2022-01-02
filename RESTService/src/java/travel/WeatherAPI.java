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
    
    public String getWeather(String location, String date){
        String output = null;
        //String coord = "51.509865,-0.118092";
        //String date = "2022-1-1"; 
        //String coord = lat + "," + lon; 
        
        String weatherDay = null;
        String tempDay = null;
        String weatherNight = null;
        String tempNight = null;
        
        try{
            URL url = new URL("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=dcf1d98034d14a32b35202955213012&q=" + location + "&format=json&num_of_days=1&date=" + date + "&cc=no&mca=no&fx24=no&tp=6");
            //URL url = new URL("http://api.worldweatheronline.com/premium/v1/weather.ashx?key=dcf1d98034d14a32b35202955213012&q=" + coord + "&format=json&date=" + date + "&cc=no&mca=no&fx24=no&tp=6");
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
            weatherDay = jsonobj.getJSONObject("data").getJSONArray("weather").getJSONObject(0).getJSONArray("hourly").getJSONObject(2).getJSONArray("weatherDesc").getJSONObject(0).getString("value");
            tempDay = jsonobj.getJSONObject("data").getJSONArray("weather").getJSONObject(0).getJSONArray("hourly").getJSONObject(2).getString("tempC");
            weatherNight = jsonobj.getJSONObject("data").getJSONArray("weather").getJSONObject(0).getJSONArray("hourly").getJSONObject(3).getJSONArray("weatherDesc").getJSONObject(0).getString("value");
            tempNight = jsonobj.getJSONObject("data").getJSONArray("weather").getJSONObject(0).getJSONArray("hourly").getJSONObject(3).getString("tempC");
            

        }
        catch(Exception e){
            return null;
        };
       
            
        output = "Day: " + tempDay + "°C" + ", " + weatherDay + ", Night: " + tempNight + "°C" + ", " + weatherNight ;
        
        return output;
    }
    
}
