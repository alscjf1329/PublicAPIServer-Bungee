package org.dev.publicapiserverbungee.service;

import java.util.Random;

public class RandomTokenGenerator {

    public static final String NUMBERS = "0123456789";

    public static String generate(int length, String elements) {
        StringBuilder result = new StringBuilder();
        Random rnd = new Random();

        while (result.length() < length) {
            int index = (int) (rnd.nextFloat() * elements.length());
            result.append(elements.charAt(index));
        }

        return result.toString();
    }
}
