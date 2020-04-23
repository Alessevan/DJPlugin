package fr.bakaaless.DJPlugin.utils;

import org.bukkit.Location;

public class LocationUtils {

    public static boolean isSameLoc(final Location location1, final Location location2) {
        return location1.getX() == location2.getX() && location1.getY() == location2.getY() && location1.getZ() == location2.getZ();
    }
}
