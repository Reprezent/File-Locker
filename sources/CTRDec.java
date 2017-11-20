// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 1

import java.util.Arrays;
import java.security.*;
import java.util.ArrayList;
import java.nio.file.Files;
import java.io.IOException;
import java.lang.UnsupportedOperationException;
import java.lang.InterruptedException;
import java.lang.SecurityException;
import java.lang.Integer;
import java.lang.Math.*;
import java.nio.ByteBuffer;

class CTRDec
{
    public CTRDec(String[] args)
    {
        CommandLineArgParser cmd_args = new CommandLineArgParser(args);
        byte[] data = null, iv = null, key = null;
        try
        {
            data = Files.readAllBytes(cmd_args.getInputFile());
            System.err.println("Data length: " + Integer.toString(data.length));
            key = utils.hexStringToBinary(Files.readAllBytes(cmd_args.getKeyFile()));
        }
        catch(IOException e) { System.err.println(e.getMessage()); }
 
		//parse iv
		iv = Arrays.copyOf(data, AES.blocksize());
		//utils.printByteArr(iv);
		SharedData decrypted = new SharedData(data.length - iv.length);
		
		int numBlocks = data.length / AES.blocksize();
		if (data.length % AES.blocksize() != 0) 
			numBlocks++;
		System.err.println("Number of Blocks: " + Integer.toString(numBlocks));
		System.err.println("Encrypted Message Size: " + decrypted.getOutput().length);
		
		ArrayList<CTR> decThreads = new ArrayList<CTR>();
		
		//Create threads, one per block, starting after first block (iv)
		for (int i = 1; i < numBlocks; i++) {
			int dec_pos = (i-1) * AES.blocksize();
			int msg_pos = i * AES.blocksize();
			
			byte[] msg = Arrays.copyOfRange(data, msg_pos, Math.min(data.length, msg_pos + AES.blocksize()));
			CTR t = new CTR(msg, key, Arrays.copyOf(iv,AES.blocksize()), dec_pos, decrypted);
			decThreads.add(t);
			t.start();
			try {
				iv = utils.addOne(iv);
			}
			catch (Exception e) {
				System.err.println("iv overflow");
				utils.printByteArr(iv);
				System.err.println(i);
				System.exit(1);
			}
		}
		//utils.printByteArr(iv);
		
		//join threads
		for (CTR t : decThreads) {
			try {
				t.join();
			}
			catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}

        try
        {
            Files.write(cmd_args.getOutputFile(), decrypted.getOutput());
        }
        catch(IOException e)                   { System.err.println(e.getMessage()); }
        catch(UnsupportedOperationException e) { System.err.println(e.getMessage()); }
        catch(SecurityException e)             { System.err.println(e.getMessage()); }

    }

    public static void main(String[] args)
    {
        new CTRDec(args);
    }
}
