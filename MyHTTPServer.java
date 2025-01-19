package test;

import test.server.HTTPServer;
import test.servlets.Servlet;

import java.util.LinkedList;
import java.util.Queue;


public class MyHTTPServer extends Thread implements HTTPServer {
    int port;
    int nThreads;
    Queue<String> requests;

    public MyHTTPServer(int port,int nThreads){
        this.port = port;
        this.nThreads = nThreads;
        requests = new LinkedList<String>();

    }

    public void addServlet(String httpCommanmd, String uri, Servlet s){
    }

    public void removeServlet(String httpCommanmd, String uri){
    }

    public void run(){
    }

    public void close(){
    }

}
