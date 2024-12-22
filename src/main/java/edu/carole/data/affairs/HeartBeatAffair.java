package edu.carole.data.affairs;

import edu.carole.Main;
import edu.carole.data.Result;
import edu.carole.data.local.DataPack;
import edu.carole.data.local.UserTokenManager;
import edu.carole.data.rec.HeartBeat;
import edu.carole.packet.HeartBeatPacket;
import edu.carole.packet.Packet;

import java.util.function.Supplier;

public class HeartBeatAffair extends Affair<HeartBeat> {


    public HeartBeatAffair(int serviceCode) {
        super(serviceCode, HeartBeatPacket::new);
    }

    @Override
    public Result<HeartBeat> handle(HeartBeat data) {
        UserTokenManager manager = Main.server.getUserManager();
        DataPack result = new DataPack();
        if (!manager.alreadyLogin(data.token())) {
            result.add("msg", "failed");
            result.add("cause", "invalid token.");
            return new Result<>(false, result, null, this, data, 416);
        }
        manager.updateTimer(data.token());
        result.add("msg", "success");
        return new Result<>(true, result, null, this, data, 200);
    }
}
