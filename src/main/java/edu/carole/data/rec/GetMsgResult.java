package edu.carole.data.rec;

import java.util.UUID;

public record GetMsgResult(UUID id, String name, String content, int index, long time) {
}
