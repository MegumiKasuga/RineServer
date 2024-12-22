package edu.carole;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;

import java.io.*;

public class Config {

    @Getter
    private String storagePath;

    @Getter
    private short port;

    private String networkId;

    @Getter
    private String serverName;
    @Getter
    private int delayMs;
    @Getter
    private final Gson gson;

    public Config(Gson gson, String path) {
        this.gson = gson;
        JsonElement pathJson;
        try {
            pathJson = JsonParser.parseReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            try {
                System.out.println(path + " not found!, created!");
                createFile(path);
                System.exit(1);
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
                System.exit(1);
                return;
            }
        }
        JsonObject json = pathJson.getAsJsonObject();
        this.storagePath = json.get("storagePath").getAsString();
        this.port = json.get("port").getAsShort();
        this.networkId = json.get("networkId").getAsString();
        this.serverName = json.get("serverName").getAsString();
        this.delayMs = json.get("delayMs").getAsInt();
    }

    public Long getNetworkId() {
        return Long.parseUnsignedLong(networkId, 16);
    }

    private void createFile(String path) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("storagePath", "./");
        json.addProperty("port",9998);
        json.addProperty("networkId","your-network-id(hex-str)");
        json.addProperty("serverName","rine-server");
        json.addProperty("delayMs", 60000);
        String jsonString = gson.toJson(json);
        File file = new File(path);
        FileWriter writer = new FileWriter(file);
        writer.write(jsonString);
        writer.close();
    }
}
