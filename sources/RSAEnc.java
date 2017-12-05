// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 2

class RSAEnc {
	public RSAEnc(String [] args){
		//creates a cmd line arg parser for enc/dec (false)
		RSACommandLineArgParser cmd = new RSACommandLineArgParser(args, false);

			RSA rsa = new RSA(cmd.getKeyFile(), true);
			rsa.encrypt(cmd.getInputFile(), cmd.getOutputFile());
	}

	public static void main(String[] args){
		new RSAEnc(args);
	}
}
