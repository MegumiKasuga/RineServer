package edu.carole.data.affairs;

import edu.carole.Main;
import edu.carole.data.Bundle;
import edu.carole.data.Result;
import edu.carole.data.UserToken;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.*;
import edu.carole.packet.GetChatPacket;
import edu.carole.packet.Packet;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class GetChatAffair extends Affair<GetChat> {

    public GetChatAffair(int serviceCode) {
        super(serviceCode, GetChatPacket::new);
    }

    @Override
    public Result<GetChat> handle(GetChat data) {
        DataPack pack = new DataPack();
        UserToken token = Main.server.getOnlineUserByToken(data.token());
        if (token == null) {
            pack.add("msg", "failed");
            pack.add("cause", "Not Login or Registered.");
            return new Result(false, pack, null, this, data, 412);
        }
        Chat chat = Main.server.getChatById(data.chatId());
        if (chat == null) {
            pack.add("msg", "failed");
            pack.add("cause", "Chat Not Found");
            return new Result(false, pack, null, this, data, 416);
        }
        List<Msg> msgs = Main.server.getChatManager().getMsg(chat, data.cursor(), data.count());
        GetMsgResult[] results = new GetMsgResult[msgs.size()];
        for (int i = 0; i < msgs.size(); i++) {
            Msg msg = msgs.get(i);
            UserData usData = Main.server.getUserManager().getDataById(token.uid());
            usData = usData != null ? usData : new UserData(token.uid(), "Rine User");
            results[i] = new GetMsgResult(msg.user(), usData.getUsername(),
                                        msg.content(), msg.index(), msg.time());
        }
        pack.add("msg", "success");
        return new Result<>(true, pack, new Bundle<>(results), this, data, 200);
    }
}
