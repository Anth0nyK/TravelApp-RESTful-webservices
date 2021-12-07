/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travel;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anthony
 */
@XmlRootElement
public class coordinates {
    @XmlElement private BigDecimal latitude;
    @XmlElement private BigDecimal longitude;
    
    public BigDecimal getlat(){
        return latitude;
    }
    
    public void setlat(BigDecimal theLatitude){
        this.latitude = theLatitude;
    }
    
    public BigDecimal getlong(){
        return longitude;
    }
    
    public void setlong(BigDecimal thelongitude){
        this.longitude = thelongitude;
    }
    
    public coordinates(){
        
    }
}
