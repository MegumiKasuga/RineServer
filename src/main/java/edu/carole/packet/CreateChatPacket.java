package edu.carole.packet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.Result;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.CreateChat;

import java.util.ArrayList;
import java.util.UUID;

public class CreateChatPacket implements Packet<CreateChat> {
    @Override
    public void setStateCode(int stateCode) {

    }

    @Override
    public JsonObject getReplyJson(Result<CreateChat> data) {
        DataPack pack = data.getDataPack();
        JsonObject reply = new JsonObject();
        reply.addProperty("state_code", data.getStateCode());
        JsonObject contentObj = new JsonObject();
        reply.add("content", contentObj);
        contentObj.addProperty("msg", pack.getString("msg"));
        if (data.getStateCode() != 200) {
            contentObj.addProperty("cause", pack.getString("cause"));
            return reply;
        }
        contentObj.addProperty("chat", pack.getNumber("chat"));
        return reply;
    }

    @Override
    public CreateChat fromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject contentObj = jsonObject.get("content").getAsJsonObject();
        int token = contentObj.get("token").getAsInt();
        JsonArray members = contentObj.get("member").getAsJsonArray();
        String name = contentObj.get("name").getAsString();
        boolean isGroup = contentObj.get("is_group").getAsBoolean();
        ArrayList<UUID> memberList = new ArrayList<>(members.size());
        for (JsonElement element : members) {
            JsonObject memberObj = element.getAsJsonObject();

            memberList.add(UUID.fromString(memberObj.get("id").getAsString()));
        }
        return new CreateChat(token, name, memberList, isGroup);
    }
}
