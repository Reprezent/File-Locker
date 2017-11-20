// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 1

import java.net.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

// Thread used by CTREnc and CTRDec
public class CTR extends Thread
{
	byte[] msg;
	byte[] key;
	byte[] iv;
	int pos;
	SharedData data;

	public CTR(byte[] msg, byte[] key, byte[] iv, int pos, SharedData outputArray)
	{
		this.msg = msg;
		this.key = key;
		this.iv = iv;
		this.pos = pos;
		this.data = outputArray;
	}
	public void run()
	{
		byte[] iv_enc = AES.Encrypt(iv, key);
		byte[] buffer = new byte[AES.blocksize()];
		int i = 0;
		for (i = 0; i < msg.length; i++) {
			try {
				buffer[i] = (byte)(iv_enc[i] ^ msg[i]);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				System.err.println("I is " + Integer.toString(i));
			}
		}

		data.insertBlock(pos, Arrays.copyOf(buffer,i));
	}
}
