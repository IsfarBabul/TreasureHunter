/**
 * The Terrain class is designed to represent the zones between the towns in the Treasure Hunter game.
 * This code has been adapted from Ivan Turner's original program -- thank you, Mr. Turner!
 */

public class Terrain {
    // instance variables
    private String terrainName;
    private String neededItem;
    private String secondaryNeededItem;

    /**
     * Sets the class member variables
     *
     * @param name The name of the zone.
     * @param item The item needed in order to cross the zone.
     */
    public Terrain(String name, String item, String secondaryItem) {
        terrainName = name;
        neededItem = item.toLowerCase();
        secondaryNeededItem = secondaryItem;
    }

    // accessors
    public String getTerrainName() {
        return terrainName;
    }

    public String getNeededItem() {
        return neededItem;
    }

    public String getSecondaryNeededItem() {
        return secondaryNeededItem;
    }

    /**
     * Guards against a hunter crossing the zone without the proper item.
     * Searches the hunter's inventory for the proper item and determines whether the hunter can cross.
     *
     * @param hunter The Hunter object trying to cross the terrain.
     * @return true if the Hunter has the proper item.
     */
    public boolean canCrossTerrain(Hunter hunter) {
        if (hunter.hasItemInContainer(neededItem, hunter.getKit()) || (hunter.hasItemInContainer(secondaryNeededItem, hunter.getKit()) && secondaryNeededItem != null)) {
            return true;
        }
        return false;
    }

    /**
     * @return A string representation of the terrain and item to cross it.
     */
    public String toString() {
        if (secondaryNeededItem != null) {
            return terrainName + " needs a(n) " + neededItem + " or a(n) " + secondaryNeededItem + " to cross.";
        }
        return terrainName + " needs a(n) " + neededItem + " to cross.";
    }
}