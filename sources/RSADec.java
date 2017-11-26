// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 2

class RSADec {
	public RSADec(String [] args){
		//creates a cmd line arg parser for enc/dec (false)
		RSACommandLineArgParser cmd = new RSACommandLineArgParser(args, false);

		try{
			RSA rsa = new RSA(cmd.getKeyFile(), false);
			rsa.decrypt(cmd.getInputFile(), cmd.getOutputFile());
		}catch(java.io.IOException e) { System.err.println(e.getMessage()); }
	}

	public static void main(String[] args){
		new RSADec(args);
	}
}
