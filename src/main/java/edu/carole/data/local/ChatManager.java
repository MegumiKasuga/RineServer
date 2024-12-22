package edu.carole.data.local;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.Main;
import edu.carole.data.UserToken;
import edu.carole.data.rec.Chat;
import edu.carole.data.rec.Msg;
import lombok.Getter;

import java.util.*;

@Getter
public class ChatManager extends TokenManager<Chat> {

    private final HashMap<Chat, MsgManager> chats = new HashMap<>();

    public ChatManager(String path) {
        super(path);
        for (Chat c : getValues()) {
            chats.put(c, new MsgManager(c));
        }
    }

    @Override
    public JsonElement getJsonElement(Chat token) {
        JsonObject object = new JsonObject();
        object.addProperty("id", token.getId());
        object.addProperty("name", token.getName());
        object.addProperty("is_group", token.isGroup());
        JsonArray usrs = new JsonArray();
        for (UserToken u : token.getUsers()) {
            JsonObject usr = new JsonObject();
            usr.addProperty("id", u.uid().toString());
            usrs.add(usr);
        }
        object.add("usrs", usrs);
        return object;
    }

    @Override
    public Chat loadFromJson(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        long id = object.get("id").getAsLong();
        String name = object.get("name").getAsString();
        boolean isGroup = object.get("is_group").getAsBoolean();
        JsonArray usrs = object.get("usrs").getAsJsonArray();
        Chat chat = new Chat(id, name, isGroup);
        for (JsonElement usr : usrs) {
            JsonObject obj = usr.getAsJsonObject();
            UUID uid = UUID.fromString(obj.get("id").getAsString());
            UserToken token = Main.server.getUserManager().getUserByID(uid);
            if (token == null)
                continue;
            chat.getUsers().add(token);
        }
        // this.chats.put(chat, new MsgManager(chat));
        return chat;
    }

    @Override
    public void getDefaultSettings(DataPack meta) {

    }

    @Override
    public void addToken(Chat token) {
        this.chats.put(token, new MsgManager(token));
        super.addToken(token);
    }

    @Override
    public boolean removeToken(Chat token) {
        boolean flag = super.removeToken(token);
        if (flag) this.chats.remove(token);
        return flag;
    }

    public Chat createNewChat(Random random, String name, List<UserToken> users, boolean isGroup) {
        Chat chat = new Chat(random.nextLong(), name, isGroup);
        chat.getUsers().addAll(users);
        this.addToken(chat);
        return chat;
    }

    public boolean hasChat(Chat chat) {
        return getValues().contains(chat);
    }

    public boolean hasChat(long chatId) {
        for (Chat c : getValues()) {
            if (c.getId() == chatId) return true;
        }
        return false;
    }

    public List<Msg> getMsg(Chat chat, int index, int count) {
        if (!hasChat(chat)) return List.of();
        MsgManager msg = chats.get(chat);
        index += index < 0 ? msg.getValues().size() : 0;
        ArrayList<Msg> result = new ArrayList<>(count);
        for (int i = Math.max(0, index - count); i < index && i < getValues().size(); i++) {
            result.add(msg.getValues().get(i));
        }
        return result;
    }

    public boolean addMsg(Chat chat, Msg msg) {
        if (!hasChat(chat)) return false;
        MsgManager manager = chats.get(chat);
        manager.addToken(msg);
        return true;
    }

    public int addMsg(Chat chat, UUID senderId, String msg, long time) {
        if (!hasChat(chat)) return -1;
        MsgManager manager = chats.get(chat);
        int index = manager.getValues().size();
        manager.addToken(new Msg(senderId, msg, time, index));
        return index;
    }

    public boolean joinChat(Chat chat, UserToken user) {
        if (!hasChat(chat)) return false;
        chat.getUsers().add(user);
        this.markDirty();
        return true;
    }

    @Override
    public void saveToDisk() {
        super.saveToDisk();
        chats.forEach((k, v) -> v.saveToDisk());
    }
}
