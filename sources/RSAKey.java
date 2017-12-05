// Alex Riedel, J.T. Liso, Sean Whalen
// COSC 583 Fall 2017
// Programming Assignment 2


import java.lang.Math;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class RSAKey
{
    public RSAKey(BigInteger N, BigInteger factor, int numBits)
    {
        this.N = N;
        this.factor = factor;
        this.numBits = numBits;
    }
    
    public static RSAKey readKey(String path)
    {
        BigInteger N = null, factor = null;
        int numBits = 0;
		String currLine = null;
		try(BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
		    for(int i = 0; i < 3; i++)
            {
			    currLine = reader.readLine();
                //exiting if missing info
                if(currLine == null)
                {
                    System.err.printf("ERROR: invalid key file, line %d%n", i+1);
                    System.exit(1);
                }

                switch(i){
                    case 0: //first line num bits
                        numBits = Integer.parseInt(currLine);
                        break;
                    case 1: //second line N
                        N = new BigInteger(currLine);
                        break;
                    case 2: //third line e or d
                        factor = new BigInteger(currLine);
                        break;
                    default: //should never get here, included for completeness
                        break;
                }
            }
        }
        catch(IOException e)
        {
            System.err.printf("ERROR: invalid key file");
            System.err.println(e);
            System.exit(1);
        }

        return new RSAKey(N, factor, numBits);
    }

    public BigInteger getN()
    {
        return N;
    }

    public BigInteger getFactor()
    {
        return factor;
    }

    public int getNumberOfBits()
    {
        return numBits;
    }

	private BigInteger N;
	private BigInteger factor;
	private int numBits;
}
