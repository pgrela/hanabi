package com.pgrela.games.hanabi.domain;

import com.pgrela.games.hanabi.domain.api.OtherPlayer;
import com.pgrela.games.hanabi.domain.api.Player;
import com.pgrela.games.hanabi.domain.hint.ColorHintAnyone;
import com.pgrela.games.hanabi.domain.hint.NumberHintAnyone;

public class ThePlayer implements OtherPlayer {
    private Player player;
    private Hand hand;
    private String name;

    public ThePlayer(Player player, Hand hand, String name) {
        this.player = player;
        this.hand = hand;
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String name() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public void dispatchHint(ColorHintAnyone colorHintAnyone) {
        if (colorHintAnyone.getToPlayer().equals(this)) {
            player.receiveHint(colorHintAnyone);
        } else {
            player.hintGiven(colorHintAnyone);
        }
    }

    public void dispatchHint(NumberHintAnyone numberHintAnyone) {
        if (numberHintAnyone.getToPlayer().equals(this)) {
            player.receiveHint(numberHintAnyone);
        } else {
            player.hintGiven(numberHintAnyone);
        }
    }

    @Override
    public String toString() {
        return "{" +
            "" + name +
            '}';
    }
}
