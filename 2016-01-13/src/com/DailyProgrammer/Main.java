package com.DailyProgrammer;

import java.util.Random;

public class Main {
    private static Random rand = new Random();

    public static void main(String[] args) {
        String target;

        if(args.length == 0) {
            System.out.print("Target word: ");
            target = System.console().readLine();
        }
        else {
            target = args[0];
        }

        StringBuilder builder = new StringBuilder();
        int fit;
        int lastFit = Integer.MAX_VALUE;
        String lastGuess;

        do {
            buildGuess(builder, target.length());
            fit = fitness(builder.toString(), target);

        }
        while(fit > 0);
    }
}
