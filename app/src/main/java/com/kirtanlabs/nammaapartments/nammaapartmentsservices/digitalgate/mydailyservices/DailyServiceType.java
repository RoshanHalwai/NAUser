package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import java.util.HashMap;
import java.util.Map;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 7/12/2018
 */
/* ------------------------------------------------------------- *
 * Daily Service Types
 * ------------------------------------------------------------- */
public class DailyServiceType {

    // Reverse-lookup map for getting a day from an abbreviation
    private static final Map<String, String> lookup = new HashMap<>();

    /* ------------------------------------------------------------- *
     * Static Initialization
     * ------------------------------------------------------------- */

    static {
        lookup.put("cooks", "Cook");
        lookup.put("maids", "Maid");
        lookup.put("childDayCares", "Child Day Care");
        lookup.put("dailyNewsPapers", "Daily Newspaper");
        lookup.put("carBikeCleaners", "Car/Bike Cleaner");
        lookup.put("drivers", "Driver");
        lookup.put("laundries", "Laundry");
        lookup.put("milkmen", "Milkman");
    }

    /* ------------------------------------------------------------- *
     * Getter
     * ------------------------------------------------------------- */

    public static String get(String dailyServiceType) {
        return lookup.get(dailyServiceType);
    }

    public static String getKeyByValue(String dailyServiceType) {
        for (Map.Entry dailyService : lookup.entrySet()) {
            if (dailyService.getValue().equals(dailyServiceType)) {
                return dailyService.getKey().toString();
            }
        }
        return "";
    }
}
