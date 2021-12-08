/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package travel;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
/**
 *
 * @author Anthony
 */
public class FanoutExchangeBinder {
    
    private static enum EXCHANGE_TYPE {DIRECT, FANOUT, TOPIC, HEADERS};
    
    public void createExchangeAndQueue(String exchange, String queue){
        try{
            Connection connection = RabbitMQConnection.getConnection();
            if(connection != null){
                Channel channel = connection.createChannel();
                //create a direct exchange with name of String exchange
                channel.exchangeDeclare(exchange, EXCHANGE_TYPE.FANOUT.toString().toLowerCase());
                //create a queue with name of String queue for the user
                channel.queueDeclare(queue, true, false, false, null);
                //bind the queue to the direct exchange for the user and use the same String as the routing key
                channel.queueBind(queue, exchange, queue);
                
                channel.close();
                connection.close();
            }
        }catch(Exception e){
            
        }
    }
    
}
