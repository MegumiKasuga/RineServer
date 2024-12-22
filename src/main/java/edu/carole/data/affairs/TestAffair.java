package edu.carole.data.affairs;

import edu.carole.data.Result;
import edu.carole.data.rec.Test;
import edu.carole.packet.TestPacket;

public class TestAffair extends Affair<Test> {

    public TestAffair(int serviceCode) {
        super(serviceCode, TestPacket::new);
    }

    @Override
    public Result<Test> handle(Test data) {
        return new Result<Test>(true, null, null, this, data, 200);
    }
}
