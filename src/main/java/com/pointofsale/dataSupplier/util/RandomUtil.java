package com.pointofsale.dataSupplier.util;

import org.springframework.stereotype.Component;

@Component
public class RandomUtil {
    
    public String generateRandomString(int length) {
        String alphaChars = "abcdefghijklmnopqrstuvwxyz";
        String numericChars = "0123456789";
        StringBuilder sb = new StringBuilder();

        int totalAlphaChars = (length + 1) / 2;
        int totalNumericChars = length - totalAlphaChars;

        for (int i = 0; i < totalAlphaChars; i++) {
            int alphaIndex = (int) (Math.random() * alphaChars.length());
            sb.append(alphaChars.charAt(alphaIndex));
        }

        sb.append("-");

        for (int i = 0; i < totalNumericChars; i++) {
            int numericIndex = (int) (Math.random() * numericChars.length());
            sb.append(numericChars.charAt(numericIndex));
        }

        return sb.toString();
    }
}
