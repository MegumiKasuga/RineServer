package edu.carole.packet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.Result;
import edu.carole.data.rec.Login;
import edu.carole.data.rec.Register;
import lombok.extern.java.Log;

import java.util.UUID;

public class LoginPacket implements Packet<Login> {

    private int stateCode = 200;
    @Override
    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public JsonObject getReplyJson(Result<Login> data) {
        JsonObject reply = new JsonObject();
        reply.addProperty("state_code", data.getStateCode());
        JsonObject content = new JsonObject();
        content.addProperty("msg",
                data.getDataPack().getString("msg"));
        if (!data.isSuccess()) {
            content.addProperty("cause",
                    data.getDataPack().getString("cause"));
        }
        reply.add("content", content);
        return reply;
    }

    @Override
    public Login fromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject content = jsonObject.get("content").getAsJsonObject();
        UUID id = UUID.fromString(content.get("id").getAsString());
        int token = content.get("token").getAsInt();
        if (content.has("name")) {
            String name = content.get("name").getAsString();
            return new Register(id, token, name);
        }
        return new Login(id, token);
    }
}
