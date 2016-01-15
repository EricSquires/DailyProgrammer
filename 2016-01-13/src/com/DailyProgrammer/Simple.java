package com.DailyProgrammer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Eric on 1/14/2016.
 */
public class Simple {
    private static Random rand = new Random();
    private static int lowChar = 32;
    private static int charRange = 126 - lowChar;

    private final int numChildren;
    private final int numSelected;
    private final double mutateChance;

    private FitComparator comparator;

    public Simple(int numChildren, int numSelected, double mutateChance) {
        this.numChildren = numChildren;
        this.numSelected = numSelected;
        this.mutateChance = mutateChance;
    }

    public int getFit(String target) {
        System.out.format("Finding fit for '%s' with %d children, %d selected per generation and a %f%% mutation chance", target, numChildren, numSelected, mutateChance);
        comparator = new FitComparator(target);

        String[] currentGen = null;
        int generation = 0;
        int printGen = 1;

        do {
            currentGen = spawnGeneration(currentGen, target);
            generation++;

            if(generation % printGen == 0) {
                printGen *= 2;
                System.out.println("Generation " + generation + ": Max fitness (" + comparator.fitness(currentGen[0], target) + ") " + Arrays.toString(currentGen));
            }
        }
        while(!currentGen[0].equals(target));

        System.out.println("Generation " + generation + ": Max fitness (" + comparator.fitness(currentGen[0], target) + ") " + Arrays.toString(currentGen));
        return generation;
    }

    private String[] spawnGeneration(String target) {
        String[] population = new String[numChildren];

        for(int i = 0; i < numChildren; i++) {
            population[i] = getRandString(target.length());
        }

        return getFittest(population);
    }

    private String[] spawnGeneration(String[] parents, String target) {
        if(parents == null) {
            return spawnGeneration(target);
        }

        String[] children = new String[parents.length * numChildren];

        for(int i = 0; i < parents.length; i++) {
            for(int j = 0; j < numChildren; j++) {
                children[i * j] = mutate(parents[i], target);
            }
        }

        return getFittest(children);
    }

    private String[] getFittest(String[] population) {
        java.util.Arrays.sort(population, comparator);
        String[] ret = new String[numSelected];

        for(int i = 0; i < numSelected; i++) {
            ret[i] = population[i];
        }

        return ret;
    }

    @NotNull
    private String mutate(String txt, String target) {
        StringBuilder builder = new StringBuilder(txt);

        for(int i = 0; i < builder.length(); i++) {
            if(rand.nextDouble() > mutateChance) {
                continue;
            }

            char currentChar = builder.charAt(i);

            char nextChar = getRandChar();

            while(nextChar == currentChar) {
                nextChar = getRandChar();
            }

            builder.setCharAt(i, nextChar);
        }

        return builder.toString();
    }

    private static char getRandChar() {
        return (char)(rand.nextInt(charRange) + lowChar);
    }

    @NotNull
    private static String getRandString(int length) {
        StringBuilder builder = new StringBuilder(length);

        for(int i = 0; i < length; i++) {
            builder.append(getRandChar());
        }

        return builder.toString();
    }

    private class FitComparator implements Comparator<String> {
        private final String target;

        private FitComparator(String target) {
            this.target = target;
        }

        @Override
        public int compare(String o1, String o2) {
            int num1 = fitness(o1, target);
            int num2 = fitness(o2, target);

            return num1 - num2;
        }

        public int fitness(String current, String target) {
            if(current == null || target == null) {
                return Integer.MAX_VALUE;
            }

            int fit = 0;

            for(int i = 0; i < target.length(); i++) {
                fit += charFitness(current.charAt(i), target.charAt(i));
            }

            return fit;
        }

        @Contract(pure = true)
        public int charFitness(char current, char target) {
            return Math.abs(current - target);
        }
    }
}
