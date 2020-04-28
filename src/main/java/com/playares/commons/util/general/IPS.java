package com.playares.commons.util.general;

import java.util.regex.Pattern;

public final class IPS {
    /**
     * Converts provided host address to a long which can be stored efficiently in a database
     * @param address IP Address
     * @return Converted result
     */
    public static long toLong(String address) {
        if (address == null || address.isEmpty()) {
            return 0;
        }

        String[] octets = address.split(Pattern.quote("."));

        if (octets.length != 4) {
            return 0;
        }

        long result = 0;

        for (int i = 3; i >= 0; i--) {
            final long octet = Long.parseLong(octets[3 - i]);

            if (octet > 255 || octet < 0) {
                return 0;
            }

            result |= octet << (i * 8);
        }

        return result;
    }

    /**
     * Converts a previously stored IP Address back to String form
     * @param address IP Address as Long
     * @return String IP Address
     */
    public static String toString(long address) {
        if (address > 4294967295L || address < 0) {
            return null;
        }

        final StringBuilder result = new StringBuilder();

        for (int i = 3; i >= 0; i--) {
            final int shift = i * 8;

            result.append((address & (0xff << shift)) >> shift);

            if (i > 0) {
                result.append(".");
            }
        }

        return result.toString();
    }
}
