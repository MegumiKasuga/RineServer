package edu.carole.data.affairs;

import edu.carole.Main;
import edu.carole.data.Result;
import edu.carole.data.UserToken;
import edu.carole.data.rec.Login;
import edu.carole.packet.Packet;
import edu.carole.packet.QuitPacket;

import java.util.function.Supplier;

public class QuitAffair extends Affair<Login> {

    public QuitAffair(int serviceCode) {
        super(serviceCode, QuitPacket::new);
    }

    @Override
    public Result<Login> handle(Login data) {
        Main.server.getUserManager().userQuit(new UserToken(data.getId(), data.getToken()));
        return new Result<>(true, null, null, this, data, 200);
    }
}
