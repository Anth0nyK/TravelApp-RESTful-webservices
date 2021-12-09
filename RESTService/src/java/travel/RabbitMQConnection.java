/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travel;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author Anthony
 */
public class RabbitMQConnection {
    
    public static Connection getConnection(){
        Connection connection = null;
        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("guest");
            factory.setPassword("guest");
            connection = factory.newConnection();
        }
        catch(Exception e){
            //e.printStackTrace();
        }
        
        
        return connection;
    }
    
    
}
