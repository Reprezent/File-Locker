// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 3

class Unlocker {
	public Unlocker(String [] args){
		FileLockerCommandLineArgParser cmd = new FileLockerCommandLineArgParser(args);

		FileLocker unlocker = new FileLocker(cmd.getDirectory(), cmd.getPubKeyFile(), cmd.getPrivKeyFile(), cmd.getValidateKey());
	}

	public static void main(String[] args){
		new Unlocker(args);
	}
}
