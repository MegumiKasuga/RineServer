package edu.carole.data.rec;

import java.util.List;
import java.util.UUID;

public record CreateChat(int creatorToken, String name, List<UUID> memberId, boolean isGroup) { }
