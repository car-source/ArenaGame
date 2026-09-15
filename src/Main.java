import java.util.Scanner;

public class Main {

    static final int MAX_HEALTH = 100;
    static final int STARTING_GOLD = 20;
    static final int ROWS = 5;
    static final int COLS = 11;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String title = """
                ========================
                    THE ARENA
                ========================
                """;
        System.out.print(title);

        System.out.println("Sand, torchlight, and a crowd that has already decided how this ends.");
        System.out.println("The gate opens.");
        System.out.println("");

        System.out.print("What is your name, challenger? ");
        String playerName = in.nextLine().trim();
        if (playerName.isEmpty()) {
            playerName = "Challenger";
        }

        int difficulty;
        do {
            System.out.print("Difficulty (1 = easy, 2 = normal, 3 = brutal): ");
            while (!in.hasNextInt()) {
                System.out.print("Numbers only. Try again: ");
                in.next();
            }
            difficulty = in.nextInt();
        } while (difficulty < 1 || difficulty > 3);
        in.nextLine();   // consume the leftover newline.

        String difficultyName = switch (difficulty) {
            case 1 -> "Easy";
            case 2 -> "Normal";
            case 3 -> "Brutal";
            default -> "Unknown";
        };
        System.out.println("Difficulty selected: " + difficultyName);
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

        for (int i = 3; i > 0; i--) {
            System.out.println(i + "...");
        }
        System.out.println("FIGHT!");
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

        while (playing) {
            System.out.println("=".repeat(40));

            System.out.printf("%n--- Turn %d ---%n", turnNumber);
            System.out.printf("%-12s HP %3d/%3d    %-14s HP %3d%n",
                              playerName, health, MAX_HEALTH, enemyName, enemyHealth);
            System.out.println("");

            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    char cell;
                    if (r == playerRow && c == playerCol) {
                        cell = '@';
                    } else if (r == enemyRow && c == enemyCol) {
                        cell = 'X';
                    } else if (r == 0 || r == ROWS - 1) {
                        cell = '-';
                    } else if (c == 0 || c == COLS - 1) {
                        cell = '|';
                    } else {
                        cell = ' ';
                    }
                    System.out.print(cell);
                }
                System.out.println();
            }
            System.out.println("");

            boolean adjacent = (playerRow == enemyRow)
                             && (Math.abs(playerCol - enemyCol) == 1);

            int roll = (turnNumber * 3) % 10 + 1;
            int damage2 = 0;

            System.out.println("Your move.");
            System.out.print("[A]ttack  [D]efend  [P]otion  [F]lee  [L]eft  [R]ight: ");
            String action = in.nextLine().trim().toUpperCase();

            switch (action) {
                case "A" -> {
                    if (!adjacent) {
                        System.out.println("You're too far away to strike.");
                    } else if (roll >= 9) {
                        damage2 = enemyPower * 2;
                        System.out.println("CRITICAL HIT!");
                    } else if (roll >= 3) {
                        damage2 = enemyPower;
                        System.out.println("A solid hit.");
                    } else {
                        damage2 = 0;
                        System.out.println("You miss.");
                    }
                }
                case "D" -> {
                    damage2 = 0;
                    health += 5;
                    System.out.println("You raise your guard and recover 5 HP.");
                }
                case "P" -> {
                    if (potions > 0) {
                        potions--;
                        health += 25;
                        System.out.println("You drink a potion and recover 25 HP.");
                    } else {
                        System.out.println("You reach for a potion. There are none.");
                    }
                }
                case "F" -> {
                    alive = false;
                    System.out.println("You run for the gate. The crowd howls.");
                }
                case "L" -> {
                    playerCol--;
                    System.out.println("You step left.");
                }
                case "R" -> {
                    playerCol++;
                    System.out.println("You step right.");
                }
                default -> System.out.println("The crowd jeers. You hesitate and lose the turn.");
            }

            if (playerCol < 1) {
                playerCol = 1;
            } else if (playerCol > COLS - 2) {
                playerCol = COLS - 2;
            }

            System.out.printf("You have %d %s left.%n",
                              potions, potions == 1 ? "potion" : "potions");

            String condition = health > MAX_HEALTH / 2 ? "steady" : "faltering";
            System.out.println("You look " + condition + ".");
            System.out.println("");

            enemyHealth -= damage2;
            if (alive && enemyHealth > 0 && adjacent) {
                health -= enemyPower;
                System.out.printf("The %s strikes back for %d.%n", enemyName, enemyPower);
            }

            if (swings > 0 && hits / swings > 0.5) {
                System.out.println("Your aim is holding up.");
            }
            if (health < MAX_HEALTH / 4 && gold >= 10) {
                System.out.println("You should buy a potion.");
            }

            if (health > MAX_HEALTH) {
                health = MAX_HEALTH;
            } else if (health < 0) {
                health = 0;
            }

            int bars = health / 5;
            String bar = "#".repeat(bars) + "-".repeat(20 - bars);
            System.out.printf("[%s] %d%%%n", bar, health);

            if (!alive) {
                System.out.println("You escape with your life, and nothing else.");
                playing = false;
            } else if (enemyHealth <= 0) {
                System.out.printf("%nThe %s falls! You win on turn %d.%n", enemyName, turnNumber);
                playing = false;
            } else if (health <= 0) {
                System.out.printf("%nYou have fallen on turn %d.%n", turnNumber);
                alive = false;
                playing = false;
            }

            turnNumber++;
        }
        System.out.printf("%nThe arena empties after %d turns.%n", turnNumber - 1);
    }
}
