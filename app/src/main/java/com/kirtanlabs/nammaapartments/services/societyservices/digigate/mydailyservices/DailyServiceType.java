package com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices;

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

    // Reverse-dailyServiceLookup map for getting a day from an abbreviation
    private static final Map<String, String> dailyServiceLookup = new HashMap<>();

    /* ------------------------------------------------------------- *
     * Static Initialization
     * ------------------------------------------------------------- */

    static {
        dailyServiceLookup.put("cooks", "Cook");
        dailyServiceLookup.put("maids", "Maid");
        dailyServiceLookup.put("childDayCares", "Child Day Care");
        dailyServiceLookup.put("dailyNewsPapers", "Daily Newspaper");
        dailyServiceLookup.put("carBikeCleaners", "Car/Bike Cleaner");
        dailyServiceLookup.put("drivers", "Driver");
        dailyServiceLookup.put("laundries", "Laundry");
        dailyServiceLookup.put("milkmen", "Milkman");
    }

    /* ------------------------------------------------------------- *
     * Getter
     * ------------------------------------------------------------- */

    public static String get(String dailyServiceType) {
        return dailyServiceLookup.get(dailyServiceType);
    }

    public static String getKeyByValue(String dailyServiceType) {
        for (Map.Entry dailyService : dailyServiceLookup.entrySet()) {
            if (dailyService.getValue().equals(dailyServiceType)) {
                return dailyService.getKey().toString();
            }
        }
        return "";
    }
}
