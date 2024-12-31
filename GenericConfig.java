package test;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GenericConfig implements Config {
    private String fileName;
    private List<String> temp;//using to read the file
    private List<Agent> agents;//to keep all the agents.
    public GenericConfig() {//defualt constructor.
        temp = new ArrayList<String>();
        agents = new ArrayList<>();
    }
    public void setConfFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void create()  {
        try{
            Scanner s=new Scanner(new FileReader(fileName));//new scanner-option to read the file
            while(s.hasNextLine()) {//checking every row
                temp.add(s.nextLine());//adding every row to a list of strings
            }
            s.close();
            if(temp.size()%3==0) {//cheking if the file has correct rows
                for (int i = 0; i < temp.size(); i+=3) {
                    String agentClassName = temp.get(i);//getting the class of agent(Plus or Inc) from row 1
                    String[] inputtopic = temp.get(i + 1).split(",");//getting the subs from row 2
                    String[] outputtopic = temp.get(i + 2).split(",");//getting the publish from row 3

                    Class<?> agentclass = Class.forName(agentClassName);//using the agentclassname to load the class by its name.
                    Agent agent = (Agent) agentclass.getConstructor(String[].class, String[].class).newInstance((Object)inputtopic, (Object)outputtopic);//using agent constructor to create new object of agent by sending array of strings to constructor.
                    ParallelAgent parallelAgent = new ParallelAgent(agent);//create object of ParalleAgent
                    agents.add(parallelAgent);//adding him to the agents list.

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }




    }

    @Override
    public String getName() {
        return "GenericConfig";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void close() {
        for(Agent a:agents) {//using close method of every agent.
            a.close();
        }

    }
}
