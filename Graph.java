package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import test.TopicManagerSingleton.TopicManager;

public class Graph extends ArrayList<Node>{
    private HashMap<String,Node> nodes;
    public Graph(){
        nodes = new HashMap<String,Node>();
    }
    public boolean hasCycles() {
        List<Node> visited= new ArrayList<>();
        for(Node n: this) {
            if(n.hasCycles())
                return true;
        }
        return false;


    }
    public void createFromTopics(){
        TopicManager graph=TopicManagerSingleton.get();
        for (Topic t: graph.getTopics()) {
            if(!nodes.containsKey("T"+ t.name));{
                nodes.put("T"+ t.name, new Node("T"+t.name));
                this.add(nodes.get("T"+ t.name));
            }
            for(Agent a:t.subs){
                if(!nodes.containsKey("A"+a.getName())){
                    nodes.put("A"+a.getName(), new Node("A"+a.getName()));
                    this.add(nodes.get("A"+a.getName()));
                }
                nodes.put(t.name,new Node("T"+t.name));
            }
        }
        for(Topic t: graph.getTopics()){
            Node topic=nodes.get("T"+t.name);
            for(Agent a:t.subs){
                Node sub=nodes.get("A"+a.getName());
                topic.addEdge(sub);
            }
            for(Agent a:t.pubs){
                Node pub=nodes.get("A"+a.getName());
                pub.addEdge(topic);
            }
        }

    }    

    
}
