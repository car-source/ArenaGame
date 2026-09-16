import java.util.Scanner;

public class Main {

    static final int MAX_HEALTH = 100;
    static final int STARTING_GOLD = 20;
    static final int ROWS = 5;
    static final int COLS = 11;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        printTitle();

        String playerName = readName(in);

        int difficulty = readChoice(in, 1, 3, "Difficulty (1 = easy, 2 = normal, 3 = brutal)");
        System.out.println("Difficulty selected: " + difficultyName(difficulty));
        System.out.println("");

        int health = MAX_HEALTH;
        int gold = STARTING_GOLD;
        int level = 1;
        boolean alive = true;
        double critChance = 0.15;
        int potions = 2;

        String enemyName = "Cave Goblin";
        int enemyHealth = 30 + difficulty * 15;
        int enemyPower = 4 + difficulty * 3;

        int playerRow = 2, playerCol = 1;
        int enemyRow = 2, enemyCol = 9;

        System.out.printf("%-12s HP %3d/%3d  Gold %4d  Lv %d%n",
                          playerName, health, MAX_HEALTH, gold, level);
        System.out.printf("Alive %-5b  Crit %.0f%%%n", alive, critChance * 100);
        System.out.println("");

        System.out.printf("%s enters the arena. The %s has %d HP.%n",
                          playerName, enemyName, enemyHealth);
        System.out.print("Press Enter to begin...");
        in.nextLine();
        System.out.println("");

        countdown(3);
        System.out.println("");

        System.out.println(enemyName.toUpperCase() + " blocks your path!");
        System.out.printf("Opponent %-14s HP %3d  Power %2d%n",
                          enemyName, enemyHealth, enemyPower);
        System.out.println("Name length: " + enemyName.length());

        boolean isBoss = enemyName.contains("Dragon");
        System.out.println("Boss fight: " + isBoss);

        if (enemyName.equalsIgnoreCase("cave goblin")) {
            System.out.println("You have fought one of these before.");
        }
        System.out.println("");

        int damage = enemyPower * 2;
        health -= damage;
        System.out.println("You take " + damage + " damage. Health: " + health);

        int potion = 15;
        health += potion;
        level++;
        System.out.println("You drink a potion. Health: " + health);
        System.out.println("You reach level " + level + ".");
        System.out.println("");

        int hits = 3;
        int swings = 7;

        int brokenAccuracy = hits / swings * 100;
        System.out.println("Accuracy (broken): " + brokenAccuracy + "%");

        double acc1 = (double) hits / swings * 100;
        double acc2 = hits * 100.0 / swings;

        System.out.printf("Accuracy (cast):    %.1f%%%n", acc1);
        System.out.printf("Accuracy (reorder): %.1f%%%n", acc2);
        System.out.println("");

        int turn = 6;
        boolean enrages = (turn % 3 == 0);
        System.out.println("Turn " + turn + " — enrages: " + enrages);
        System.out.println("");

        double critDamage = damage * 1.75;
        int applied = (int) critDamage;
        System.out.println("Crit damage (double): " + critDamage);
        System.out.println("Crit damage (int):    " + applied);
        System.out.println("Lost to the cast:     " + (critDamage - applied));
        System.out.println("");

        int turnNumber = 1;
        boolean playing = true;
        boolean fled = false;

