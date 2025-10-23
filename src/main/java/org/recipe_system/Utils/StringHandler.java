package org.recipe_system.Utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringHandler {
    public static String capitalize(String input) {
        if (null == input || 0 == input.length()) {
            return input;
        }
        return Arrays.stream(input.split("((?<=[^-`\\w])|(?=[^-`\\w]))"))
                .map(s -> s.matches("[-`\\w]+") ? Character.toUpperCase(s.charAt(0)) + s.substring(1) : s)
                .collect(Collectors.joining(""));
    }
}
