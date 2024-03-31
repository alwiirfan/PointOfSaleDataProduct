package com.pointofsale.dataSupplier.util;

import org.springframework.stereotype.Component;

import java.util.Random;

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

    public String generateRandomNumber() {
       Random random = new Random();

       Integer trx1 = random.nextInt(100);
       Integer trx2 = random.nextInt(100);
       Integer trx3 = random.nextInt(10000);
       Integer trx4 = random.nextInt(10000);
       Integer trx5 = random.nextInt(100);
       Integer trx6 = random.nextInt(100);

       String trxNumString = String.format("TRX-%02d.%02d.%04d.%04d.%02d.%02d", trx1, trx2, trx3, trx4, trx5, trx6);
       return trxNumString;
    }
}