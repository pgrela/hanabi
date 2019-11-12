package com.pgrela.games.hanabi.domain.genetic;

import com.pgrela.algorithms.genetic.api.Genome;
import com.pgrela.algorithms.genetic.engine.Randomness;
import com.pgrela.games.hanabi.domain.player.Cheater;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CheatingFriends implements Genome<CheatingFriends> {
    private CheaterGenome[] friends;
    private Random random = Randomness.RANDOM;

    public CheatingFriends(CheaterGenome[] friends) {
        this.friends = friends;
    }

    @Override
    public CheatingFriends mutate() {
        CheaterGenome[] friends = Arrays.stream(this.friends).map(CheaterGenome::clone).toArray(CheaterGenome[]::new);
        mutateOne(friends);
        mutateOne(friends);
        mutateOne(friends);
        if(random.nextBoolean())swap(friends);
        return new CheatingFriends(friends);
    }

    private void mutateOne(CheaterGenome[] friends) {
        int i = random.nextInt(friends.length);
        friends[i] = friends[i].mutate();
    }

    private void swap(CheaterGenome[] friends) {
        int a = random.nextInt(friends.length);
        int b = random.nextInt(friends.length);
        CheaterGenome tmp = friends[a];
        friends[a] = friends[b];
        friends[b] = tmp;
    }

    @Override
    public CheatingFriends cross(CheatingFriends partner) {
        CheaterGenome[] friends = new CheaterGenome[this.friends.length];
        for (int i = 0; i < this.friends.length; i++) {
            friends[i] = this.friends[i].cross(partner.friends[i]);
        }
        return new CheatingFriends(friends);
    }

    @Override
    public CheatingFriends deserialize(String genome) {
        return fromString(genome);
    }

    public static CheatingFriends fromString(String genome){
        return new CheatingFriends(Arrays.stream(genome.substring(1, genome.length() - 1).split(";")).map(CheaterGenome::fromString).toArray(CheaterGenome[]::new));
    }

    @Override
    public String serialize() {
        return "[" + Arrays.stream(friends).map(CheaterGenome::serialize).collect(Collectors.joining(";")) + "]";
    }

    @Override
    public CheatingFriends immigrant() {
        return random(4);
    }

    public static CheatingFriends random(int players) {
        return new CheatingFriends(Stream.generate(CheaterGenome::random).limit(players).toArray(CheaterGenome[]::new));
    }

    public List<Cheater> friends() {
        return Arrays.stream(friends).map(Cheater::new).collect(Collectors.toList());
    }
}
