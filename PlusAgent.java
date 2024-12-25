package test;

import test.TopicManagerSingleton.TopicManager;

public class PlusAgent extends BinOpAgent {
    private Number x=0.0;
    private Number y=0.0;
    public PlusAgent(String[]subs,String[] pubs) {
        super("PlusAgent",subs[0],subs[1],pubs[0],(x,y)->x+y);
    }

}
