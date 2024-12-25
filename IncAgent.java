package test;

import test.TopicManagerSingleton.TopicManager;

public class IncAgent extends PlusAgent {
    public IncAgent(String[]subs,String[]pubs) {
        super(new String[]{subs[0],subs[0]},pubs);
    }
}
