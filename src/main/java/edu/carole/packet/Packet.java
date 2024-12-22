package edu.carole.packet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.carole.data.Result;

public interface Packet<T> {

    void setStateCode(int stateCode);

    JsonObject getReplyJson(Result<T> data);

    T fromJson(final JsonElement json);
}