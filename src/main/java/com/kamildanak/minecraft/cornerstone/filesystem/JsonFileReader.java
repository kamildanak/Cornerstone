package com.kamildanak.minecraft.cornerstone.filesystem;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonFileReader extends FileReader {
    public JsonFileReader(File file) throws FileNotFoundException {
        super(file);
    }

    public JsonObject readJson() throws IOException {
        JsonParser jsonParser = new JsonParser();
        Object obj = jsonParser.parse(this);
        return (JsonObject) obj;
    }
}
