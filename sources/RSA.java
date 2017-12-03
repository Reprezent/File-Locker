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
import java.util.Arrays;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class RSA {
	private BigInteger N;
	private BigInteger order;
	private int numBits;

	private BigInteger e;
	private BigInteger d;

	private String ca;

	//reads in key file and generates RSA class based on that
	RSA(String path, boolean enc) throws java.io.IOException{
		BufferedReader reader = new BufferedReader(new FileReader(path));

			
		String currLine;

		for(int i = 0; i < 3; i++){
			currLine = reader.readLine();
			
			//exiting if missing info
			if(currLine == null){
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
					if(enc)
						e = new BigInteger(currLine);
					else
						d = new BigInteger(currLine);
                    break;
				default: //should never get here, included for completeness
					break;
			}
		}

		reader.close();
	}

	//generates RSA with n bits for N
	RSA(int n, String auth){
		ca = auth;
		numBits = n;
		SecureRandom rand = new SecureRandom();

		//generate p and q
		//  BigInteger p = BigInteger.probablePrime(numBits/2, rand); 
		//  BigInteger q = BigInteger.probablePrime(numBits/2, rand);
		BigInteger p = RandomGen.Generate(numBits/2);
		BigInteger q = RandomGen.Generate(numBits/2);

		// N = p*q
		N = p.multiply(q);

		// order of N = (p-1)(q-1)
		order =  p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));

		//calculate e that is coprime to order
		//e = RandomGen.Generate(order.bitLength());
		e = new BigInteger("5");

		//looping until the gcd between order and e is 1
		while(order.gcd(e).compareTo(BigInteger.ONE) != 0) {
			e = e.add(BigInteger.ONE);
		}

		
		//making e mod the order of the group if e >= order
		if(order.compareTo(e) != 1)
			e = e.mod(order);

		//d is the modInverse of e with respect to order
		d = e.modInverse(order);
	}

	public void encrypt(String input, String output) throws java.io.IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		BufferedReader reader = new BufferedReader(new FileReader(input));
		SecureRandom rand = new SecureRandom();
		String currLine = reader.readLine();

		//exiting on bad input
		if(currLine == null){
			System.err.println("ERROR: input file read");
			System.exit(1);
		}

		reader.close();

		BigInteger msg = new BigInteger(currLine);

		//padding
		byte [] m = msg.toByteArray();
		//int numRandBytes = (int)Math.round((double)this.numBits / 8) - 3 - m.length;
        int numRandBytes = this.numBits / 8 - 3 - m.length;

       // System.err.printf("Number of random bytes: %d%nNumber of random bits %d%n", numRandBytes, this.numBits);
/*       System.err.printf("Number of bits in the input: %8d%n", msg.bitLength());
       System.err.printf("Number of bits in the key:   %8d%n", this.numBits);
       System.err.printf("Number of random bytes:      %8d%n", numRandBytes);
       */

		byte [] r = new byte[numRandBytes];
		byte [] message = new byte[3 + r.length + m.length];
		//System.out.println(Integer.toString(3+r.length+m.length));
		//System.out.println(Integer.toString(this.numBits / 8));
		boolean wasZeroByte = true;

		if (message.length > this.numBits/8) {
			System.err.println("ERROR: message length exceeds N length, try a larger key or smaller message\n");
			writer.close();
			return;
		}
		
		//make sure no random byte is 0x00
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

		//concat elements into message
		int i=0;
		message[i++] = (byte)0x00;
		message[i++] = (byte)0x02;
		for (byte b : r) 
			message[i++] = b;
		message[i++] = (byte)0x00;
		for (byte b : m)
			message[i++] = b;

		//encrypt integer representation of message
		BigInteger padded = new BigInteger(message);
		//System.out.println(padded.toString());
		// use our own modPow implementation
		BigInteger encrypted = modPow.compute(padded, e, N); //padded.modPow(e,N);

		//writing encrypted message    
		writer.write(encrypted.toString());
		writer.newLine();
		writer.close();
	}

	public void decrypt(String input, String output) throws java.io.IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		BufferedReader reader = new BufferedReader(new FileReader(input));

		String currLine = reader.readLine();

		//exiting on bad input
		if(currLine == null) {
			System.err.println("ERROR: input file read");
			System.exit(1);
		}

		reader.close();

		BigInteger msg = new BigInteger(currLine);
		
		//decrypting using our own modPow implemenation
		BigInteger decrypted = modPow.compute(msg, d, N); //msg.modPow(d, N);
		//System.err.println(decrypted.toString());

		//unpadding
		byte [] padded = decrypted.toByteArray();
		if (padded.length < 3) {
			System.err.println("ERROR: decrypted message is empty, no decryption written");
			writer.close();
			return;
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

		//writing decrypted message
		writer.write(decUnpadded.toString());
		writer.newLine();
		writer.close();
	}

	public void writepubKey(String path) throws java.io.IOException{ 
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		
		//first line contains number of bits in N
		writer.write(Integer.toString(numBits));
		writer.newLine();

		//second line contains N
		writer.write(N.toString());
		writer.newLine();

		//third line contains e
		writer.write(e.toString());
		writer.newLine();

		writer.close();
	}

	public void writesecKey(String path) throws java.io.IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));

		//first line contains number of bits in N
		writer.write(Integer.toString(numBits));
		writer.newLine();

		//second line contains N
		writer.write(N.toString());
		writer.newLine();

		//third line contains d
		writer.write(d.toString());
		writer.newLine();
		
		writer.close();
	}

	//signs the message using SHA256 hash, returns a BigInteger representing the signature
	public void sign(String msgFile, String sigFile) throws java.security.NoSuchAlgorithmException, java.io.IOException{
		Path path = Paths.get(msgFile);
		byte[] msg = Files.readAllBytes(path);

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(msg);

		BigInteger hashed_int = new BigInteger(hash);

		// computing H(m)^d mod N
	    BigInteger mod = modPow.compute(hashed_int, d, N);	

		BufferedWriter writer = new BufferedWriter(new FileWriter(sigFile));
		writer.write(mod.toString());
		writer.newLine();
		writer.close();
	}

	public void validate(String msgFile, String sigFile) throws java.security.NoSuchAlgorithmException, java.io.IOException{
		BufferedReader reader = new BufferedReader(new FileReader(sigFile));
		BigInteger signature = new BigInteger(reader.readLine());
		reader.close();

		//computes signature^e mod N
		BigInteger vt = modPow.compute(signature, e, N);

		Path path = Paths.get(msgFile);
		byte[] msg = Files.readAllBytes(path);

		//hashing the message file and computing modN to verify the signature
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(msg);
		BigInteger tm = new BigInteger(hash);
		tm = tm.mod(N);


		//checking if the tag equals the hash of the original message
		if(vt.equals(tm))
			System.out.println("True");
		else
			System.out.println("False");

	}

	public void casign(String pubKeyFile, String secKeyFile) throws java.io.IOException, java.security.NoSuchAlgorithmException{
		String sigFile = pubKeyFile + "-casig";

		BufferedWriter writer = new BufferedWriter(new FileWriter(sigFile));
		
		//user provided cert authority
		if(ca != null){
			RSA caRSA = new RSA(ca, false);
			caRSA.sign(pubKeyFile, sigFile);
		}else{ //sign itself
			RSA caRSA = new RSA(secKeyFile, false);
			caRSA.sign(pubKeyFile, sigFile);
		}
	}

}
