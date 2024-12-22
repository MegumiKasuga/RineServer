package edu.carole.data.local;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.UserToken;
import edu.carole.data.rec.UserData;
import lombok.Getter;

import java.util.*;

@Getter
public class UserTokenManager extends TokenManager<UserToken> {

    private final HashMap<UserToken, Long> onlineUsers;

    private final UserDataManager dataManager;

    public UserTokenManager(String path, String dataManagerPath) {
        super(path);
        dataManager = new UserDataManager(dataManagerPath);
        onlineUsers = new HashMap<>();
    }

    @Override
    public JsonElement getJsonElement(UserToken token) {
        JsonObject json = new JsonObject();
        json.addProperty("token", token.token());
        json.addProperty("id", token.uid().toString());
        return json;
    }

    @Override
    public UserToken loadFromJson(JsonElement json) {
        JsonObject obj = json.getAsJsonObject();
        UUID uid = UUID.fromString(obj.get("id").getAsString());
        int token = obj.get("token").getAsInt();
        return new UserToken(uid, token);
    }

    @Override
    public void getDefaultSettings(DataPack meta) {

    }

    public UserToken getUserByID(UUID id) {
        for (UserToken v : getValues()) {
            if (v.uid().equals(id))
                return v;
        }
        return null;
    }

    public boolean alreadyLogin(int userToken) {
        for (UserToken v : onlineUsers.keySet()) {
            if (v.token() == userToken)
                return true;
        }
        return false;
    }

    public void userLogin(UserToken token) {
        onlineUsers.put(token, System.currentTimeMillis());
    }

    public void userRegister(UserToken token, UserData data) {
        addToken(token);
        dataManager.addToken(data);
        userLogin(token);
    }

    public void updateTimer(UserToken token) {
        onlineUsers.put(token, System.currentTimeMillis());
    }

    public void updateTimer(long token) {
        for (UserToken v : onlineUsers.keySet()) {
            if (v.token() == token)
                onlineUsers.put(v, System.currentTimeMillis());
        }
    }

    public void userQuit(UserToken token) {
        onlineUsers.remove(token);
    }

    public boolean isUserOnline(UserToken token) {
        return onlineUsers.containsKey(token);
    }

    public boolean isUserOnline(int userToken) {
        for (UserToken v : onlineUsers.keySet()) {
            if (v.token() == userToken)
                return true;
        }
        return false;
    }

    public void removeOvertimeUsers(long delay) {
        long currentTime = System.currentTimeMillis();
        long overTime = currentTime - delay;
        HashSet<UserToken> toRemove = new HashSet<>();
        getOnlineUsers().forEach((k, v) -> {
            if (v < overTime) toRemove.add(k);
        });
        for (UserToken v : toRemove) {
            onlineUsers.remove(v);
        }
    }

    public boolean hasUser(UUID userId) {
        for (UserToken token : getValues()) {
            if (token.uid().equals(userId)) return true;
        }
        return false;
    }

    public UserToken getTokenById(UUID userId) {
        for (UserToken token : getValues()) {
            if (token.uid().equals(userId)) return token;
        }
        return null;
    }

    public UserData getDataById(UUID userId) {
        return dataManager.getUserData(userId);
    }

    @Override
    public void saveToDisk() {
        super.saveToDisk();
        dataManager.saveToDisk();
    }
}
