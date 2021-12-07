/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anthony
 */

@XmlRootElement(name="proposalFromClient")
@XmlAccessorType(XmlAccessType.FIELD)
public class proposalFromClient {
    @XmlElement private String userID;
    @XmlElement private coordinates place;
    @XmlElement private String date;
    
    
    
    public String getUserID(){
        return userID;
    }
    
    public void setUserID(String theUserID){
        this.userID = theUserID;
    }
    
    
    
    public coordinates getCoord(){
        return place;
    }
    
    public void setCoord(coordinates theCoord){
        this.place = theCoord;
    }
    
    
    
    public String getDate(){
        return date;
    }
    
    public void setDate(String theDate){
        this.date = theDate;
    }
 
    
    
    public proposalFromClient(){
        
    }
}
