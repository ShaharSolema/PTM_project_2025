package test.configs;

import test.graph.Agent;
import test.graph.Message;
import test.graph.Topic;
import test.graph.TopicManagerSingleton;
import test.graph.TopicManagerSingleton.TopicManager;

public class PlusAgent implements Agent {
    private Double x=0.0;
    private Double y=0.0;
    private Topic first;
    private Topic second;
    private Topic out;
    public PlusAgent(String[]subs,String[] pubs) {
        TopicManager manager= TopicManagerSingleton.get();
        first=manager.getTopic(subs[0]);
        second=manager.getTopic(subs[1]);
        out=manager.getTopic(pubs[0]);
        first.subscribe(this);//subscribe to subs
        second.subscribe(this);
        out.addPublisher(this);
    }

    @Override
    public String getName() {
        return "PlusAgent";
    }

    @Override
    public void reset() {
        x=0.0;
        y=0.0;
    }

    @Override
    public void callback(String topic, Message msg) {
        try{
            if(topic.equals(first.name))//cheking if topic of the messeage is equals to the name of this topic
                x= msg.asDouble;
            else if (topic.equals(second.name)) {
                y= msg.asDouble;
            }
            if(x!=null && y!=null)//checking if x,y are legal numbers.
                out.publish(new Message(x+y));

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        x=0.0;
        y=0.0;
        first.unsubscribe(this);
        second.unsubscribe(this);
    }
}
