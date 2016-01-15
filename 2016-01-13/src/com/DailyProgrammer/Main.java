package com.DailyProgrammer;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String target;

        if(args.length == 0) {
            System.out.print("Target word: ");

            Scanner scanner = new Scanner(System.in);
            target = scanner.nextLine();
        }
        else {
            target = args[0];
        }

        Simple simpleFit = new Simple(10, 5, 0.05);

        System.out.println(simpleFit.getFit(target) + " generations to find a match");
    }
}
