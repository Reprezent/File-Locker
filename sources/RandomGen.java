// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 2

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomGen
{
    private class BitSieve
    {


    }
    private static final BigInteger TWO = new BigInteger("2");
    static BigInteger Generate(int numBits)
    {
        SecureRandom gen = new SecureRandom();
        BigInteger rv;
        do
        {
            // Just set the bit then test, rather than generate a prime until
            rv = new BigInteger(numBits, gen).setBit(numBits - 1);
        }
        // Test if the top most bit is set.
        while(!isPrime(rv));

        return rv.abs();
    }


    // Miller-Rabian Primality Test
    static boolean isPrime(BigInteger val, long numIterations)
    {
        SecureRandom gen = new SecureRandom();
        if(val.equals(BigInteger.ONE) || val.equals(BigInteger.ZERO))
        {
            return false;
        }

        if(val.equals(TWO) || val.equals(BigInteger.valueOf(3)))
        {
            return true;
        }

        long numDivisions = 0;
        BigInteger oddTester = val.subtract(BigInteger.ONE);
        while(oddTester.mod(TWO).equals(BigInteger.ZERO))
        {
            oddTester = oddTester.shiftRight(1);
            numDivisions++;
        }

        for(long i = 0; i < numIterations; i++)
        {
            BigInteger a;
            do
            {
                a = new BigInteger(val.subtract(BigInteger.ONE).bitLength(), gen);
            }
            while(a.compareTo(TWO) < 0 || a.compareTo(val.subtract(BigInteger.ONE)) > 0);
            BigInteger x = a.modPow(oddTester, val);
            if(x.equals(BigInteger.ONE) || x.equals(val.subtract(BigInteger.ONE)))
            {
                continue;
            }

            int j = 0;
            for(; j < numDivisions; j++)
            {
                x = x.modPow(TWO, val);
                if(x.equals(BigInteger.ONE))
                    return false;
                if(x.equals(val.subtract(BigInteger.ONE)))
                    break;
            }
            if(j == numDivisions)
            {
                return false;
            }
        }

        return true;
    }

    static boolean isPrime(BigInteger val)
    {
        return isPrime(val, 100);
    }
};

