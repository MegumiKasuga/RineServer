package edu.carole.data.rec;

import edu.carole.data.UserToken;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class UserData {

    private final UUID id;

    @Setter
    private String username;

    public UserData(UserToken token, String username) {
        this.id = token.uid();
        this.username = username;
    }

    public UserData(UUID id, String username) {
        this.id = id;
        this.username = username;
    }
}
