// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 1
//

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;


class AES
{

    // Function Name: 
    //
    // Param: 
    // Param: 
    //
    // Return: 
    //
    public static byte[] Encrypt(byte[] data, byte[] key)
    {
        return mode(data, key, Cipher.ENCRYPT_MODE);
    }

    // Function Name: 
    //
    // Param: 
    // Param: 
    //
    // Return: 
    //
    public static byte[] Decrypt(byte[] data, byte[] key)
    {
        return mode(data, key, Cipher.DECRYPT_MODE);
    }

    // Function Name: 
    //
    // Param: 
    // Param: 
    // Param: 
    //
    // Return: 
    //
    private static byte[] mode(byte[] data, byte[] key, int opmode)
    {
        try
        {
            Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
            c.init(opmode, new SecretKeySpec(key, "AES"));
            return c.doFinal(data);
        }
        catch(IllegalArgumentException e)  { System.err.println(e.getMessage()); }
        catch(NoSuchAlgorithmException e)  { System.err.println(e.getMessage()); }
        catch(NoSuchPaddingException e)    { System.err.println(e.getMessage()); }
        catch(InvalidKeyException e)       { System.err.println(e.getMessage()); }
        catch(IllegalBlockSizeException e) { System.err.println(e.getMessage()); }
        catch(BadPaddingException e)       { System.err.println(e.getMessage()); }

        return null;
    }


    public static int blocksize()
    {
        try
        {
            return Cipher.getInstance("AES/ECB/NoPadding").getBlockSize();
        }
        catch(NoSuchAlgorithmException e)
        {

        }
        catch(NoSuchPaddingException e)
        {

        }
        return 0;
    }
    
}
