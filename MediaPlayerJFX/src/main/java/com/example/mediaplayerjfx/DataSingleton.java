package com.example.mediaplayerjfx;

import java.io.File;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();
    private File file;
    private DataSingleton() {}
    public static DataSingleton getInstance() { return instance; }
    public File getFile() { return file; }
    public void setFile(File file) { this.file = file; }
}
