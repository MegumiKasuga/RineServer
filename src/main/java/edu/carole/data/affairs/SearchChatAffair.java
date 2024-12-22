package edu.carole.data.affairs;

import edu.carole.Main;
import edu.carole.data.Bundle;
import edu.carole.data.Result;
import edu.carole.data.UserToken;
import edu.carole.data.local.DataPack;
import edu.carole.data.rec.Chat;
import edu.carole.data.rec.SearchChatResult;
import edu.carole.packet.SearchChatPacket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class SearchChatAffair extends Affair<DataPack> {

    public SearchChatAffair(int serviceCode) {
        super(serviceCode, SearchChatPacket::new);
    }

    @Override
    public Result<DataPack> handle(DataPack data) {
        DataPack resultPack = new DataPack();
        if (!Main.server.getUserManager().alreadyLogin(data.getNumber("token").intValue())) {
            resultPack.add("msg", "failed");
            resultPack.add("cause", "Not Login or Registered.");
            return new Result<>(false, resultPack, null, this, data, 412);
        }
        boolean acc = data.getBoolean("acc");
        ArrayList<SearchChatResult> chats = new ArrayList<>();
        List<Chat> values = Main.server.getChatManager().getValues();
        String msg = data.getString("msg");
        if (msg.isEmpty()) {
            resultPack.add("msg", "failed");
            resultPack.add("cause", "Could not handle empty search message!");
            return new Result<>(false, resultPack, null, this, data, 416);
        }
        if (acc) {
            accuracySearchGroup(msg, chats, values);
        } else {
            List<String> tokens = new LinkedList<>();
            splitMsg(msg, tokens);
            fuzzySearchGroup(tokens, chats, values);
        }
        SearchChatResult[] chatResult = chats.toArray(new SearchChatResult[0]);
        resultPack.add("msg", "success");
        return new Result<>(true, resultPack, new Bundle<SearchChatResult>(chatResult),
                this, data, 200);
    }

    private void accuracySearchGroup(String msg, List<SearchChatResult> chats, List<Chat> values) {
        try {
            long number = Long.parseUnsignedLong(msg);
            for (Chat chat : values) {
                if (!chat.isGroup()) continue;
                if (chat.getId() == number || chat.getName().equals(msg)) {
                    SearchChatResult result = new SearchChatResult(
                            chat.getId(), chat.getName(), chat.getUsers(), SearchChatResult.ChatType.CHAT
                    );
                    chats.add(result);
                }
            }
        } catch (Exception ignored) {
            for (Chat chat : values) {
                if (chat.getName().equals(msg)) {
                    if (!chat.isGroup()) continue;
                    SearchChatResult result = new SearchChatResult(
                            chat.getId(), chat.getName(), chat.getUsers(), SearchChatResult.ChatType.CHAT
                    );
                    chats.add(result);
                }
            }
        }
    }

    private void splitMsg(String msg, List<String> output) {
        output.add(msg);
        for (int i = msg.length() - 1; i > 1; i--) {
            for (int j = 0; j < msg.length() - i + 1; j++) {
                output.add(msg.substring(j, j + i));
            }
        }
    }

    private void fuzzySearchGroup(List<String> tokens, List<SearchChatResult> chats, List<Chat> values) {
        for (Chat chat : values) {
            for (String token : tokens) {
                if (chat.getName().contains(token)) {
                    if (!chat.isGroup()) continue;
                    SearchChatResult result = new SearchChatResult(
                            chat.getId(), chat.getName(), chat.getUsers(), SearchChatResult.ChatType.CHAT
                    );
                    chats.add(result);
                }
            }
        }
    }

    private void accuracySearchUser(String msg, List<SearchChatResult> chats, List<UserToken> values) {
        for (UserToken token : values) {

        }
    }

    private void fuzzySearchUser(List<String> tokens, List<SearchChatResult> chats, List<UserToken> values) {

    }
}
