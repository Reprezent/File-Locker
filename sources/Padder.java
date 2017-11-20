// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 1
//

import java.util.Arrays;
import java.lang.Integer;
import java.lang.Byte;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

class Padder
{

    // Function Name: pad 
    //
    // Param: data Plaintext to be encrypted.
    // Param: blockize Blocksize  of the cryptographic scheme.
    //
    // Return: byte[] Plaintext with padding added. 
    //
    static public byte[] pad(byte[] data, int blocksize)
    {

        Integer temp = new Integer(blocksize - data.length % blocksize);
		byte padlength = temp.byteValue();
        System.err.println("Data length: " + Integer.toString(data.length));
        System.err.println("Blocksize: " + Integer.toString(blocksize));
        System.err.println("Pad Length: " + Integer.toString(padlength));
		byte[] padded_data = Arrays.copyOf(data, data.length + temp.intValue());

        // Integer pad_len = new Integer(padlength);
		//padding the data with the padlength 
		for(int i = data.length; i < padded_data.length; i++){
			padded_data[i] = padlength;
		}

		return padded_data;
    }

    // Function Name: pad 
    //
    // Param: data Plaintext to be encrypted.
    // Param: blockize Blocksize  of the cryptographic scheme.
    //
    // Return: byte[] Plaintext with padding added. 
    //
    static public byte[] unpad(byte[] data)
    {	
        System.err.println("Data length: " + Integer.toString(data.length));
        System.err.println("Pad Length: " + Byte.toString(data[data.length - 1]));
		return Arrays.copyOf(data, data.length - data[data.length-1]);
    }
}
