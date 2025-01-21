package test.configs;

import test.graph.Agent;
import test.graph.Message;

public class ParallelAgent implements Agent {
    private Agent a;

    public ParallelAgent(Agent agent) {
        this.a = agent;
    }

    @Override
    public String getName() {
        return a.getName() ;
    }

    @Override
    public void reset() {
        a.reset();
    }

    @Override
    public void callback(String topic, Message msg) {
        a.callback(topic, msg);
    }

    @Override
    public void close() {
        a.close();
    }
}
