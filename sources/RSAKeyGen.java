// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 2

class RSAKeyGen {
	public RSAKeyGen(String [] args){
		//creates a cmd line arg parser for key gen (true)
		RSACommandLineArgParser cmd = new RSACommandLineArgParser(args, true);

		RSA rsa = new RSA(cmd.numBits(), cmd.authority());

			rsa.writepubKey(cmd.pubKeyFile());
			rsa.writesecKey(cmd.secKeyFile());
			rsa.casign(cmd.pubKeyFile(), cmd.secKeyFile());
	}

	public static void main(String[] args){
		new RSAKeyGen(args);
	}
}
