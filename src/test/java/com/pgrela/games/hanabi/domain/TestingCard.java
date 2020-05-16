package com.pgrela.games.hanabi.domain;

class TestingCard {
    public static Card card(Number number, Color color){
        return new Card(color, number);
    }
}