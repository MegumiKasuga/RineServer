package edu.carole.packet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.Result;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.SendMsg;

public class SendMsgPacket implements Packet<SendMsg> {
    @Override
    public void setStateCode(int stateCode) {

    }

    @Override
    public JsonObject getReplyJson(Result<SendMsg> data) {
        DataPack pack = data.getDataPack();
        JsonObject reply = new JsonObject();
        JsonObject content = new JsonObject();
        reply.add("content", content);
        reply.addProperty("state_code", data.getStateCode());
        content.addProperty("msg", pack.getString("msg"));
        if (data.isSuccess()) {
            content.addProperty("index", pack.getNumber("index"));
        } else {
            content.addProperty("cause", pack.getString("cause"));
        }
        return reply;
    }

    @Override
    public SendMsg fromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject content = jsonObject.get("content").getAsJsonObject();
        int senderToken = content.get("token").getAsInt();
        long chatId = content.get("chat_id").getAsLong();
        String msg = content.get("content").getAsString();
        long timeStamp = content.get("time").getAsLong();
        return new SendMsg(senderToken, msg, chatId, timeStamp);
    }
}
