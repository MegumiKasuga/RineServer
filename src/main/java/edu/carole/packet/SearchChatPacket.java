package edu.carole.packet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.Main;
import edu.carole.data.Bundle;
import edu.carole.data.Result;
import edu.carole.data.local.ChatManager;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.Chat;
import edu.carole.data.rec.SearchChatResult;

public class SearchChatPacket implements Packet<DataPack> {

    private int stateCode = 200;
    @Override
    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public JsonObject getReplyJson(Result<DataPack> data) {
        setStateCode(data.getStateCode());
        final JsonObject reply = new JsonObject();
        reply.addProperty("state_code", stateCode);
        JsonObject contentObj = new JsonObject();
        reply.add("content", contentObj);
        contentObj.addProperty("msg",
                data.getDataPack().getString("msg"));
        if (stateCode != 200 || data.getBundle() == null) {
            contentObj.addProperty("cause",
                    data.getDataPack().getString("cause"));
            return reply;
        }
        JsonArray values = new JsonArray();
        Bundle<SearchChatResult> bundle = data.getBundle();
        for (SearchChatResult c : bundle.getData()) {
            values.add(c.getJson());
        }
        contentObj.add("values", values);
        return reply;
    }

    @Override
    public DataPack fromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject contentObj = jsonObject.get("content").getAsJsonObject();
        int token = contentObj.get("token").getAsInt();
        String message = contentObj.get("msg").getAsString();
        boolean acc = contentObj.get("accuracy").getAsBoolean();
        DataPack pack = new DataPack();
        pack.add("token", token);
        pack.add("msg", message);
        pack.add("acc", acc);
        return pack;
    }
}
