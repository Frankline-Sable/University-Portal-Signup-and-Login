package FrontEnd;

import java.util.Scanner;

/**
 * Created by Frankline Sable on 11/30/2016.
 */
public class testing_names {

    public static void main(String[] args){

        Scanner scan=new Scanner(System.in);

        System.out.println("Enter your name:\t");
        String name=scan.nextLine();
        String name2[]=name.split(" ");

        System.out.println("Name original:\t"+name);
        System.out.println("Name Formatted:\t"+name2[0]);
    }
}
