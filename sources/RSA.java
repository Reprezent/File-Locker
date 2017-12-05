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
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Arrays;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

public class RSA {
	private String ca;

    private RSAKey private_key;
    private RSAKey public_key;

	//reads in key file and generates RSA class based on that
	RSA(String path, boolean enc)
    {
        if(enc)
            public_key = RSAKey.readKey(path);
        else
            private_key = RSAKey.readKey(path);
	}

	//generates RSA with n bits for N
	RSA(int n, String auth)
    {
		ca = auth;
		SecureRandom rand = new SecureRandom();

		//generate p and q
		//  BigInteger p = BigInteger.probablePrime(numBits/2, rand); 
		//  BigInteger q = BigInteger.probablePrime(numBits/2, rand);
		BigInteger p = RandomGen.Generate(n/2);
		BigInteger q = RandomGen.Generate(n/2);

		// N = p*q
		BigInteger N = p.multiply(q);

		// order of N = (p-1)(q-1)
		BigInteger order =  p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));

		//calculate e that is coprime to order
		//e = RandomGen.Generate(order.bitLength());
		BigInteger e = new BigInteger("5");

		//looping until the gcd between order and e is 1
		while(order.gcd(e).compareTo(BigInteger.ONE) != 0) {
			e = e.add(BigInteger.ONE);
		}

		
		//making e mod the order of the group if e >= order
		if(order.compareTo(e) != 1)
			e = e.mod(order);

		//d is the modInverse of e with respect to order
		BigInteger d = e.modInverse(order);

        public_key = new RSAKey(N, e, n);
        private_key = new RSAKey(N, d, n);
	}

    private String readFile(String input_file)
    {
		String currLine = null;
        try(BufferedReader reader = new BufferedReader(new FileReader(input_file)))
        {
            currLine = reader.readLine();
        }
        catch(IOException e)
        {
			System.err.println("ERROR: input file read");
            System.err.println(e);
            System.exit(1);
        }


		// Exiting on bad input.
		if(currLine == null){
			System.err.println("ERROR: input file read");
			System.exit(1);
		}

        return currLine;
    }

    private void writeFile(String output_file, String output)
    {

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(output_file)))
        {
		    writer.write(output);
		    writer.newLine();
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }


    public BigInteger encrypt(BigInteger msg)
    {
		SecureRandom rand = new SecureRandom();
		// Padding.
		byte [] m = msg.toByteArray();

        int numRandBytes = public_key.getNumberOfBits() / 8 - 3 - m.length;

        // System.err.printf("Number of random bytes: %d%nNumber of random bits %d%n", numRandBytes, this.numBits);
        // System.err.printf("Number of bits in the input: %8d%n", msg.bitLength());
        // System.err.printf("Number of bits in the key:   %8d%n", this.numBits);
        // System.err.printf("Number of random bytes:      %8d%n", numRandBytes);

		byte [] r = new byte[numRandBytes];
		byte [] message = new byte[3 + r.length + m.length];
		// System.out.println(Integer.toString(3+r.length+m.length));
		// System.out.println(Integer.toString(this.numBits / 8));
		boolean wasZeroByte = true;

		if (message.length > public_key.getNumberOfBits()/8) {
			System.err.println("ERROR: message length exceeds N length, try a larger key or smaller message\n");
			return null;
		}
		
		// make sure no random byte is 0x00
		while(wasZeroByte) {
			wasZeroByte = false;
			rand.nextBytes(r);
			for (byte b : r) {
				if (b == (byte)0x00) {
					wasZeroByte = true;
					break;
				}
			}
		}

		// concat elements into message
		int i=0;
		message[i++] = (byte)0x00;
		message[i++] = (byte)0x02;
		for (byte b : r) 
			message[i++] = b;
		message[i++] = (byte)0x00;
		for (byte b : m)
			message[i++] = b;

		// Encrypt integer representation of message.
		BigInteger padded = new BigInteger(message);
		// System.out.println(padded.toString());

		// Use our own modPow implementation.
		BigInteger encrypted = modPow.compute(padded, public_key.getFactor(), public_key.getN()); //padded.modPow(e,N);

        return encrypted;
    }


	public void encrypt(String input, String output){
		BigInteger msg = new BigInteger(readFile(input));
        BigInteger encrypted = encrypt(msg);

		// Writing encrypted message.
        writeFile(output, encrypted.toString());
	}

    public BigInteger decrypt(BigInteger msg)
    {
		//decrypting using our own modPow implemenation
		BigInteger decrypted = modPow.compute(msg, private_key.getFactor(), private_key.getN()); //msg.modPow(d, N);
		//System.err.println(decrypted.toString());

		//unpadding
		byte [] padded = decrypted.toByteArray();
		if (padded.length < 3) {
			System.err.println("ERROR: decrypted message is empty, no decryption written");
			return null;
		}
		//System.out.println(Integer.toString(padded.length));
		// skip the FIRST byte, why not first two (0x00 and 0x02)?
		// answer: the prepended 0x00 byte disappears 
		// with conversion to bigInt bc it has 
		// no effect on int val, the 0x02 does however
		int i;
		for (i=1; i<padded.length; i++) {
			if (padded[i] == (byte)0x00)
				break;
		}
		// I didn't incr i again to avoid 
		// array out of bound error possiblities
		// and again 0x00 prepended doesn't change val
		// anyway
		byte [] m = Arrays.copyOfRange(padded, i, padded.length);
		BigInteger decUnpadded  = new BigInteger(m);
        

        return decUnpadded;
    }
	public void decrypt(String input, String output)
    {
		String currLine = readFile(input);

		BigInteger msg = new BigInteger(currLine);
        BigInteger decUnpadded = decrypt(msg);

		// Writing decrypted message.
        writeFile(output, decUnpadded.toString());
	}

	public void writepubKey(String path)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path)))
        {
            // First line contains number of bits in N.
            writer.write(Integer.toString(public_key.getNumberOfBits()));
            writer.newLine();

            // Second line contains N.
            writer.write(public_key.getN().toString());
            writer.newLine();

            // Third line contains e.
            writer.write(public_key.getFactor().toString());
            writer.newLine();
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
	}

	public void writesecKey(String path)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path)))
        {
            // First line contains number of bits in N.
            writer.write(Integer.toString(private_key.getNumberOfBits()));
            writer.newLine();

            // Second line contains N.
            writer.write(private_key.getN().toString());
            writer.newLine();

            // Third line contains d.
            writer.write(private_key.getFactor().toString());
            writer.newLine();
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
	}

	public void sign(String msgFile, String sigFile)
    {
        sign(Paths.get(msgFile), Paths.get(sigFile));
	}

    // Signs the message using SHA256 hash, returns a BigInteger representing the signature
    public void sign(Path msgFile, Path sigFile)
    {
		byte[] msg = null;
        try
        {
            msg = Files.readAllBytes(msgFile);
        }
        catch(IOException e) { System.err.println(e); }

		MessageDigest digest = null;
        try
        {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch(NoSuchAlgorithmException e)
        {
            System.err.println(e);
            System.exit(1);
        }
		byte[] hash = digest.digest(msg);

		BigInteger hashed_int = new BigInteger(hash);

		// computing H(m)^d mod N
	    BigInteger mod = modPow.compute(hashed_int, private_key.getFactor(), private_key.getN());	

        try { Files.write(sigFile, mod.toByteArray()); }
        catch(IOException e) { System.err.println(e); }

    }


    public boolean validateB(String msgFile, String sigFile)
    {
        return validate(Paths.get(msgFile), Paths.get(sigFile));
    }


    public boolean validate(Path msgFile, Path sigFile)
    {
		BigInteger signature = null;
        try
        {
            signature = new BigInteger(Files.readAllBytes(sigFile));
        }
        catch(IOException e) { System.err.println(e); }

		//computes signature^e mod N
		BigInteger vt = modPow.compute(signature, public_key.getFactor(), public_key.getN());

		byte[] msg = null;
        try
        {
            msg = Files.readAllBytes(msgFile);
        }
        catch(IOException e) { System.err.println(e); }

		//hashing the message file and computing modN to verify the signature
		MessageDigest digest = null;
        try { digest = MessageDigest.getInstance("SHA-256"); }
        catch(NoSuchAlgorithmException e) { System.err.println(e); }
		byte[] hash = digest.digest(msg);
		BigInteger tm = new BigInteger(hash);
		tm = tm.mod(public_key.getN());

		return vt.equals(tm);
    }

	public void validate(String msgFile, String sigFile)
    {
		BigInteger signature = new BigInteger(readFile(sigFile));

		//computes signature^e mod N
		BigInteger vt = modPow.compute(signature, public_key.getFactor(), public_key.getN());

		byte[] msg = null;
        try
        {
            msg = Files.readAllBytes(Paths.get(msgFile));
        }
        catch(IOException e) { System.err.println(e); }

		//hashing the message file and computing modN to verify the signature
		MessageDigest digest = null;
        try { digest = MessageDigest.getInstance("SHA-256"); }
        catch(NoSuchAlgorithmException e) { System.err.println(e); }
		byte[] hash = digest.digest(msg);
		BigInteger tm = new BigInteger(hash);
		tm = tm.mod(public_key.getN());


		//checking if the tag equals the hash of the original message
		if(vt.equals(tm))
			System.out.println("True");
		else
			System.out.println("False");

	}

	public void casign(String pubKeyFile, String secKeyFile)
    {
		String sigFile = pubKeyFile + "-casig";

		//user provided cert authority
		if(ca != null){
			RSA caRSA = new RSA(ca, false);
			caRSA.sign(pubKeyFile, sigFile);
		}else{ //sign itself
			RSA caRSA = new RSA(secKeyFile, false);
			caRSA.sign(pubKeyFile, sigFile);
		}
	}

    public int getNumberOfBits()
    {
        if(private_key == null)
            return public_key.getNumberOfBits();
        else
            return private_key.getNumberOfBits();
    }

}
