// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 3

class RSASign {
	public RSASign(String [] args){
		//creates a cmd line arg parser for enc/dec (false)
		CommandLineArgParser cmd = new CommandLineArgParser(args, "sign");

		try{
			RSA rsa = new RSA(cmd.getKeyFile(), false);
			rsa.sign(cmd.getMsgFile(), cmd.getSigFile());
		}catch(java.io.IOException e) { System.err.println(e.getMessage()); 
		}catch(java.security.NoSuchAlgorithmException e) { System.err.println(e.getMessage()); }
	}

	public static void main(String[] args){
		new RSASign(args);
	}
}
