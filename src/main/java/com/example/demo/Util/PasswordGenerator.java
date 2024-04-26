package com.example.demo.Util;

import java.security.SecureRandom;

public class PasswordGenerator {
    public static String generatePassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Create a character list that includes at least one character of each type
        final String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        final String digits = "0123456789";
        final String specialCharacters = "!@#$%^&*()-_=+";
        String allCharacters = upperCaseLetters + lowerCaseLetters + digits + specialCharacters;
        // Add at least one character of each type to the password
        password.append(upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length())));
        password.append(lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(specialCharacters.charAt(random.nextInt(specialCharacters.length())));
        // Add the remaining random characters until the desired length is reached
        for (int i = 4; i < length; i++) {
            char randomChar = allCharacters.charAt(random.nextInt(allCharacters.length()));
            password.append(randomChar);
        }

        // Shuffle passwords to avoid adding characters in a fixed order
        shufflePassword(password);

        return password.toString();
    }

    // Password scrambling function to increase the strength of random passwords
    private static void shufflePassword(StringBuilder password) {
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < password.length(); i++) {
            int randomIndex = random.nextInt(password.length());
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(randomIndex));
            password.setCharAt(randomIndex, temp);
        }
    }
}
