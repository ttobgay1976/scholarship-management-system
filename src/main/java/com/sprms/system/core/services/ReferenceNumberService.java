package com.sprms.system.core.services;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class ReferenceNumberService {

    /**
     * Generates a reference number for membership requests
     * Format: MEM + YYYYMMDD + 3-digit random number (000-999)
     * Example: MEM20260427001
     */
    public String generateMembershipReferenceNumber() {
        return generateApplicationNo("MEM");
    }

    /**
     * Generates a reference number for fund requests
     * Format: FUND + YYYYMMDD + 3-digit random number (000-999)
     * Example: FUND20260427001
     */
    public String generateFundReferenceNumber() {
        return generateApplicationNo("FUND");
    }

    /**
     * Generic method to generate application numbers with different prefixes
     * @param prefix The prefix to use (MEM for membership, FUND for fund requests)
     * @return Generated reference number
     */
    private String generateApplicationNo(String prefix) {
        // Format: YYYYMMDD
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // Random 3-digit number (000-999)
        int randomNum = new Random().nextInt(1000);
        String randomPart = String.format("%03d", randomNum);

        return prefix + date + randomPart;
    }
}
