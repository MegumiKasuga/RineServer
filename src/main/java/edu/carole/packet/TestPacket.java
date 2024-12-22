package edu.carole.packet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.Result;
import edu.carole.data.rec.HeartBeat;
import edu.carole.data.rec.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestPacket implements Packet<Test> {

    private int stateCode = 200;

    public TestPacket() {}

    @Override
    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public JsonObject getReplyJson(Result<Test> data) {
        JsonObject obj = new JsonObject();
        obj.addProperty("state_code", stateCode);
        if (stateCode == 200) {
            JsonObject contentObj = new JsonObject();
            contentObj.addProperty("msg", "Welcome!");
            contentObj.addProperty("time", System.currentTimeMillis());
            obj.add("content", contentObj);
            return obj;
        }
        JsonObject contentObj = new JsonObject();
        contentObj.addProperty("msg", "Bad Request");
        contentObj.addProperty("time", System.currentTimeMillis());
        obj.add("content", contentObj);
        return obj;
    }

    @Override
    public Test fromJson(JsonElement json) {
        if (!json.isJsonObject()) {
            stateCode = 400;
            return null;
        }
        JsonObject jsonObject = json.getAsJsonObject();
        jsonObject.get("service_code").getAsInt();
        if (!jsonObject.has("content")) {
            stateCode = 400;
            return null;
        }
        JsonElement element = jsonObject.get("content");
        if (!element.isJsonObject()) {
            stateCode = 400;
            return null;
        }
        JsonObject contentObj = element.getAsJsonObject();
        InetAddress addr;
        try {
            addr = InetAddress.getByName(contentObj.get("host").getAsString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            stateCode = 400;
            return null;
        }
        return new Test(
                contentObj.get("msg").getAsString(),
                addr,
                contentObj.get("mac").getAsString(),
                contentObj.get("id").getAsString(),
                contentObj.get("time").getAsLong()
        );
    }
}
