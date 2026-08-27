import java.util.Scanner;

public class Main {

    static final int MAX_HEALTH = 100;
    static final int STARTING_GOLD = 20;

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

        System.out.println("The arena awaits.");
        System.out.println("Sand, torchlight, and a crowd that has already decided how this ends.");
        System.out.println("The gate opens.");
        System.out.println("");

        String playerName = "Nameless";
        int health = MAX_HEALTH;
        int gold = STARTING_GOLD;
        int level = 1;
        boolean alive = true;
        double critChance = 0.15;

        String enemyName = "Cave Goblin";
        int enemyHealth = 40;
        int enemyPower = 7;

        System.out.printf("%-12s HP %3d/%3d Gold %4d Lv %d%n",
                playerName,
                health,
                MAX_HEALTH,
                gold,
                level);
        System.out.printf("Alive %-5b Crit %.0f%% ", alive, critChance * 100);
        System.out.println("Crit chance: " + critChance);
        System.out.println("");

        System.out.println("Shout name:       " + enemyName.toUpperCase());
        System.out.println("Character count:  " + enemyName.length());
        System.out.println("Is boss fight?    " + enemyName.contains("Dragon"));
        System.out.println("Is cave goblin?   " + enemyName.equalsIgnoreCase("cave goblin"));

        System.out.println("Opponent: " + enemyName);
        System.out.println("Health: " + enemyHealth);
        System.out.println("Power: " + enemyPower);
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

        String middle = null;

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

        int bars = health / 5;
        String bar = "#".repeat(bars) + "-".repeat(20 - bars);
        System.out.printf("[%s] %d%%%n", bar, health);
    }
}
