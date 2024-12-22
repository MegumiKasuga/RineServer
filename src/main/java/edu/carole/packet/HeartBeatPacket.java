package edu.carole.packet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.Result;
import edu.carole.data.rec.HeartBeat;

public class HeartBeatPacket implements Packet<HeartBeat> {

    private int stateCode = 200;
    @Override
    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public JsonObject getReplyJson(Result<HeartBeat> data) {
        JsonObject reply = new JsonObject();
        setStateCode(data.getStateCode());
        reply.addProperty("state_code", stateCode);
        JsonObject content = new JsonObject();
        content.addProperty("msg",
                data.getDataPack().getString("msg"));
        if (stateCode != 200) {
            content.addProperty("cause",
                    data.getDataPack().getString("cause"));
        }
        return reply;
    }

    @Override
    public HeartBeat fromJson(JsonElement json) {
        JsonObject content = json.getAsJsonObject().get("content").getAsJsonObject();
        return new HeartBeat(content.get("token").getAsInt());
    }
}
