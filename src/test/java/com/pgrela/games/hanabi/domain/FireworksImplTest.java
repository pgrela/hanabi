package com.pgrela.games.hanabi.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class FireworksImplTest {
    @Test
    public void shouldAffirmContainingACard() {
        FireworksImpl fireworks = FireworksImpl.empty();
        fireworks.add(TestingCard.card(Number.ONE, Color.RED));
        fireworks.add(TestingCard.card(Number.TWO, Color.RED));

        boolean contains = fireworks.contains(TestingCard.card(Number.ONE, Color.RED));

        Assertions.assertThat(contains).isEqualTo(true);
    }

    @Test
    public void shouldAffirmContainingTopCard() {
        FireworksImpl fireworks = FireworksImpl.empty();
        fireworks.add(TestingCard.card(Number.ONE, Color.RED));
        fireworks.add(TestingCard.card(Number.TWO, Color.RED));

        boolean contains = fireworks.contains(TestingCard.card(Number.TWO, Color.RED));

        Assertions.assertThat(contains).isEqualTo(true);
    }

    @Test
    public void shouldNeglectContainingACard() {
        FireworksImpl fireworks = FireworksImpl.empty();
        fireworks.add(TestingCard.card(Number.ONE, Color.RED));
        fireworks.add(TestingCard.card(Number.TWO, Color.RED));

        boolean contains = fireworks.contains(TestingCard.card(Number.THREE, Color.RED));

        Assertions.assertThat(contains).isEqualTo(false);
    }


}