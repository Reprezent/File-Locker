// Alex Riedel, J.T. Liso, Sean Whalen
// COSC 583 Fall 2017
// Programming Assignment 2


import java.security.SecureRandom;
import java.lang.Integer;
import java.util.Arrays;
import java.math.BigInteger;

class random_bytes
{
    public static String generate(int numBytes)
    {
        byte[] array = new byte[numBytes];
        SecureRandom gen = new SecureRandom();
        gen.nextBytes(array);
        BigInteger a = new BigInteger(array);

        return a.abs().toString();
    }


    public static void main(String[] args)
    {
        if(args.length == 0)
        {
            System.out.println("usage: random_bytes numBytes");
            return;
        }
        System.out.print(generate(Integer.parseInt(args[0])));
    }
}
