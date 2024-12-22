package edu.carole.data.affairs;

import edu.carole.Main;
import edu.carole.data.Result;
import edu.carole.data.UserToken;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.Login;
import edu.carole.data.rec.Register;
import edu.carole.data.rec.UserData;
import edu.carole.packet.LoginPacket;

public class RegisterAffair extends Affair<Login> {

    public RegisterAffair(int serviceCode) {
        super(serviceCode, LoginPacket::new);
    }

    @Override
    public Result<Login> handle(Login data) {
        DataPack result = new DataPack();
        if (!(data instanceof Register register)) {
            result.add("msg", "failed");
            result.add("cause", "Method not allowed.");
            return new Result<Login>(false, result, null, this, data, 405);
        }
        UserToken token = Main.server.hasUser(data);
        if (token != null) {
            result.add("msg", "failed");
            result.add("cause", "ID already in use.");
            return new Result<Login>(false, result, null, this, data, 405);
        }
        token = new UserToken(data.getId(), data.getToken());
        UserData userData = new UserData(token, register.getName());
        Main.server.getUserManager().userRegister(token, userData);
        result.add("msg", "success");
        return new Result<Login>(true, result, null, this, data, 200);
    }
}
