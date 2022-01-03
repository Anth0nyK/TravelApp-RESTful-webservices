/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelapp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anthony
 */


public class TripProposal {
    private String userID;
    //@XmlElement private coordinates location;
    private String title;
    private String location;
    private String messageID;
    private String date;
    private String weather;
    
    
    public String getUserID(){
        return userID;
    }
    
    public void setUserID(String theUserID){
        this.userID = theUserID;
    }
    
    
    public String getTitle(){
        return title;
    }
    
    public void setTitle(String theTitle){
        this.title = theTitle;
    }
    
    
    public String getLocation(){
        return location;
    }
    
    public void setLocation(String location){
        this.location = location;
    }
    
//    public coordinates getCoord(){
//        return location;
//    }
//    
//    public void setCoord(coordinates theCoord){
//        this.location = theCoord;
//    }
//    
    
    
    public String getMessageID(){
        return messageID;
    }
    
    public void setMessageID(String theMessageID){
        this.messageID = theMessageID;
    }
    
    
    
    public String getDate(){
        return date;
    }
    
    
    
    public void setDate(String theDate){
        this.date = theDate;
    }
 
    
    public String getWeather(){
        return weather;
    }
    
    public void setWeather(String theWeather){
        this.weather = theWeather;
    }
    
    
    public TripProposal(){
        
    }
}
