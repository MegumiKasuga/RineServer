package edu.carole.data.rec;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Login {

    final UUID id;
    final int token;

    public Login(UUID uuid, int token) {
        this.id = uuid;
        this.token = token;
    }

    @Override
    public String toString() {
        return "UserToken<" + id + ", " + token +  ">";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
