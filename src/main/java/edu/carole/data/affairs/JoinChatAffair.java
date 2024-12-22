package edu.carole.data.affairs;

import edu.carole.Main;
import edu.carole.data.Result;
import edu.carole.data.UserToken;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.Chat;
import edu.carole.data.rec.JoinChat;
import edu.carole.packet.JoinChatPacket;
import edu.carole.packet.Packet;

import java.util.function.Supplier;

public class JoinChatAffair extends Affair<JoinChat> {

    public JoinChatAffair(int serviceCode) {
        super(serviceCode, JoinChatPacket::new);
    }

    @Override
    public Result<JoinChat> handle(JoinChat data) {
        DataPack result = new DataPack();
        UserToken token = Main.server.getOnlineUserByToken(data.userToken());
        if (token == null) {
            result.add("msg", "failed");
            result.add("cause", "Not Login or Registered.");
            return new Result<>(false, result, null, this, data, 412);
        }
        Chat chat = Main.server.getChatById(data.chatId());
        if (!Main.server.getChatManager().hasChat(data.chatId())) {
            result.add("msg", "failed");
            result.add("cause", "Invalid chat id.");
            return new Result<>(false, result, null, this, data, 416);
        }
        Main.server.getChatManager().joinChat(chat, token);
        result.add("msg", "success");
        result.add("chat", chat.getId());
        return new Result<>(true, result, null, this, data, 200);
    }
}
