package net.team.astera.impl.util.traits;

import com.google.gson.JsonElement;

public interface Jsonable {
    JsonElement toJson();

    void fromJson(JsonElement element);

    default String getFileName() {
        return "";
    }
}