        while (playing) {
            printBanner("Turn " + turnNumber);
            System.out.printf("%-12s HP %3d/%3d    %-14s HP %3d%n",
                              playerName, health, MAX_HEALTH, enemyName, enemyHealth);
            System.out.println("");

            drawArena(playerRow, playerCol, enemyRow, enemyCol);

            boolean adjacent = isAdjacent(playerRow, playerCol, enemyRow, enemyCol);
            int roll = (turnNumber * 3) % 10 + 1;
            int damage2 = 0;

            String action = readAction(in, adjacent, enemyName);

            switch (action) {
                case "A" -> damage2 = attack(adjacent, enemyPower, roll);
                case "L" -> playerCol = moveLeft(playerCol);
                case "R" -> playerCol = moveRight(playerCol, enemyCol, enemyName);
                case "D" -> health = defend(health);
                case "P" -> {
                    if (potions > 0) {
                        potions--;
                        health = drinkPotion(health);
                    } else {
                        System.out.println("You reach for a potion. There are none.");
                    }
                }
                case "F" -> fled = flee();
                default -> System.out.println("The crowd jeers. You hesitate and lose the turn.");
            }

            System.out.printf("You have %d %s left.%n",
                              potions, potions == 1 ? "potion" : "potions");

            String condition = health > MAX_HEALTH / 2 ? "steady" : "faltering";
            System.out.println("You look " + condition + ".");
            System.out.println("");

            enemyHealth = applyDamage(enemyHealth, damage2);
            health = enemyResponse(fled, adjacent, health, enemyHealth, enemyPower, enemyName);

            if (swings > 0 && hits / swings > 0.5) {
                System.out.println("Your aim is holding up.");
            }
            if (health < MAX_HEALTH / 4 && gold >= 10) {
                System.out.println("You should buy a potion.");
            }

            printHealthBar(health);

            playing = !endOfFight(fled, health, enemyHealth, enemyName, turnNumber);

            turnNumber++;
        }
        System.out.printf("%nThe arena empties after %d turns.%n", turnNumber - 1);
    }

    // =========================================================================
    // METHODS
    // =========================================================================

    static void printTitle() {
        System.out.print("""
                ========================
                    THE ARENA
                ========================
                """);
        System.out.println("Sand, torchlight, and a crowd that has already decided how this ends.");
        System.out.println("The gate opens.");
        System.out.println("");
    }

    static void countdown(int from) {
        for (int i = from; i > 0; i--) {
            System.out.println(i + "...");
        }
        System.out.println("FIGHT!");
    }

    static void printBanner(String text) {
        System.out.println("=".repeat(40));
        System.out.println(text);
    }

    static void printHealthBar(int hp) {
        int bars = hp / 5;
        String bar = "#".repeat(bars) + "-".repeat(20 - bars);
        System.out.printf("[%s] %d%%%n", bar, hp);
    }

    static boolean isAlive(int hp) {
        return hp > 0;
    }

    static int calculateDamage(int power, int roll) {
        if (roll >= 9) {
            return power * 2;
        } else if (roll >= 3) {
            return power;
        } else {
            return 0;
        }
    }

    static int calculateDamage(int power, int roll, double critMultiplier) {
        if (roll >= 9) {
            return (int) (power * critMultiplier);
        } else if (roll >= 3) {
            return power;
        } else {
            return 0;
        }
    }

    static int applyDamage(int hp, int damage) {
        return hp - damage;
    }

    static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    static void drawArena(int playerRow, int playerCol, int enemyRow, int enemyCol) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (r == playerRow && c == playerCol)      System.out.print('@');
                else if (r == enemyRow && c == enemyCol)   System.out.print('X');
                else if (r == 0 || r == ROWS - 1)          System.out.print('-');
                else if (c == 0 || c == COLS - 1)          System.out.print('|');
                else                                       System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println("");
    }

    static int readChoice(Scanner in, int min, int max, String prompt) {
        int choice;
        do {
            System.out.print(prompt + ": ");
            while (!in.hasNextInt()) {
                System.out.print("Numbers only. Try again: ");
                in.next();
            }
            choice = in.nextInt();
        } while (choice < min || choice > max);
        in.nextLine();
        return choice;
    }

    static String readName(Scanner in) {
        System.out.print("What is your name, challenger? ");
        String name = in.nextLine().trim();
        if (name.isEmpty()) {
            name = "Challenger";
        }
        return name;
    }

    static String difficultyName(int difficulty) {
        return switch (difficulty) {
            case 1 -> "Easy";
            case 2 -> "Normal";
            case 3 -> "Brutal";
            default -> "Unknown";
        };
    }

    static boolean isAdjacent(int r1, int c1, int r2, int c2) {
        return (r1 == r2) && (Math.abs(c1 - c2) == 1);
    }

    static String readAction(Scanner in, boolean adjacent, String enemyName) {
        if (adjacent) {
            System.out.print("[A]ttack  [D]efend  [P]otion  [L]eft  [R]ight  [F]lee: ");
        } else {
            System.out.print("The " + enemyName + " is out of reach.  "
                             + "[L]eft  [R]ight  [D]efend  [P]otion  [F]lee: ");
        }
        return in.nextLine().trim().toUpperCase();
    }

    static int attack(boolean adjacent, int enemyPower, int roll) {
        if (!adjacent) {
            System.out.println("You swing at empty air. Get closer first.");
            return 0;
        }
        int damage = calculateDamage(enemyPower, roll);
        if (roll >= 9) {
            System.out.println("CRITICAL HIT!");
        } else if (roll >= 3) {
            System.out.println("A solid hit.");
        } else {
            System.out.println("You miss.");
        }
        return damage;
    }

    static int moveLeft(int playerCol) {
        if (playerCol - 1 < 1) {
            System.out.println("The wall stops you.");
            return playerCol;
        }
        System.out.println("You step left.");
        return playerCol - 1;
    }

    static int moveRight(int playerCol, int enemyCol, String enemyName) {
        if (playerCol + 1 > COLS - 2) {
            System.out.println("The wall stops you.");
            return playerCol;
        } else if (playerCol + 1 == enemyCol) {
            System.out.println("The " + enemyName + " blocks your way.");
            return playerCol;
        }
        System.out.println("You step right.");
        return playerCol + 1;
    }

    static int defend(int health) {
        System.out.println("You raise your guard and recover 5 HP.");
        return health + 5;
    }

    static int drinkPotion(int health) {
        System.out.println("You drink a potion and recover 25 HP.");
        return health + 25;
    }

    static boolean flee() {
        System.out.println("You run for the gate. The crowd howls.");
        return true;
    }

    static int enemyResponse(boolean fled, boolean adjacent, int health,
                             int enemyHealth, int enemyPower, String enemyName) {
        if (!fled && isAlive(enemyHealth) && adjacent) {
            health -= enemyPower;
            System.out.printf("The %s strikes back for %d.%n", enemyName, enemyPower);
        }
        return clamp(health, 0, MAX_HEALTH);
    }

    static boolean endOfFight(boolean fled, int health, int enemyHealth,
                              String enemyName, int turnNumber) {
        if (fled) {
            System.out.println("You escape with your life, and nothing else.");
            return true;
        } else if (!isAlive(enemyHealth)) {
            System.out.printf("%nThe %s falls! You win on turn %d.%n", enemyName, turnNumber);
            return true;
        } else if (!isAlive(health)) {
            System.out.printf("%nYou have fallen on turn %d.%n", turnNumber);
            return true;
        }
        return false;
    }
}
