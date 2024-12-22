package edu.carole.data.affairs;

import edu.carole.Main;
import edu.carole.data.Result;
import edu.carole.data.UserToken;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.Chat;
import edu.carole.data.rec.Msg;
import edu.carole.data.rec.SendMsg;
import edu.carole.packet.Packet;
import edu.carole.packet.SendMsgPacket;

import java.util.function.Supplier;

public class SendMsgAffair extends Affair<SendMsg> {


    public SendMsgAffair(int serviceCode) {
        super(serviceCode, SendMsgPacket::new);
    }

    @Override
    public Result<SendMsg> handle(SendMsg data) {
        DataPack result = new DataPack();
        UserToken token = Main.server.getOnlineUserByToken(data.senderToken());
        if (token == null) {
            result.add("msg", "failed");
            result.add("cause", "Not Login or Registered.");
            return new Result<>(false, result, null, this, data, 412);
        }
        Chat chat = Main.server.getChatById(data.chat());
        if (chat == null) {
            result.add("msg", "failed");
            result.add("cause", "Chat Not Found.");
            return new Result<>(false, result, null, this, data, 416);
        }
        if (data.msg().length() > 500) {
            result.add("msg", "failed");
            result.add("cause", "Message too long");
            return new Result<>(false, result, null, this, data, 405);
        }
//        if (!chat.getUsers().contains(token)) {
//            result.add("msg", "failed");
//            result.add("cause", "Permission denied.");
//            return new Result<>(false, result, null, this, data, 405);
//        }
        int index = Main.server.getChatManager().addMsg(chat, token.uid(), data.msg(), data.timeStamp());
        result.add("msg", "success");
        result.add("index", index);
        return new Result<>(true, result, null, this, data, 200);
    }
}
