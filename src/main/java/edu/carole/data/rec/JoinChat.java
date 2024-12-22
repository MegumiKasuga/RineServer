package edu.carole.data.rec;

import edu.carole.data.UserToken;

import java.io.Serializable;

public record JoinChat(long chatId, int userToken) {
}
