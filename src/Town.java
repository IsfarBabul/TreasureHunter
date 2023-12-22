/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String mode;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, String mode) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.mode = mode;

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item;
            if (hunter.hasItemInContainer(terrain.getNeededItem(), hunter.getKit())) {
                item = terrain.getNeededItem();
            } else {
                item = terrain.getSecondaryNeededItem();
            }
            printMessage = "You used your " + item + " to cross the " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET + ".";
            if (checkItemBreak()) {
                if (!mode.equals("e") && !item.equals("sword")) {
                    hunter.removeItemFromContainer(item, hunter.getKit());
                    printMessage += "\nUnfortunately, you lost your " + item + ".";
                }
            }

            return true;
        }

        if (terrain.getSecondaryNeededItem() != null) {
            printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + Colors.CYAN + terrain.getNeededItem() + " or a " + terrain.getSecondaryNeededItem() + Colors.CYAN + ".";
        } else {
            printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + Colors.CYAN + terrain.getNeededItem() + Colors.CYAN + ".";
        }
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "You left the shop.";
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            if (mode.equals("e")) {
                noTroubleChance = .4;
            } else {
                noTroubleChance = 0.66;
            }

        } else {
            if (mode.equals("e")) {
                noTroubleChance = .15;
            } else {
                noTroubleChance = 0.33;
            }
        }

        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = "You want trouble, stranger!" + Colors.RED + " You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (mode.equals("s") && hunter.hasItemInContainer("sword", hunter.getKit())) {
                noTroubleChance = 0.042;
                double strikeBack = Math.random();
                if (toughTown) {
                    if (strikeBack < 0.09) {
                        noTroubleChance = 1;
                    }
                } else {
                    if (strikeBack < 0.06) {
                        noTroubleChance = 1;
                    }
                }
            }
            double playerStrike = Math.random();
            if (playerStrike > noTroubleChance) {
                if (mode.equals("s") && hunter.hasItemInContainer("sword", hunter.getKit())) {
                    printMessage += Colors.GREEN + "Ahh, this stranger has a sword! This guy's for real!" + Colors.RESET + " Here, take my gold. I'm outta here!";
                    printMessage += Colors.GREEN + "\nYou intimidated the brawler and receive " + Colors.YELLOW + goldDiff + Colors.GREEN + " gold. Nice going." + Colors.RESET;
                } else {
                    printMessage += "Okay, stranger! You proved yer mettle." + Colors.RESET + " Here, take my gold.";
                    printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                }
                hunter.changeGold(goldDiff);
            } else {
                if (mode.equals("s") && hunter.hasItemInContainer("sword", hunter.getKit())) {
                    printMessage += Colors.GREEN + "This stranger's got a sword! This guy's not playing fair!" + Colors.BLUE + " Freeze, don't move or I'll shoot! If you don't play fair I won't either. Now Pay Up!";
                    if (playerStrike < 0.042) {
                        printMessage += "\nYou fled the scene and they missed their gunshot and pay nothing." + Colors.RESET;
                    } else {
                        printMessage += "\nYou were held at gunpoint and pay " + Colors.YELLOW + goldDiff + Colors.GREEN + " gold. Just wow." + Colors.RESET;
                    }
                } else {
                    printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET;
                    printMessage += "\nYou lost the brawl and pay " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                }
                if (playerStrike >= 0.042) {
                    hunter.changeGold(-goldDiff);
                }
            }
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET + ".";

    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random() * 2;
        if (rnd < .33) {
            return new Terrain("Mountains", "Rope", null);
        } else if (rnd < .66) {
            return new Terrain("Ocean", "Boat", null);
        } else if (rnd < .99) {
            return new Terrain("Plains", "Horse", null);
        } else if (rnd < 1.33) {
            return new Terrain("Desert", "Water", null);
        } else if (rnd < 1.66) {
            return new Terrain("Jungle", "Machete", "sword");
        } else {
            return new Terrain("Marsh", "Boots", null);
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }
}