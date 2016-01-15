package com.DailyProgrammer;

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

    private Comparator<String> comparator;

    public Simple(int numChildren, int numSelected, double mutateChance) {
        this.numChildren = numChildren;
        this.numSelected = numSelected;
        this.mutateChance = mutateChance;
    }

    public int getFit(String target) {
        comparator = new FitComparator(target);

        String[] currentGen = spawnGeneration(target);
        int generation = 0;

        while(currentGen[0] != target) {
            generation++;
            currentGen = spawnGeneration(currentGen, target);
        }

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
        String[] children = new String[parents.length * numChildren];

        for(int i = 0; i < parents.length; i++) {
            for(int j = 0; j < numChildren; j++) {
                children[i * j] = mutate(parents[i]);
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

    private String mutate(String txt) {
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

    private static String getRandString(int length) {
        StringBuilder builder = new StringBuilder(length);

        for(int i = 0; i < length; i++) {
            builder.setCharAt(i, getRandChar());
        }

        return builder.toString();
    }

    private class FitComparator implements Comparator<String> {
        private final String target;

        private FitComparator(String target) {
            this.target = target;
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this;
        }

        @Override
        public int compare(String o1, String o2) {
            int num1 = fitness(o1, target);
            int num2 = fitness(o2, target);

            return num1 - num2;
        }

        private int fitness(String current, String target) {
            int fit = 0;

            for(int i = 0; i < target.length(); i++) {
                fit += charFitness(current.charAt(i), target.charAt(i));
            }

            return fit;
        }

        private int charFitness(char current, char target) {
            return Math.abs(current - target);
        }
    }
}
