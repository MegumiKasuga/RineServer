package edu.carole.data.local;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;

public class DataPack {

    private final HashMap<String, String> stringData;
    private final HashMap<String, Boolean> booleanData;
    private final HashMap<String, Number> numberData;

    public DataPack() {
        stringData = new HashMap<>();
        booleanData = new HashMap<>();
        numberData = new HashMap<>();
    }

    public String getString(String key) {
        return stringData.getOrDefault(key, "");
    }

    public Number getNumber(String key) {
        return numberData.getOrDefault(key, 0);
    }

    public boolean getBoolean(String key) {
        return booleanData.getOrDefault(key, false);
    }

    public boolean containsString(final String key) {
        return stringData.containsKey(key);
    }

    public boolean containsNumber(final String key) {
        return numberData.containsKey(key);
    }

    public boolean containsBoolean(final String key) {
        return booleanData.containsKey(key);
    }

    public boolean containsKey(final String key) {
        return containsBoolean(key) || containsNumber(key) || containsString(key);
    }

    public void add(String key, String value) {
        stringData.put(key, value);
    }

    public void add(String key, boolean value) {
        booleanData.put(key, value);
    }

    public void add(String key, Number value) {
        numberData.put(key, value);
    }

    public JsonObject getJson() {
        JsonObject json = new JsonObject();
        stringData.forEach(json::addProperty);
        booleanData.forEach(json::addProperty);
        numberData.forEach(json::addProperty);
        return json;
    }

    public void fromJson(JsonObject json) {
        stringData.clear();
        booleanData.clear();
        numberData.clear();
        json.entrySet().forEach(entry -> {
            String strValue = entry.getValue().getAsString();
            try {
                if (strValue.equals("true") || strValue.equals("false")) {
                    boolean b = entry.getValue().getAsBoolean();
                    booleanData.put(entry.getKey(), b);
                } else {
                    try {
                        Number num = entry.getValue().getAsNumber();
                        numberData.put(entry.getKey(), num);
                    } catch (Exception e2) {
                        stringData.put(entry.getKey(), strValue);
                    }
                }
            } catch (Exception e) {
                stringData.put(entry.getKey(), strValue);
            }
        });
    }
}
