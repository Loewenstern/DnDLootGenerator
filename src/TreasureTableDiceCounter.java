//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class TreasureTableDiceCounter {
    static double in = 8512000.0/15.0;
    static String closestDice;
    static int closestDiceCount;
    static double closestDistance = 9999;

    public static void main(String[] args) {
        calc("d4", 2.5);
        calc("d6", 3.5);
        calc("d8", 4.5);
        calc("d10", 5.5);
        calc("d12", 6.5);
        calc("d20", 10.5);
        System.out.println(closestDiceCount + closestDice + " Distanz: " + closestDistance);
    }

    public static void calc(String dice, double d){
        int upper;
        int lower;
        double tmp;
        d = d * 10000;
        lower = (int)(in / d);
        upper = lower + 1;
        tmp = in - (lower * d);
        if (tmp < closestDistance && lower <= 20){
            closestDistance = tmp;
            closestDice = dice;
            closestDiceCount = lower;
        }
        tmp = (upper * d) - in;
        if (tmp < closestDistance && upper <= 20){
            closestDistance = tmp;
            closestDice = dice;
            closestDiceCount = upper;
        }
    }
}