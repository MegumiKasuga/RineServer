package edu.carole;

import edu.carole.data.affairs.*;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.*;

import java.util.HashMap;

public class AllAffairs {

    public static final HashMap<Integer, Affair> AFFAIRS = new HashMap<>();

    public static final Affair<Test> TEST_AFFAIR =
            register(new TestAffair(0));

    public static final Affair<Login> LOGIN_AFFAIR =
            register(new LoginAffair(1));

    public static final Affair<Login> REGISTER_AFFAIR =
            register(new RegisterAffair(2));

    public static final Affair<HeartBeat> HEART_BEAT_AFFAIR =
            register(new HeartBeatAffair(3));

    public static final Affair<DataPack> SEARCH_CHAT_AFFAIR =
            register(new SearchChatAffair(4));

    public static final Affair<Login> QUIT_AFFAIR =
            register(new QuitAffair(5));

    public static final Affair<JoinChat> JOIN_CHAT_AFFAIR =
            register(new JoinChatAffair(6));

    // 7 -> create chat
    public static final Affair<CreateChat> CREATE_CHAT_AFFAIR =
            register(new CreateChatAffair(7));

    // 8 -> send msg
    public static final Affair<SendMsg> SEND_MSG_AFFAIR =
            register(new SendMsgAffair(8));

    public static final Affair<GetChat> GET_CHAT_AFFAIR =
            register(new GetChatAffair(9));

    public static <T> Affair<T> register(Affair<T> affair) {
        AFFAIRS.put(affair.getServiceCode(), affair);
        return affair;
    }
}
