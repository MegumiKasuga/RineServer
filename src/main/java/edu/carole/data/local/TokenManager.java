package edu.carole.data.local;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.carole.Main;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class TokenManager<T> {

    @Getter
    private final List<T> values;
    private final String path;
    private boolean dirty = false;

    @Getter
    private final DataPack meta;

    public TokenManager(String path) {
        this.path = path;
        values = new ArrayList<T>();
        File file = new File(path);
        meta = new DataPack();
        if (!file.exists()) {
            createFile(path);
            return;
        }
        JsonArray array;
        try {
            JsonElement element = JsonParser.parseReader(new FileReader(path));
            JsonObject obj = element.getAsJsonObject();
            meta.fromJson(obj.getAsJsonObject("meta"));
            array = obj.get("values").getAsJsonArray();
            for (JsonElement jsonElement : array) {
                values.add(loadFromJson(jsonElement));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    public void updateFile() {
        try {
            JsonArray array = new JsonArray();
            values.forEach(v -> array.add(getJsonElement(v)));
            FileWriter writer = new FileWriter(path);
            JsonObject object = new JsonObject();
            object.add("values", array);
            object.add("meta", meta.getJson());
            writer.write(Main.gson.toJson(object));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToken(T token) {
        this.values.add(token);
        this.markDirty();
    }

    public boolean removeToken(T token) {
        boolean flag = this.values.remove(token);
        if (flag) this.markDirty();
        return flag;
    }

    public void forEach(Consumer<T> consumer) {
        values.forEach(consumer);
    }

    public abstract JsonElement getJsonElement(T token);

    public void onMetaSave(DataPack dataPack) {}

    public abstract T loadFromJson(JsonElement json);

    public void createFile(String path) {
        try {
            JsonObject json = new JsonObject();
            getDefaultSettings(meta);
            json.add("meta", meta.getJson());
            JsonArray values = new JsonArray();
            json.add("values", values);
            FileWriter writer = new FileWriter(path);
            writer.write(Main.gson.toJson(json));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void getDefaultSettings(DataPack meta);

    public void markDirty() {
        dirty = true;
    }

    public void saveToDisk() {
        if (!dirty) return;
        updateFile();
        this.dirty = false;
    }
}
