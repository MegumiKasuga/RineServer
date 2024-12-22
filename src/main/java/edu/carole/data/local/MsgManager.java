package edu.carole.data.local;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.rec.Chat;
import edu.carole.data.rec.Msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MsgManager extends TokenManager<Msg> {

    private final Chat chat;

    public MsgManager(Chat chat) {
        super("chat/" + chat.getId() + "_msg.json");
        this.chat = chat;
    }

    @Override
    public JsonElement getJsonElement(Msg token) {
        JsonObject json = new JsonObject();
        json.addProperty("id", token.user().toString());
        json.addProperty("content", token.content());
        json.addProperty("time", token.time());
        json.addProperty("index", token.index());
        return json;
    }

    @Override
    public Msg loadFromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        UUID id = UUID.fromString(jsonObject.get("id").getAsString());
        String content = jsonObject.get("content").getAsString();
        long time = jsonObject.get("time").getAsLong();
        int index = jsonObject.get("index").getAsInt();
        return new Msg(id, content, time, index);
    }

    @Override
    public void getDefaultSettings(DataPack meta) {
        // meta.add("id", chat.getId());
        // meta.add("name", chat.getName());
        // meta.add("index", -1);
    }

    @Override
    public void addToken(Msg token) {
        super.addToken(token);
        getMeta().add("index", token.index());
    }

    public void addMsg(UUID usrId, String content, long time) {
        addToken(new Msg(usrId, content, time, this.getValues().size()));
    }

    public List<Msg> getMsg(int index, int count) {
        if (index < 0) index += getValues().size();
        if (this.getMeta().getNumber("index").intValue() < 0)
            return List.of();
        if (this.getMeta().getNumber("index").intValue() < index) {
            return List.of();
        }
        return getValues().subList(Math.max(0, index - count), index);
    }
}
