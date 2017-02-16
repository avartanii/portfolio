import java.util.Arrays;

public class AdvancedChangeMaker {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        } else if (args.length < 2) {
            System.out.println("Invalid number of arguments.");
            System.out.println();
            printUsage();
            return;
        }

        try {
            int amount = Integer.parseInt(args[args.length - 1]);
            if (amount < 0) {
                System.out.println("Change cannot be made for negative amounts.");
                System.out.println();
                printUsage();
                return;
            }

            int[] denominations = new int[args.length - 1];

            for (int i = 0; i < denominations.length; i++) {
                denominations[i] = Integer.parseInt(args[i]);
                if (denominations[i] <= 0) {
                    System.out.println("Denominations must all be greater than zero.");
                    System.out.println();
                    printUsage();
                    return;
                }

                for (int j = 0; j < i; j++) {
                    if (denominations[j] == denominations[i]) {
                        System.out.println("Duplicate denominations are not allowed.");
                        System.out.println();
                        printUsage();
                        return;
                    }
                }
            }

            Tuple change = makeOptimalChange(denominations, amount);
            if (change.isImpossible()) {
                System.out.println("It is impossible to make " + amount + " cents with those denominations.");
            } else {
                int coinTotal = change.total();
                System.out.println(amount + " cents can be made with " + coinTotal + " coin" +
                    getSimplePluralSuffix(coinTotal) + " as follows:");

                for (int i = 0; i < denominations.length; i++) {
                    int coinCount = change.getElement(i);
                    System.out.println("- "  + coinCount + " " + denominations[i] + "-cent coin" +
                        getSimplePluralSuffix(coinCount));
                }
            }
        } catch (NumberFormatException nfe) {
            System.out.println("Denominations and amount must all be integers.");
            System.out.println();
            printUsage();
        }
    }

    public static Tuple makeOptimalChange(int[] denominations, int amount) {
        Tuple[][] table = new Tuple[denominations.length][amount + 1];
        int[] addendArray = new int[denominations.length];

        //Create initial zeros
        for (int i = 0; i < denominations.length; i++) {
            table[i][0] = new Tuple(denominations.length);
        }

        for (int y = 0; y < denominations.length; y++) {
            for (int x = 1; x < amount + 1; x++) {
                if (x >= denominations[y]) {
                    addendArray[y] = 1;
                    Tuple addend1 = new Tuple(addendArray);
                    Tuple addend2;
                    if (x - denominations[y] >= 0) {
                        if (!table[y][x - denominations[y]].isImpossible()) {
                            addend2 = new Tuple(table[y][x - denominations[y]].getElements());
                        } else {
                            addend2 = Tuple.IMPOSSIBLE;
                        }
                    } else {
                        addend2 = Tuple.IMPOSSIBLE;
                    }
                    if (!addend2.isImpossible()) {
                        table[y][x] = addend1.add(addend2);
                        if (y > 0 && !table[y - 1][x].isImpossible()) {
                            table[y][x] = (table[y][x].total() > table[y - 1][x].total()) || (table[y][x].isImpossible() && !table[y - 1][x].isImpossible()) ? table[y - 1][x] : table[y][x];
                        }
                    } else {
                        table[y][x] = Tuple.IMPOSSIBLE;
                    }
                } else {
                    if (y > 0 && !table[y - 1][x].isImpossible()) {
                        table[y][x] = !table[y - 1][x].isImpossible() ? table[y - 1][x] : table[y][x];
                    } else {
                        table[y][x] = Tuple.IMPOSSIBLE;
                    }
                }
                if (y > 0 && !table[y - 1][x].isImpossible()) {
                    table[y][x] = (table[y][x].total() > table[y - 1][x].total()) || (table[y][x].isImpossible() && !table[y - 1][x].isImpossible()) ? table[y - 1][x] : table[y][x];
                }
                addendArray[y] = 0;

            }
        }

        if (!table[denominations.length - 1][amount].isImpossible()) {
            return table[denominations.length - 1][amount];
        }

        return Tuple.IMPOSSIBLE;
    }

    private static void printUsage() {
        System.out.println("Usage: java AdvancedChangeMaker <coin denomination in cents>... <amount in cents>");
    }

    private static String getSimplePluralSuffix(int count) {
        return count == 1 ? "" : "s";
    }

}