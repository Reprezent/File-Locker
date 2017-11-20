// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 1

class CryptoDriver
{


	public static void main(String[] args){
		CommandLineArgParser parser = new CommandLineArgParser(args);
		System.out.println(parser.getInputFile());
	}
}
