package test.configs;

import java.util.function.BinaryOperator;

import test.graph.Agent;
import test.graph.Message;
import test.graph.Topic;
import test.graph.TopicManagerSingleton;
import test.graph.TopicManagerSingleton.TopicManager;

public class BinOpAgent implements Agent {
    private String agentName;//name of agent
    private Topic first; //input topic
    private Topic second;//input topic
    private Topic out;//output input
    private BinaryOperator<Double> operator;//lambda operator
    private Double left=null;//double for lambada operator
    private Double right=null;//double for lambada operator
    // constructor
    public BinOpAgent(String agentName, String atopic, String btopic, String out, BinaryOperator<Double> combiner) {//constructor
        this.agentName = agentName;
        operator = combiner;
        TopicManager manager= TopicManagerSingleton.get();//get the topic manager instance
        this.first=manager.getTopic(atopic);//create the required topics.
        this.second=manager.getTopic(btopic);
        this.out=manager.getTopic(out);
        this.first.subscribe(this);//adding first to the subs array
        this.second.subscribe(this);//adding second to the subs array
        this.out.addPublisher(this);//adding out to the publisher array

    }
//    name getter
    @Override
    public String getName() {
        return agentName;
    }
//  reset values to 0.0
    @Override
    public void reset(){
        left = 0.0;
        right = 0.0;
    }
//  callback method
    @Override
    public void callback(String topic, Message msg) {
        if(Double.isNaN(msg.asDouble))  return;//if the value is not a double.
        if(topic.equals(first.name))//update the variable (left or right) based on the topic
            left= msg.asDouble;
        else if(topic.equals(second.name))
            right= msg.asDouble;
        if(left!=null&&right!=null) {//checking if both of the numbers is not a null/
         double result = operator.apply(left, right);//using the lambada function and getting double result.
         out.publish(new Message(result));//publish the result.
        }

    }

    @Override
    public void close() {//sets all the fields to null
        agentName = null;
        first = null;
        second = null;
        out = null;
        operator = null;
        left = null;
        right = null;


    }


}
