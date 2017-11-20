// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 2

class RSAEnc {
	public RSAEnc(String [] args){
		//creates a cmd line arg parser for enc/dec (false)
		CommandLineArgParser cmd = new CommandLineArgParser(args, false);

		try{
			RSA rsa = new RSA(cmd.getKeyFile(), true);
			rsa.encrypt(cmd.getInputFile(), cmd.getOutputFile());
		}catch(java.io.IOException e) { System.err.println(e.getMessage()); }
	}

	public static void main(String[] args){
		new RSAEnc(args);
	}
}
