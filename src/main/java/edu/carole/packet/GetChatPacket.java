package edu.carole.packet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.Result;
import edu.carole.data.rec.GetChat;
import edu.carole.data.rec.GetMsgResult;

public class GetChatPacket implements Packet<GetChat> {
    @Override
    public void setStateCode(int stateCode) {

    }

    @Override
    public JsonObject getReplyJson(Result<GetChat> data) {
        JsonObject reply = new JsonObject();
        reply.addProperty("state_code", data.getStateCode());
        JsonObject contentObj = new JsonObject();
        reply.add("content", contentObj);
        contentObj.addProperty("msg", data.getDataPack().getString("msg"));
        if (data.getStateCode() != 200) {
            contentObj.addProperty("cause", data.getDataPack().getString("cause"));
            return reply;
        }
        GetMsgResult[] results = (GetMsgResult[]) data.getBundle().getData();
        JsonArray array = new JsonArray();
        contentObj.add("values", array);
        for (GetMsgResult result : results) {
            JsonObject msgObj = new JsonObject();
            msgObj.addProperty("id", result.id().toString());
            msgObj.addProperty("content", result.content());
            msgObj.addProperty("name", result.name());
            msgObj.addProperty("time", result.time());
            msgObj.addProperty("index", result.index());
            array.add(msgObj);
        }
        return reply;
    }

    @Override
    public GetChat fromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject content = jsonObject.get("content").getAsJsonObject();
        int token = content.get("token").getAsInt();
        long chatId = content.get("chat").getAsLong();
        int index = content.has("index") ? content.get("index").getAsInt() : -1;
        int count = content.has("count") ? content.get("count").getAsInt() : 20;
        return new GetChat(token, chatId, index, count);
    }
}
