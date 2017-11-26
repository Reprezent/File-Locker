// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 3

class RSAValidate {
	public RSAValidate(String [] args){
		//creates a cmd line arg parser for enc/dec (false)
		CommandLineArgParser cmd = new CommandLineArgParser(args, "validate");

		try{
			RSA rsa = new RSA(cmd.getKeyFile(), true);
			rsa.validate(cmd.getMsgFile(), cmd.getSigFile());
		}catch(java.io.IOException e) { System.err.println(e.getMessage()); 
		}catch(java.security.NoSuchAlgorithmException e) { System.err.println(e.getMessage()); }
	}

	public static void main(String[] args){
		new RSAValidate(args);
	}
}
