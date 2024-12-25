package test;

import java.util.ArrayList;
import java.util.List;


public class Node {
    private String name;
    private List<Node> edges;
    private Message msg;
    public Node(String name) { /// Node constructor
        this.name = new String(name);
        this.edges = new ArrayList<Node>();
        this.msg = null;
    }
    public void SetName(String name) { /// name setter
        this.name = new String(name);
    }
    public String getName() { /// name getter
        return name;
    }
    public List<Node> getEdges() { /// List getter
        return edges;
    }
    public void setEdges(List<Node> edges) { /// List setter
        this.edges = edges;
    }
    public Message getMsg() { /// message getter
        return msg;
    }
    public void setMsg(Message msg) {/// message setter
        this.msg=msg;
    }
    public void addEdge(Node n){/// add Node to List
        edges.add(n);
    }
    public boolean hasCycles(){/// checking if there is any cycles
        List<Node> hasvisit=new ArrayList<Node>();///new Array of visited Nodes
        return (helpCycles(this,hasvisit));
    }
    private boolean helpCycles(Node n,List<Node> hasvisit){/// checking by recursion if there is any cycles
        if(hasvisit.contains(n)){/// if node is exist in hasvisit
            return true;
        }
        hasvisit.add(n);///if Node doesn't exist , adding him to visited array
        for(Node nn : n.getEdges()) {/// checking the neighborhoods of Node.
            if(helpCycles(nn,hasvisit))/// recursion.
                return true;
        }
        hasvisit.remove(n);///if we found that we don't have a cycle from this Node , we remove him from the visited array.
        return false;
    }





}