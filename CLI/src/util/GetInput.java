package util;

import java.util.Scanner;


public class GetInput {

    private static Scanner scanner = new Scanner(System.in);

    public int getInt(String prompt) {
        try {
            System.out.print(prompt);
            return Integer.parseInt(scanner.nextLine());

        } catch (NumberFormatException e) {
            System.out.println("Invalid input, expect Integer !");
            return -1;
        }
    }

    public String getStr(String prompt) {
        try {
            System.out.print(prompt);
            return scanner.nextLine();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input, expect String !");
            return null;
        }
    }
}
