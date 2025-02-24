package com.api.formSync.util;


public class Log {
    public static void green(String... strings) {
        System.out.print("\u001B[32m");
        for (String str : strings) {
            System.out.print(str + " ");
        }
        reset();
    }

    public static void red(String... strings) {
        System.out.print("\u001B[31m");
        for (String str : strings) {
            System.out.print(str + " ");
        }
        reset();
    }

    public static void blue(String... strings) {
        System.out.print("\u001B[34m");
        for (String str : strings) {
            System.out.print(str + " ");
        }
        reset();
    }

    public static void yellow(String... strings) {
        System.out.print("\u001B[33m");
        for (String str : strings) {
            System.out.print(str + " ");
        }
        reset();
    }

    private static void reset() {
        System.out.println("\u001B[0m");
    }
}


