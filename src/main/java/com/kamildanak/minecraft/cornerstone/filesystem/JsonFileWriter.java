package com.kamildanak.minecraft.cornerstone.filesystem;

import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFileWriter extends FileWriter {

    public JsonFileWriter(File file) throws IOException {
        super(file);
    }

    public void write(JsonObject obj) throws IOException {
        String str = obj.toString();
        this.write(str);
    }
}
