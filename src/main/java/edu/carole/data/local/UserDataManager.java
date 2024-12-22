package edu.carole.data.local;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.UserToken;
import edu.carole.data.rec.UserData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class UserDataManager extends TokenManager<UserData> {

    private final HashMap<UUID, UserData> userDatas;

    public UserDataManager(String path) {
        super(path);
        userDatas = new HashMap<>();
        for (UserData data : getValues()) {
            userDatas.put(data.getId(), data);
        }
    }

    @Override
    public JsonElement getJsonElement(UserData token) {
        JsonObject object = new JsonObject();
        object.addProperty("id", token.getId().toString());
        object.addProperty("name", token.getUsername());
        return object;
    }

    @Override
    public void addToken(UserData token) {
        super.addToken(token);
        this.userDatas.put(token.getId(), token);
    }

    @Override
    public UserData loadFromJson(JsonElement json) {
        JsonObject object = json.getAsJsonObject();
        UUID id = UUID.fromString(object.get("id").getAsString());
        String username = object.get("name").getAsString();
        return new UserData(id, username);
    }

    public UserData getUserData(UUID id) {
        return userDatas.getOrDefault(id, null);
    }

    @Override
    public void getDefaultSettings(DataPack meta) {

    }
}
