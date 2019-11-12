package com.pgrela.games.hanabi.domain;

import static org.junit.jupiter.api.Assertions.*;

class TestingCard {
    public static Card card(Number number, Color color){
        return new Card(color, number);
    }
}