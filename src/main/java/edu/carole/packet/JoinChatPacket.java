package edu.carole.packet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.Result;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.JoinChat;

public class JoinChatPacket implements Packet<JoinChat> {
    @Override
    public void setStateCode(int stateCode) {

    }

    @Override
    public JsonObject getReplyJson(Result<JoinChat> data) {
        JsonObject reply = new JsonObject();
        DataPack pack = data.getDataPack();
        reply.addProperty("state_code", data.getStateCode());
        JsonObject contentObj = new JsonObject();
        reply.add("content", contentObj);
        contentObj.addProperty("msg", pack.getString("msg"));
        if (data.getStateCode() != 200) {
            contentObj.addProperty("cause", pack.getString("cause"));
        } else {
            contentObj.addProperty("chat", pack.getNumber("cause"));
        }
        return reply;
    }

    @Override
    public JoinChat fromJson(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        JsonObject content = obj.get("content").getAsJsonObject();
        return new JoinChat(content.get("chat").getAsLong(), content.get("token").getAsInt());
    }
}
