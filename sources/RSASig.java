// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 2
//

import java.lang.Math;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


class RSASig
{
    public RSASig(String message, String key_file, String sig_file, boolean encrypt) throws java.io.IOException
    {
        RSA sig = new RSA(key_file, encrypt);
		BufferedReader reader = new BufferedReader(new FileReader(message));
        byte[] hashed_message = hash(message);
        sig.encrypt(new String(hashed_message), sig_file);
        

	}


    public static byte[] hash(String message)
    {
        
        try
        {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            return sha.digest(message.getBytes());
        }
        catch(NoSuchAlgorithmException e) { e.printStackTrace(); System.exit(-1); }
            
        return null;
    }

}
