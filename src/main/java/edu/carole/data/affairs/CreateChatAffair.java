package edu.carole.data.affairs;

import edu.carole.Main;
import edu.carole.data.Result;
import edu.carole.data.UserToken;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.Chat;
import edu.carole.data.rec.CreateChat;
import edu.carole.packet.CreateChatPacket;
import edu.carole.packet.Packet;

import java.util.UUID;
import java.util.function.Supplier;

public class CreateChatAffair extends Affair<CreateChat> {
    public CreateChatAffair(int serviceCode) {
        super(serviceCode, CreateChatPacket::new);
    }

    @Override
    public Result<CreateChat> handle(CreateChat data) {
        UserToken token = Main.server.getOnlineUserByToken(data.creatorToken());
        DataPack result = new DataPack();
        if (token == null) {
            result.add("msg", "failed");
            result.add("cause", "Not Login or Registered.");
            return new Result<>(false, result, null, this, data, 412);
        }
        if (data.name().isBlank() || data.name().length() > 16) {
            result.add("msg", "failed");
            result.add("cause", data.name().isBlank() ? "Name should not be empty." : "Name is too long");
            return new Result<>(false, result, null, this, data, 416);
        }
        Chat chat = new Chat(Main.random.nextLong(), data.name(), data.isGroup());
        for (UUID id : data.memberId()) {
            UserToken t = Main.server.getUserManager().getTokenById(id);
            if (t == null) continue;
            chat.getUsers().add(t);
        }
        Main.server.getChatManager().addToken(chat);
        result.add("msg", "success");
        result.add("chat", chat.getId());
        return new Result<>(true, result, null, this, data, 200);
    }
}
