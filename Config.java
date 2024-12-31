package test;

import java.io.FileNotFoundException;

public interface Config {
    void create() throws FileNotFoundException;
    String getName();
    int getVersion();
    void close();
}
