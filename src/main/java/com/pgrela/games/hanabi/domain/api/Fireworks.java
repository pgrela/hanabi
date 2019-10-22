package com.pgrela.games.hanabi.domain.api;

import java.util.List;

public interface Fireworks {

    boolean areFinished();

    boolean canAccept(KnownCard card);

    List<KnownCard> topCards();

    int score();
}
