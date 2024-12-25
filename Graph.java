package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import test.TopicManagerSingleton.TopicManager;

public class Graph extends ArrayList<Node>{
    private HashMap<String,Node> nodes;//stores the unique keys to nodes
    public Graph(){//defualt constructor to initialize the HashMap
        nodes = new HashMap<String,Node>();
    }
    public boolean hasCycles() {//checking if there is any cycles.
        List<Node> visited= new ArrayList<>();
        for(Node n: this) {
            if(n.hasCycles())//using Node method to check cycles.
                return true;
        }
        return false;


    }
    public void createFromTopics(){//method to create graph from topics.
        TopicManager graph=TopicManagerSingleton.get();//getting TopicManger instance.
        for (Topic t: graph.getTopics()) {
            if(!nodes.containsKey("T"+ t.name));{//adding new node if topic doesnt exist
                nodes.put("T"+ t.name, new Node("T"+t.name));
                this.add(nodes.get("T"+ t.name));
            }
            for(Agent a:t.subs){
                if(!nodes.containsKey("A"+a.getName())){//adding new node if agent doesnt exist
                    nodes.put("A"+a.getName(), new Node("A"+a.getName()));
                    this.add(nodes.get("A"+a.getName()));
                }
                nodes.put(t.name,new Node("T"+t.name));
            }
        }
        for(Topic t: graph.getTopics()){//
            Node topic=nodes.get("T"+t.name);
            for(Agent a:t.subs){
                Node sub=nodes.get("A"+a.getName());//add Node from topic to subscribe.
                topic.addEdge(sub);
            }
            for(Agent a:t.pubs){
                Node pub=nodes.get("A"+a.getName());//add Node from publisher to topic.
                pub.addEdge(topic);
            }
        }

    }    

    
}
