package test;

import java.util.Date;

public class Message {
    public final byte[] data;
    public final String asText;
    public final double asDouble;
    public final Date date;
    public Message(byte[] by) {
        this.data=by.clone();
        this.asText=new String(by);
        this.asDouble=parseDouble(asText);
        this.date=new Date();
    }
    public Message(String by) {
        this(by.getBytes());
    }
    public Message(double s) {
        this(Double.toString(s));
    }
    private static double parseDouble(String s) {
        try {
            return Double.parseDouble(s);
        }
        catch (NumberFormatException e) {
            return Double.NaN;
        }
    }



}