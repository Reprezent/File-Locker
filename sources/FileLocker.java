// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 3


import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.stream.Stream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;


import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;


class FileLocker {
	public FileLocker(String d, String p, String pr, String v){
		directory = d;
		pubKeyFile = p;
		privKeyFile = pr;
		validateKey = v;
        manifestFile = d + "/key.manifest";
    }

    void lock()
    {
        this.key = genKey();

        if(!verifyKey(privKeyFile, pubKeyFile))
        {
            System.err.println("ERROR: Key pairing not valid.\n Exiting...");
            System.exit(1);
        }
        
        priv = new RSA(privKeyFile, false);
        pub = new RSA(pubKeyFile, true);
        // Need this since our RSA only signs with priavte keys.
        // Or not because its dumb.
        // RSA sign_pub = new RSA(pubKey, false);

        writeKey();
        priv.sign(manifestFile, manifestFile + ".sig");

        encryptAllFiles();
        signAllFiles();
    }

    void encryptAllFiles()
    {
        try(Stream<Path> files = Files.walk(Paths.get(directory)))
        {
            // For each file encrypt them and then replace them with thier own file.
            files.forEach(x ->
            { 
                try
                {
                    Files.write(x, CBC.encrypt(Files.readAllBytes(x), this.key));
                }
                catch(IOException e)
                {
                    System.err.println(e);
                }
                
            });
            
        }
        catch(IOException e)
        {
            System.err.println(e);
        }

    }


    void signAllFiles()
    {
        try(Stream<Path> files = Files.walk(Paths.get(directory)))
        {
            // For each file sign the file and store it in file_name.sig
            files.forEach(x -> { priv.sign(x, Paths.get(x.toFile().getName() + ".sig")); });
        }
        catch(IOException e)
        {
            System.err.println(e);
        }

    }

    public byte[] genKey()
    {
        byte[] rv = new byte[AES.blocksize()];
        new SecureRandom().nextBytes(rv);
        return rv;
    }


    // After readint eh writeup this doesn't sound right,
    // but I honestly cannot understand what the writeup is saying
    // anyways.
    public boolean verifyKey(String privKey, String pubKey)
    {
        SecureRandom rand = new SecureRandom();
        byte[] buf = new byte[Math.min(32, priv.getNumberOfBits() - 56) / 8];
        rand.nextBytes(buf);
        BigInteger a = new BigInteger(buf);

        BigInteger temp = pub.encrypt(a);
        BigInteger cmp = priv.encrypt(temp);

        return temp == a;
    }

    private static char[] toHex(byte[] msg)
    {
        final char[] hex_str = "0123456789ABCDEF".toCharArray();
        char[] hex = new char[msg.length * 2];
        for(int i = 0; i < msg.length; i++)
        {
            hex[i*2]     = hex_str[msg[i] >>> 4];
            hex[i*2 + 1] = hex_str[msg[i] & 0x0F];
        }

        return hex;
    }

    public void writeKey()
    {
        BigInteger enc_key = pub.encrypt(new BigInteger(key));
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(manifestFile)))
        {
		    writer.write(enc_key.toString());
		    writer.newLine();
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }

    private RSA priv, pub;
    private String manifestFile;
	private String directory;
	private String pubKeyFile;
	private String privKeyFile;
	private String validateKey;
    private byte[] key;
}
