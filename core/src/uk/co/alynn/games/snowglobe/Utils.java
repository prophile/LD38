package uk.co.alynn.games.snowglobe;

import java.util.Random;

public abstract class Utils {
    private static final Random defaultRandom = new Random();

    public static int randomPoisson(double lambda) {
        return randomPoisson(lambda, defaultRandom);
    }

    public static int randomPoisson(double lambda, Random random) {
        // from Knuth
        double L = Math.exp(-lambda);
        int k = 0;
        double p = 1.0;

        do {
            ++k;
            p *= random.nextDouble();
        } while (p > L);

        return k - 1;
    }

    public static void main(String[] args) {
        double rate = 0.1;
        Random rng = new Random();
        for (int i = 0; i < 1000; ++i) {
            int output = randomPoisson(rate, rng);
            System.out.println(output);
        }
    }
}
