package com.example.myproject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class ValidationProcess {

    public static boolean isValidPassword(String password) {
        // Check if password length is between 8 and 14 characters
        if (password.length() < 8 || password.length() > 14) {
            return false;
        }

        // Check if password contains at least one digit
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        // Check if password contains at least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Check if password contains at least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }

        // Check if password contains at least one symbol
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            return false;
        }

        return true;
    }


    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String generateTransactionId() {
        int length = 10;
        StringBuilder transactionIdBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            transactionIdBuilder.append(randomChar);
        }

        return transactionIdBuilder.toString();
    }

    public static boolean isValidEmail(String email) {
        // Regular expression pattern for email validation
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }


    public static String hashPassword(String password) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = messageDigest.digest(password.getBytes());

            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hashBuilder.append(hex);
            }

            return hashBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

}
