package Backend.Addons;

import Backend.Entities.Items.Item;
import Exceptions.BadEmailException;
import Exceptions.BadPasswordException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Util {

    private Util() {

    }

    /**
     * Validate if an email address is correct by the default rules of email addresses.
     * @param email the email address to validate
     * @return true if validated successfully, else false.
     */

    public static boolean validateEmailAddress(String email) throws BadEmailException {
        final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (!matcher.find()) {
            throw new BadEmailException("email " + email + "is not a valid email address");
        }
        return true;
    }

    /**
     * Validate if a password is correct by the rules of the system:
     * 1. At least 8 characters.
     * 2. Only lower and upper case letters.
     * @param password the password to validate
     * @return true if validated successfully, else false.
     */
    public static boolean validatePassword(String password) throws BadPasswordException {
        final Pattern VALID_PASSWORD_REGEX = Pattern.compile("[a-zA-Z0-9]{8,}");
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        if (!matcher.find()) {
            throw new BadPasswordException("password " + password + "is not a valid password");
        }
        return true;
    }

    /**
     * Finds the closest match of a given string to a list of Items by their names.
     * Find only match which the levenshtein distance is at most 2.
     * @param targetName the name to lookup
     * @param items the list of items to look the name in
     * @return the item with the closest match. If all items are more than distance 2, returns null
     */
    public static Item findClosestItemMatch(String targetName, ArrayList<Item> items) {
        int distance = Integer.MAX_VALUE;
        Item closest = null;
        for (Item compareObject : items) {
            int currentDistance = calculateLevenshteinDistance(compareObject.getName(), targetName);
            if (currentDistance < distance) {
                distance = currentDistance;
                closest = compareObject;
            }
        }
        if (distance > 2) {
            return null;
        }
        return closest;
    }

    /**
     * Calculate levenshtein distance (the difference) between two strings.
     * @param x first string
     * @param y second string
     * @return the distance
     */
    private static int calculateLevenshteinDistance(String x, String y) {
        int[][] dp = new int[x.length() + 1][y.length() + 1];

        for (int i = 0; i <= x.length(); i++) {
            for (int j = 0; j <= y.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                }
                else if (j == 0) {
                    dp[i][j] = i;
                }
                else {
                    dp[i][j] = min(dp[i - 1][j - 1]
                                    + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)),
                            dp[i - 1][j] + 1,
                            dp[i][j - 1] + 1);
                }
            }
        }

        return dp[x.length()][y.length()];
    }

    private static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }

    private static int min(int... numbers) {
        return Arrays.stream(numbers)
                .min().orElse(Integer.MAX_VALUE);
    }

}
