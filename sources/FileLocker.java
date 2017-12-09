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

import java.io.FileOutputStream;
import java.util.Arrays;


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
        if(!verifyKey(pubKeyFile, validateKey))
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

    void unlock()
    {
        priv = new RSA(privKeyFile, false);
        this.key = decryptKey();
        removeKey();
        if(!verifyKey(pubKeyFile, validateKey))
        {
            System.err.println("ERROR: Key pairing not valid.\n Exiting...");
            System.exit(1);
        }

        pub = new RSA(pubKeyFile, true);

        
        verifyAllFiles();

        removeAllTags();

        decryptAllFiles();
    }

    void verifyAllFiles()
    {

        try(Stream<Path> files = Files.walk(Paths.get(directory)))
        {
            // For each file encrypt them and then replace them with thier own file.
            long i = 
            files
            .filter(Files::isRegularFile)
            .filter(x -> { return !x.toString().startsWith(manifestFile); })
            .filter(x -> !x.getFileName().toString().endsWith(".sig")) // Remove all signature fileSA
            .filter(x -> { return !pub.validate(x, Paths.get(x.toString() + ".sig")); })  // Remove all files that actully validate.
            .count();

            if(i != 0)
            {
                System.err.println("ERROR: Files do not validate");
                Files.walk(Paths.get(directory))
                .filter(Files::isRegularFile)
                .filter(x -> { return !x.toString().startsWith(manifestFile); })
                .filter(x -> !x.getFileName().toString().endsWith(".sig")) // Remove all signature fileSA
                .filter(x -> { return !pub.validate(x, Paths.get(x.toString() + ".sig")); })  // Remove all files that actully validate.
                .forEach( x ->
                {
                    System.err.println("    " + x.toString() + "    FAILED");
                });

                System.exit(1);
            }
            
        }
        catch(IOException e)
        {
            System.err.println(e);
        }


    }

    void removeAllTags()
    {
        try(Stream<Path> files = Files.walk(Paths.get(directory)))
        {
            // For each file encrypt them and then replace them with thier own file.
            // Obviously dumb if im encrypting other signature files.
            // Proper way to do this would be to have a signature->file manifest.
            files.filter(x -> x.getFileName().toString().endsWith(".sig")) // Remove all non signature files
            .forEach(x -> { try { Files.delete(x); } catch(IOException e) { System.err.println(e); } }); // Delete all files
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }


    void decryptAllFiles()
    {
        try(Stream<Path> files = Files.walk(Paths.get(directory)))
        {
            // For each file encrypt them and then replace them with thier own file.
            files
            .filter(Files::isRegularFile)
            .filter(x -> !x.getFileName().toString().endsWith(".sig")) // Remove all signature fileSA
            .filter(x -> { return !x.getFileName().toString().startsWith(manifestFile); })
            .forEach(x ->
            { 
                try
                {
                    Files.write(x, CBC.decrypt(Files.readAllBytes(x), this.key));
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
    void encryptAllFiles()
    {
        try(Stream<Path> files = Files.walk(Paths.get(directory)))
        {
            // For each file encrypt them and then replace them with thier own file.
            files
            .filter(Files::isRegularFile)
            .filter(x -> !x.getFileName().toString().endsWith(".sig")) // Remove all signature fileSA
            .filter(x -> { return !x.toString().endsWith(manifestFile); })
            .forEach(x ->
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
            files
            .filter(Files::isRegularFile)
            .filter(x -> !x.getFileName().toString().endsWith(".sig")) // Remove all signature fileSA
            .filter(x -> { return !x.getFileName().toString().startsWith(manifestFile); })
            .forEach(x -> { priv.sign(x, Paths.get(x.toString() + ".sig")); });
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


    public boolean verifyKey(String Key, String verifyKey)
    {
        String sig = Key + "-casig";
        RSA ver = new RSA(verifyKey, true);
        return ver.validateB(Key, sig);
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
        try (FileOutputStream fos = new FileOutputStream(manifestFile)) {
               fos.write(enc_key.toByteArray());
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }


    public byte[] decryptKey()
    {
        BigInteger decrypted = null;
        try{
        decrypted = priv.decrypt(new BigInteger(Files.readAllBytes(Paths.get(manifestFile))));
        }catch(IOException e) {}
        
        
        byte[] temp = decrypted.toByteArray();
        if(temp.length == 17 && temp[0] == 0x00)
        {
            // Remove Null Byte?
            temp = Arrays.copyOfRange(temp, 1, 17);
        }
            
        return Arrays.copyOf(temp, AES.blocksize());
    }

    public void removeKey()
    {
            try { Files.delete(Paths.get(manifestFile)); } catch(IOException e) { System.err.println(e); }
    }


    private RSA priv, pub, verify;
    private String manifestFile;
	private String directory;
	private String pubKeyFile;
	private String privKeyFile;
	private String validateKey;
    private byte[] key;
}
