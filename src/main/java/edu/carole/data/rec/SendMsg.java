package edu.carole.data.rec;

public record SendMsg(int senderToken, String msg, long chat, long timeStamp) {
}
