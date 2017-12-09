// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 3

class Locker {
	public Locker(String [] args){
		FileLockerCommandLineArgParser cmd = new FileLockerCommandLineArgParser(args);

		FileLocker locker = new FileLocker(cmd.getDirectory(), cmd.getPubKeyFile(), cmd.getPrivKeyFile(), cmd.getValidateKey());
        locker.lock();
	}

	public static void main(String[] args){
		new Locker(args);
	}
}
