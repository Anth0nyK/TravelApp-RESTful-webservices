/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author Anthony
 */
public class Todo {
    private String summary;
    private String description;
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary){
        this.summary = summary;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
