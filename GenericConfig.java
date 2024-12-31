package test;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GenericConfig implements Config {
    private String fileName;
    List<String> temp;
    List<Agent> agents;
    public GenericConfig() {
        temp = new ArrayList<String>();
        agents = new ArrayList<>();
    }
    public void setConfFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void create()  {
        try( Scanner s=new Scanner(new FileReader(fileName))){
            while(s.hasNextLine()) {
                temp.add(s.nextLine());
            }
            if(temp.size()%3==0) {
                for (int i = 0; i < temp.size(); i++) {
                    String agentClassName = temp.get(i);
                    String[] inputtopic = temp.get(i + 1).split(",");
                    String[] outputtopic = temp.get(i + 2).split(",");

                    Class<?> agentclass = Class.forName(agentClassName);
                    Agent agent = (Agent) agentclass.getConstructor(String[].class, String[].class).newInstance((Object)inputtopic, (Object)outputtopic);
                    ParallelAgent parallelAgent = new ParallelAgent(agent);
                    agents.add(parallelAgent);

                }
            }
            s.close();
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
        for(Agent a:agents) {
            a.close();
        }

    }
}
