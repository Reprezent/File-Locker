// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 3
//

import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.lang.StackTraceElement;
import java.lang.Thread;

class CommandLineArgParser
{
	public CommandLineArgParser(String[] cmdopts, String vers)
	{
		keyFile = null;
		msgFile = null;
		sigFile = null;

		int i;

		if(cmdopts.length == 0)
		{
			System.err.println("ERROR: Need to specify command line arguments.");
		}

		//parsing list of command options
		for(i = 0; i < cmdopts.length; i++){
			switch(cmdopts[i]){
				case "-k":
					if((i+1) >= cmdopts.length){
						System.err.println("ERROR: No key file provided.");
						System.exit(-1);
						return;
					}

					keyFile = cmdopts[++i];
					break;

				case "-m":
					if((i+1) >= cmdopts.length){
						System.err.println("ERROR: No message file provided.");
						System.exit(-1);
						return;
					}

					msgFile = cmdopts[++i];
					break;

				case "-s":
					if((i+1) >= cmdopts.length){
						System.err.println("ERROR: No signature file provided.");
						System.exit(-1);
						return;
					}

					sigFile = cmdopts[++i];
					break;

                case "-t":
					if((i+1) >= cmdopts.length){
						System.err.println("ERROR: No tag file provided.");
						System.exit(-1);
						return;
					}

					macFile = cmdopts[++i];
					break;

				default:
					System.err.println("Ignoring command line argument " + cmdopts[i]);
					break;
			}
		}


		if((keyFile == null || msgFile == null || sigFile == null) && vers.equals("sign")){
			printSignUsage();
			System.exit(-1);
		}

		if((keyFile == null || msgFile == null || sigFile == null) && vers.equals("validate")){
			printValidateUsage();
			System.exit(-1);
		}
        if((keyFile == null || msgFile == null || macFile == null) && vers.equals("cbcmac"))
        {
            printCBCMacUsage();
            System.exit(-1);
        }
        if((keyFile == null || msgFile == null || macFile == null) && vers.equals("cbcvalidate"))
        {
            printCBCValidateUsage();
            System.exit(-1);
        }
        

	}

	private void printSignUsage(){
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement main = stack[stack.length - 1];
		String mainClass = main.getClassName();
		System.err.print("usage: java " + mainClass);

		System.err.println(" -k <key-file> -m <message-file> -s <signature-file>");
		System.err.println("    -k <key-file>        Valid RSA private key file.");
		System.err.println("    -m <message-file>        File to sign.");
		System.err.println("    -s <signature-file>        File to store RSA signature.");
		System.err.println();
	}


	private void printValidateUsage(){
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement main = stack[stack.length - 1];
		String mainClass = main.getClassName();
		System.err.print("usage: java " + mainClass);

		System.err.println(" -k <key-file> -m <message-file> -s <signature-file>");
		System.err.println("    -k <key-file>        Valid RSA public key file.");
		System.err.println("    -m <message-file>        File to sign.");
		System.err.println("    -s <signature-file>        File to store RSA signature.");
		System.err.println();
	}

	private void printCBCMacUsage(){
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement main = stack[stack.length - 1];
		String mainClass = main.getClassName();
		System.err.print("usage: java " + mainClass);

		System.err.println(" -k <key-file> -m <message-file> -t <tag-file>");
		System.err.println("    -k <key-file>        Valid RSA private key file.");
		System.err.println("    -m <message-file>        File to sign.");
		System.err.println("    -t <tag-file>        File to write CBCMac tag to.");
		System.err.println();
	}

	private void printCBCValidateUsage(){
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		StackTraceElement main = stack[stack.length - 1];
		String mainClass = main.getClassName();
		System.err.print("usage: java " + mainClass);

		System.err.println(" -k <key-file> -m <message-file> -t <tag-file>");
		System.err.println("    -k <key-file>        Valid RSA private key file.");
		System.err.println("    -m <message-file>        File to sign.");
		System.err.println("    -t <tag-file>        File to read CBCMac tag.");
		System.err.println();
	}
	public boolean hasKeyFile()
	{
		return keyFile != null;
	}


	public String getKeyFile()
	{
		return keyFile;
	}

	public boolean hasMsgFile()
	{
		return msgFile != null;
	}

	public String getMsgFile()
	{
		return msgFile;
	}


	public boolean hasSigFile()
	{
		return sigFile != null;
	}


	public String getSigFile()
	{
		return sigFile;
	}


    public boolean hasMacFile()
    {
        return macFile != null;
    }

    public String getMacFile()
    {
        return macFile;
    }

	private String opts;
	private String keyFile;
	private String msgFile;
	private String sigFile;
    private String macFile;
}
