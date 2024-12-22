package edu.carole.data.rec;

import edu.carole.data.UserToken;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Chat {

    private final long id;
    @Setter
    private String name;
    private final List<UserToken> users;

    @Setter
    private boolean group;

    public Chat(long id, String name, boolean isGroup) {
        this.id = id;
        this.name = name;
        this.users = new ArrayList<UserToken>();
        this.group = isGroup;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                ", group=" + group +
                '}';
    }
}
