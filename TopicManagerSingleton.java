
package test;
import java.util.HashMap;
import java.util.Collection;


public class TopicManagerSingleton {
    private final static TopicManager instance=new TopicManager();
    public static TopicManager get(){
        return instance;
    }
    public static class TopicManager{
        private HashMap<String,Topic> mapi;
        private TopicManager(){
            mapi=new HashMap<String,Topic>();
        }
        public Topic getTopic(String name){
            if(mapi.containsKey(name)){//cheking if name exist in hashmap
                return (mapi.get(name));
            }
            Topic newtopic=new Topic(name);
            mapi.put(name,newtopic);//adding topic to the map.
            return mapi.get(name);
        }
        public Collection<Topic> getTopics(){
            return mapi.values();
        }
        public void clear(){
            mapi.clear();
        }
    }
}

