package edu.carole.packet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.Result;
import edu.carole.data.rec.Login;

import java.util.UUID;

public class QuitPacket implements Packet<Login> {
    @Override
    public void setStateCode(int stateCode) {

    }

    @Override
    public JsonObject getReplyJson(Result<Login> data) {
        JsonObject object = new JsonObject();
        object.addProperty("state_code", data.getStateCode());
        JsonObject content = new JsonObject();
        content.addProperty("msg", "success");
        object.add("content", content);
        return object;
    }

    @Override
    public Login fromJson(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        JsonObject content = obj.get("content").getAsJsonObject();
        UUID id = UUID.fromString(content.get("id").getAsString());
        int token = content.get("token").getAsInt();
        return new Login(id, token);
    }
}
