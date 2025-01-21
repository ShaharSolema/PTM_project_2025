package test.configs;

import test.graph.Agent;
import test.graph.Message;
import test.graph.Topic;
import test.graph.TopicManagerSingleton;
import test.graph.TopicManagerSingleton.TopicManager;

public class IncAgent implements Agent {
    private Double x=0.0;
    private final Topic sub;
    private final Topic pub;
    public IncAgent(String[]subs,String[]pubs) {
        TopicManager manager= TopicManagerSingleton.get();
        sub=manager.getTopic(subs[0]);
        pub=manager.getTopic(pubs[0]);
        this.sub.subscribe(this);
        this.pub.addPublisher(this);
    }

    @Override
    public String getName() {
        return "IncAgent";
    }

    @Override
    public void reset() {
        x=0.0;
    }

    @Override
    public void callback(String topic, Message msg) {
        try {
            if (topic.equals(sub.name))//cheking if topic of the messeage is equals to the name of this topic
                x = msg.asDouble;
            if (x != null) {//checking if x is a legal number
                pub.publish(new Message(x + 1));//publish x plus 1.
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        sub.unsubscribe(this);
    }
}
