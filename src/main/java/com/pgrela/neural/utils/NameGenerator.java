package com.pgrela.neural.utils;

import java.util.Random;

public class NameGenerator {
    private static String[] NAMES = new String[]{
            "Emma", "Olivia", "Ava", "Isabella", "Sophia", "Charlotte", "Mia", "Amelia",
            "Harper", "Evelyn", "Abigail", "Emily", "Elizabeth", "Mila", "Ella", "Avery", "Sofia",
            "Camila", "Aria", "Scarlett", "Victoria", "Madison", "Luna", "Grace", "Chloe", "Penelope",
            "Layla", "Riley", "Zoey", "Nora", "Lily", "Eleanor", "Hannah", "Lillian", "Addison", "Aubrey",
            "Ellie", "Stella", "Natalie", "Zoe", "Leah", "Hazel", "Violet", "Aurora", "Savannah", "Audrey",
            "Brooklyn", "Bella", "Claire", "Skylar", "Lucy", "Paisley", "Everly", "Anna", "Caroline", "Nova",
            "Genesis", "Emilia", "Kennedy", "Samantha", "Maya", "Willow", "Kinsley", "Naomi", "Aaliyah", "Elena",
            "Sarah", "Ariana", "Allison", "Gabriella", "Alice", "Madelyn", "Cora", "Ruby", "Eva", "Serenity", "Autumn",
            "Adeline", "Hailey", "Gianna", "Valentina", "Isla", "Eliana", "Quinn", "Nevaeh", "Ivy", "Sadie", "Piper",
            "Lydia", "Alexa", "Josephine", "Emery", "Julia", "Delilah", "Arianna", "Vivian", "Kaylee", "Sophie", "Brielle",
            "Madeline", "Liam", "Noah", "William", "James", "Oliver", "Benjamin", "Elijah", "Lucas", "Mason", "Logan",
            "Alexander", "Ethan", "Jacob", "Michael", "Daniel", "Henry", "Jackson", "Sebastian", "Aiden", "Matthew",
            "Samuel", "David", "Joseph", "Carter", "Owen", "Wyatt", "John", "Jack", "Luke", "Jayden", "Dylan", "Grayson",
            "Levi", "Isaac", "Gabriel", "Julian", "Mateo", "Anthony", "Jaxon", "Lincoln", "Joshua", "Christopher",
            "Andrew", "Theodore", "Caleb", "Ryan", "Asher", "Nathan", "Thomas", "Leo", "Isaiah", "Charles", "Josiah",
            "Hudson", "Christian", "Hunter", "Connor", "Eli", "Ezra", "Aaron", "Landon", "Adrian", "Jonathan", "Nolan",
            "Jeremiah", "Easton", "Elias", "Colton", "Cameron", "Carson", "Robert", "Angel", "Maverick", "Nicholas", "Dominic",
            "Jaxson", "Greyson", "Adam", "Ian", "Austin", "Santiago", "Jordan", "Cooper", "Brayden", "Roman", "Evan", "Ezekiel",
            "Xavier", "Jose", "Jace", "Jameson", "Leonardo", "Bryson", "Axel", "Everett", "Parker", "Kayden", "Miles", "Sawyer", "Jason"};
    private static String[] ADJECTIVES = new String[]{
            "Great", "First", "Decisive", "Last", "Pitched", "Naval", "Second", "Long", "Bloody", "Fierce", "Final", "Major",
            "Famous", "Legal", "Real", "Uphill", "Desperate", "Constant", "Single", "Terrible", "Open", "Big", "Hard", "Drawn",
            "Bitter", "Disastrous", "Greatest", "Memorable", "Day", "Actual", "Furious", "Defensive", "Fought", "Fatal",
            "Successful", "Severe", "Ongoing", "Mock", "Spiritual", "Impending", "Ideological", "Regular", "Year", "Sanguinary",
            "Epic", "Crucial", "Sham", "Celebrated", "Indecisive", "Daily", "Victorious", "Unequal", "Glorious", "Continuous",
            "Protracted", "Lost", "Terrific", "Verbal", "Hour", "Historic", "Tremendous", "Prolonged", "Tough", "Biggest", "Brief",
            "Scale", "Climactic", "Heroic", "Bloodiest", "Mighty", "Continual", "Hopeless", "Contested", "Grim"
    };
    private static boolean[] taken = new boolean[ADJECTIVES.length * NAMES.length];

    private static Random random = new Random();

    public static final String name(int seed) {
        random.setSeed(seed);
        int i = random.nextInt(taken.length);
        while (taken[i]) i = (i + 1) % taken.length;
        taken[i] = true;
        return ADJECTIVES[i % ADJECTIVES.length] + " " + NAMES[(i / ADJECTIVES.length)];
    }

}
