package test;

import java.util.function.BinaryOperator;
import java.util.function.Function;

import test.TopicManagerSingleton.TopicManager;

public class BinOpAgent implements Agent {
    private String agentName;
    private Topic first;
    private Topic second;
    private Topic out;
    private BinaryOperator<Double> operator;
    private Double left=0.0;
    private Double right=0.0;
    // constructor
    public BinOpAgent(String agentName, String atopic, String btopic, String out, BinaryOperator<Double> combiner) {
        this.agentName = agentName;
        operator = combiner;
        TopicManager manager=TopicManagerSingleton.get();//get the topic manager instance
        this.first=manager.getTopic(atopic);//create the required topics.
        this.second=manager.getTopic(btopic);
        this.out=manager.getTopic(out);

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
        if(Double.isNaN(msg.asDouble)) return;//if the value is not a double.
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
