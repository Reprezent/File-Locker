// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 3




class FileLocker {
	public FileLocker(String d, String p, String pr, String v){
		directory = d;
		pubKeyFile = p;
		privKeyFile = pr;
		validateKey = v;
        manifestFile = d + "/key.manifest";

        this.key = genKey();
        if(!verifyKey(privKeyFile, pubKeyFile))
        {
            System.err.println("ERROR: Key pairing not valid.\n Exiting...");
            System.exit(1);
        }
        
        writeKey(d);

        

	}


    public byte[] genKey()
    {
        return (new SecureRandom()).nextBytes(AES.blocksize());
    }


    // After readint eh writeup this doesn't sound right,
    // but I honestly cannot understand what the writeup is saying
    // anyways.
    public boolean verifyKey(String privKey, String pubKey)
    {
        SecureRandom rand = new SecureRandom();
        RSA priv = new RSA(privKey, false);
        RSA pub = new RSA(pubKey, true);
        BigInteger a = new BigInteger(rand.generateBytes(Math.min(32, priv.getNumberOfBits() - 56));

        BigInteger temp = pub.encrypt(a);
        BigInteger cmp = priv.encrypt(temp);

        return temp == a;
    }

    public void writeKey(String dir)
    {
        final char[] hex_str = "0123456789ABCDEF".toCharArray();
        char[] hex = new char[AES.blocksize() * 2];
        for(int i = 0; i < key.length; i++)
        {
            hex[i*2]     = hex_str[key[i] >>> 4];
            hex[i*2 + 1] = hex_str[key[i] & 0x0F];
        }

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(manifestFile)))
        {
		    writer.write(hex);
		    writer.newLine();
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }

    private String mainfestFile;
	private String directory;
	private String pubKeyFile;
	private String privKeyFile;
	private String validateKey;
    private byte[] key;
}
