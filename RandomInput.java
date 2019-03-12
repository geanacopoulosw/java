import java.util.Random;

// Prints a bunch of random integers.  Maximum value MAX_VAL.
// Number to print is first command-line argument.
public class RandomInput {

    public static int MAX_VAL = 10000;

    public static void main(String[] args) {
        Random rng = new Random();
        if (args.length < 1) {
            System.err.println("usage:  java RandomInput [nums_to_generate]");
            System.exit(0);
        }
        int nums_to_generate = new Integer(args[0]);
        for (int i = 0; i < nums_to_generate; i++) {
            // nextInt max is exclusive, so add 1
            System.out.println(rng.nextInt(MAX_VAL+1));
        }
    }
}
