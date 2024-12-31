package test;

import test.TopicManagerSingleton.TopicManager;

public class IncAgent implements Agent {
    private Double x=0.0;
    private final Topic sub;
    private final Topic pub;
    public IncAgent(String[]subs,String[]pubs) {
        TopicManager manager=TopicManagerSingleton.get();
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
            if (topic.equals(sub.name))//update the variable (left or right) based on the topic
                x = msg.asDouble;
            if (x != null) {//checking if both of the numbers is not a null/
                pub.publish(new Message(x + 1));//publish the result.
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
