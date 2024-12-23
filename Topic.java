package test;
//package project_biu.graph;


import java.util.ArrayList;
import java.util.List;

public class Topic {
    public final String name;
    public List<Agent> subs;
    public List<Agent> pubs;
    Topic(String name){
        this.name=new String(name);
        subs=new ArrayList<Agent>();
        pubs=new ArrayList<Agent>();
    }

    public void subscribe(Agent a){
        subs.add(a);
    }
    public void unsubscribe(Agent a){
        subs.remove(a);
    }

    public void publish(Message m){
        for(Agent a : subs){
            a.callback(this.name,m);
        }
    }

    public void addPublisher(Agent a){
        pubs.add(a);
    }

    public void removePublisher(Agent a){
        pubs.remove(a);
    }


}