import java.util.Scanner;

/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all the display based on the messages it receives from the Town object. <p>
 *
 * This code has been adapted from Ivan Turner's original program -- thank you, Mr. Turner!
 */

public class TreasureHunter {
    // static variables
    private static final Scanner SCANNER = new Scanner(System.in);

    // instance variables
    private Town currentTown;
    private Hunter hunter;
    private static final String[] treasures = {"crown", "trophy", "gem", "dust"};
    private String treasure;
    private boolean searchedTown;
    private boolean hardMode;
    private boolean testMode;
    private boolean win;

    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter() {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        testMode = false;
        treasure = "";
        win = false;
    }

    /**
     * Starts the game; this is the only public method
     */
    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer() {
        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = SCANNER.nextLine().toLowerCase();

        // set hunter instance variable
        hunter = new Hunter(name, 10);

        System.out.print("Choose mode (e, n or h): ");
        String hard = SCANNER.nextLine().toLowerCase();
        if (hard.equals("h")) {
            hardMode = true;
        } else if (hard.equals("test")) {
            testMode = true;
            hunter.changeGold(90);
            String[] items = {"water", "rope", "boots", "machete", "horse", "boat"};
            int[] costs = {2, 4, 6, 6, 12, 20};
            for (int i = 0; i < items.length; i++) {
                hunter.changeGold(costs[i]);
                hunter.buyItem(items[i], costs[i]);
            }
        }
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown() {
        double markdown = 0.25;
        double toughness = 0.4;
        if (hardMode) {
            // in hard mode, you get less money back when you sell items
            markdown = 0.5;

            // and the town is "tougher"
            toughness = 0.75;
        }
        treasure = treasures[(int) (Math.random() * 4)];
        searchedTown = false;
        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu() {
        String choice = "";

        while (!choice.equals("x") && !hunter.isBroke() && !win) {
            System.out.println();
            System.out.println(currentTown.getLatestNews());
            System.out.println("***");
            System.out.println(hunter);
            System.out.println(currentTown);
            System.out.println("(B)uy something at the shop.");
            System.out.println("(S)ell something at the shop.");
            System.out.println("(M)ove on to a different town.");
            System.out.println("(L)ook for trouble!");
            System.out.println("(H)unt for treasure!");
            System.out.println("Give up the hunt and e(X)it.");
            System.out.println();
            System.out.print("What's your next move? ");
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
        }
        if (hunter.isBroke()) {
            System.out.println(currentTown.getLatestNews());
            System.out.println("Game Over! You can't pay your debt!");
        } else if (win) {
            System.out.println("Congratulations, you have found the last of the three treasures, you win!");
        }
    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown()) {
                // This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("h")) {
            if (!searchedTown) {
                System.out.println("You found a " + treasure + "!");
                if (hunter.hasItemInContainer(treasure, hunter.getCollection())) {
                    System.out.println("You already have this treasure!");
                }
                boolean added = hunter.addTreasure(treasure);
                if (!added) {
                    System.out.println("You did not add the " + treasure + ".");
                    System.out.println("You want to keep your collection clean.");
                } else {
                    win = determineWin();
                }
                searchedTown = true;
            } else {
                System.out.println("You have already searched this town!");
            }
        } else if (choice.equals("x")) {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        } else {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }

    /**
     * Determines whether a game has been won or not.
     *
     * @return whether the win condition is met or not
     */
    private boolean determineWin() {
        String[] treasureCollection = hunter.getCollection();
        for (String treasure : treasureCollection) {
            if (treasure == null) {
                return false;
            }
        }
        return true;
    }
}