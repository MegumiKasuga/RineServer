package edu.carole.data.rec;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.carole.data.UserToken;

import java.util.List;

public record SearchChatResult(long chatId, String name, List<UserToken> member, ChatType type) {

    public JsonObject getJson() {
        JsonObject json = new JsonObject();
        json.addProperty("id", chatId);
        json.addProperty("name", name);
        json.addProperty("type", type.toString());
        JsonArray users = new JsonArray();
        for (UserToken member : member) {
            JsonObject memberJson = new JsonObject();
            memberJson.addProperty("id", member.uid().toString());
            users.add(memberJson);
        }
        json.add("member", users);
        return json;
    }

    public enum ChatType {
        CHAT, USER, SERVICE;

        @Override
        public String toString() {
            return switch (this) {
                case CHAT -> "chat";
                case USER -> "user";
                case SERVICE -> "service";
            };
        }
    }
}
