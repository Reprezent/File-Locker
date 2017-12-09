// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 3

import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.lang.StackTraceElement;
import java.lang.Thread;

class FileLockerCommandLineArgParser
{
	public FileLockerCommandLineArgParser(String[] cmdopts)
	{
		directory = null;
		pubKeyFile = null;
		privKeyFile = null;
		validateKey = null;

		int i;

		if(cmdopts.length == 0)
		{
			System.err.println("ERROR: Need to specify command line arguments.");
			printLockerUsage();
		}

		//parsing list of command options
		for(i = 0; i < cmdopts.length; i++){
			switch(cmdopts[i]){
				case "-d":
					if((i+1) >= cmdopts.length){
						System.err.println("ERROR: No directory provided.");
						System.exit(-1);
						return;
					}

					directory = cmdopts[++i];
					break;

				case "-p":
					if((i+1) >= cmdopts.length){
						System.err.println("ERROR: No public file provided.");
						System.exit(-1);
						return;
					}

					pubKeyFile = cmdopts[++i];
					break;

				case "-r":
					if((i+1) >= cmdopts.length){
						System.err.println("ERROR: No private key file provided.");
						System.exit(-1);
						return;
					}

					privKeyFile = cmdopts[++i];
					break;

                case "-vk":
					if((i+1) >= cmdopts.length){
						System.err.println("ERROR: No validating public key file provided.");
						System.exit(-1);
						return;
					}

					validateKey = cmdopts[++i];
					break;

				default:
					System.err.println("Ignoring command line argument " + cmdopts[i]);
					break;
			}
		}


        if(pubKeyFile == null || privKeyFile == null || directory == null || validateKey == null)
        {
            printLockerUsage();
            System.exit(-1);
        }
        

	}

	private void printLockerUsage(){
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement main = stack[stack.length - 1];
		String mainClass = main.getClassName();
		System.err.print("usage: java " + mainClass);

		System.err.println(" -d <directory> -p <public-key> -r <private-key> -vk <validating-public-key>");
		System.err.println("    -d <directory>                     Directory to lock or unlock");
		System.err.println("    -p <action public key>             Public RSA Key");
		System.err.println("    -r <action private key>            Private RSA Key");
		System.err.println("    -vk <validating public key>        RSA key to validatie public RSA Key");
		System.err.println();
        System.exit(1);
	}

	public boolean hasDirectory()
	{
		return directory != null;
	}


	public String getDirectory()
	{
		return directory;
	}

	public boolean hasPubKeyFile()
	{
		return pubKeyFile != null;
	}

	public String getPubKeyFile()
	{
		return pubKeyFile;
	}


	public boolean hasPrivKeyFile()
	{
		return privKeyFile != null;
	}


	public String getPrivKeyFile()
	{
		return privKeyFile;
	}

	public boolean hasValidateKey(){
		return validateKey != null;
	}

	public String getValidateKey(){
		return validateKey;
	}


	private String directory;
	private String pubKeyFile;
	private String privKeyFile;
	private String validateKey;
}
