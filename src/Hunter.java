/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you, Mr. Turner!
 */

public class Hunter {
    //instance variables
    private String hunterName;
    private String[] kit;
    private String[] collection;
    private int gold;
    private boolean broke;

    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param hunterName The hunter's name.
     * @param startingGold The gold the hunter starts with.
     */
    public Hunter(String hunterName, int startingGold) {
        this.hunterName = hunterName;
        kit = new String[6]; // only 5 possible items can be stored in kit
        collection = new String[3];
        gold = startingGold;
    }

    /**
     * Returns if Hunter is broke.
     * @return status of the Hunter on if they're broke or not
     */
    public boolean isBroke() {
        return broke;
    }

    //Accessors
    public String getHunterName() {
        return hunterName;
    }

    /**
     *
     * @return collection
     */
    public String[] getKit() {
        return kit;
    }

    /**
     *
     * @return collection
     */
    public String[] getCollection() {
        return collection;
    }

    /**
     * Updates the amount of gold the hunter has.
     *
     * @param modifier Amount to modify gold by.
     */
    public void changeGold(int modifier) {
        gold += modifier;
        if (gold < 0) {
            broke = true;
        }
    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem The cost of the item.
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if (costOfItem == 0 || gold < costOfItem || hasItemInContainer(item, kit)) {
            return false;
        }

        gold -= costOfItem;
        addItem(item, kit);
        return true;
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItemInContainer(item, kit)) {
            return false;
        }

        gold += buyBackPrice;
        removeItemFromContainer(item, kit);
        return true;
    }

    /**
     * Removes an item from the container by setting the index of the item to null.
     *
     * @param item The item to be removed.
     * @param container The container to remove the item from.
     */
    public void removeItemFromContainer(String item, String[] container) {
        int itmIdx = findItemInContainer(item, container);

        // if item is found
        if (itmIdx >= 0) {
            container[itmIdx] = null;
        }
    }

    /**
     * Checks to make sure that the item is not already in the container.
     * If not, it assigns the item to an index in the container with a null value ("empty" position).
     *
     * @param item The item to be added to the container.
     * @param container The container the item gets added to.
     * @return true if the item is not in the container and has been added.
     */
    private boolean addItem(String item, String[] container) {
        if (!hasItemInContainer(item, container)) {
            int idx = emptyPositionInContainer(container);
            container[idx] = item;
            return true;
        }

        return false;
    }

    public boolean addTreasure(String treasure) {
        if (!treasure.equals("dust")) {
            addItem(treasure, collection);
            return true;
        }
        return false;
    }

    /**
     * Checks if the container Array has the specified item.
     *
     * @param item The search item
     * @param container The search container
     * @return true if the item is found.
     */
    public boolean hasItemInContainer(String item, String[] container) {
        for (String tmpItem : container) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }

        return false;
    }

     /**
     * Returns a printable representation of the container, which
     * is a list of the items in kit or collection, with a space between each item.
     *
     * @return The printable String representation of the container.
     */
    public String getContainer(String[] container) {
        String printableContainer = Colors.PURPLE + "";
        String space = " ";

        for (String item : container) {
            if (item != null) {
                printableContainer += item + space;
            }
        }

        return printableContainer;
    }


    /**
     * @return A string representation of the hunter.
     */
    public String toString() {
        String str = hunterName + " has " + Colors.YELLOW + gold + Colors.RESET + " gold";
        if (!containerIsEmpty(kit)) {
            str += " and " +  Colors.PURPLE + getContainer(kit) + Colors.RESET;
        }
        str += "\nTreasures found:";
        if (!containerIsEmpty(collection)) {
            str += " a " +  Colors.PURPLE + getContainer(collection) + Colors.RESET;
        } else {
            str += " none";
        }
        return str;
    }

    /**
     * Searches container Array for the index of the specified value.
     *
     * @param item String to look for.
     * @param container Array to look through.
     * @return The index of the item, or -1 if not found.
     */
    private int findItemInContainer(String item, String[] container) {
        for (int i = 0; i < container.length; i++) {
            String tmpItem = container[i];

            if (item.equals(tmpItem)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Check if the kit is empty - meaning all elements are null.
     *
     * @param container Array to look through.
     * @return true if kit is completely empty.
     */
    private boolean containerIsEmpty(String[] container) {
        for (String string : container) {
            if (string != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Finds the first index where there is a null value.
     *
     * @return index of empty index, or -1 if not found.
     */
    private int emptyPositionInContainer(String[] container) {
        for (int i = 0; i < container.length; i++) {
            if (container[i] == null) {
                return i;
            }
        }

        return -1;
    }
}