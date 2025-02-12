package test.server;


import test.servlets.Servlet;

public interface HTTPServer extends Runnable{
    public void addServlet(String httpCommanmd, String uri, Servlet s);
    public void removeServlet(String httpCommanmd, String uri);
    public void start();
    public void close();
}
