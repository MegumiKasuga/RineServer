package edu.carole.data.rec;

import java.util.UUID;

public record Msg(UUID user, String content, long time, int index) {
}
