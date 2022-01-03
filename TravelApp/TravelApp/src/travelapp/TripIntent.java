/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travelapp;

/**
 *
 * @author Anthony
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anthony
 */

@XmlRootElement(name="TripIntent")
@XmlAccessorType(XmlAccessType.FIELD)
public class TripIntent {
    @XmlElement private String senderID;
    @XmlElement private String receiverID;
    @XmlElement private String message;
    @XmlElement private String date;
    @XmlElement private String messageID;
    //@XmlElement private String proposalID;
    
    
    public String getsenderID(){
        return senderID;
    }
    
    public void setsenderID(String theSenderID){
        this.senderID = theSenderID;
    }
    
    
    public String getDate(){
        return date;
    }
    
    public void setDate(String date){
        this.date = date;
    }
    
    
    
    public String getreceiverID(){
        return receiverID;
    }
    
    public void setreceiverID(String theReceiverID){
        this.receiverID = theReceiverID;
    }
    
    
    
    
    public String getmessageID(){
        return messageID;
    }
    
    public void setmessageID(String theMessageID){
        this.messageID = theMessageID;
    }
    
    
    
    
//    public String getproposalID(){
//        return proposalID;
//    }
//    
//    public void setproposalID(String theProposalID){
//        this.proposalID = theProposalID;
//    }
//    
    public String getmessage(){
        return message;
    }
    
    public void setMessage(String theMessage){
        this.message = theMessage;
    }
    

}