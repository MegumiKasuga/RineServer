package edu.carole.data.affairs;

import edu.carole.Main;
import edu.carole.data.Result;
import edu.carole.data.UserToken;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.Login;
import edu.carole.packet.LoginPacket;
import edu.carole.packet.Packet;

import java.util.function.Supplier;

public class LoginAffair extends Affair<Login> {

    public LoginAffair(int serviceCode) {
        super(serviceCode, LoginPacket::new);
    }

    @Override
    public Result<Login> handle(Login data) {
        UserToken token = Main.server.hasUser(data);
        DataPack result = new DataPack();
        boolean flag = token != null;
        if (flag) {
            System.out.println("user: " + data);
            Main.server.getUserManager().userLogin(token);
            result.add("msg", "success");
        } else {
            result.add("msg", "failed");
            result.add("cause", "invalid user token");
        }
        return new Result<>(flag, result, null, this, data, 200);
    }
}
