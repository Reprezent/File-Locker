// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 1
//

import java.nio.file.Paths;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;
import java.lang.StackTraceElement;
import java.lang.Thread;

class CommandLineArgParser
{
    public CommandLineArgParser(String[] cmdopts)
    {
		keyFile = null;
		inputFile = null;
		outputFile = null;
		ivFile =  null;

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

				case "-i":
					if((i+1) >= cmdopts.length){
						System.err.println("ERROR: No input file provided.");
                        System.exit(-1);
						return;
					}

					inputFile = cmdopts[++i];
					break;
				
				case "-o":
					if((i+1) >= cmdopts.length){
						System.err.println("ERROR: No output file provided.");
                        System.exit(-1);
						return;
					}

					outputFile = cmdopts[++i];
					break;

				case "-v":
					if((i+1) >= cmdopts.length)
                    {
                        System.err.println("ERROR: No IV file provided.");
                        System.exit(-1);
						return;
                    }
					

					ivFile = cmdopts[++i];
					break;

				default:
					System.err.println("Ignoring command line argument " + cmdopts[i]);
					break;
			}
		}

        if(keyFile == null || inputFile == null || outputFile == null)
        {
            printUsage();
            System.exit(-1);
        }

    }
   /* 
    public static String getMainClassName()
    {
        for(final Map.Entry<String, String> entry : System.getenv().entrySet())
            if(entry.getKey().startsWith("JAVA_MAIN_CLASS")) // like JAVA_MAIN_CLASS_13328
                return entry.getValue();
       // throw new IllegalStateException("Cannot determine main class.");

    }
    */
    private void printUsage()
    {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StackTraceElement main = stack[stack.length - 1];
        String mainClass = main.getClassName();
        System.err.print("usage: java " + mainClass);
        System.err.println(" -i <input-file> -o <output-file> -k <key-file> [-v <iv-file>]");
        System.err.println("  options:");
        System.err.println("    -i <input-file>     Specifies an input file to be encrypted/decrypted");
        System.err.println("    -o <output-file>    Specified an output file for the resulting encryption/decryption to be stored.");
        System.err.println("    -k <output-file>    Specifies a 128 bit key stored in hexadecimal form.");
        System.err.println("    -i <iv-file>        Specified a 128 bit IV stored in hexadecimal form.");
        System.err.println();
    }

    public boolean hasKeyFile()
    {
        return keyFile != null;
    }
    

    public Path getKeyFile()
    {
        try
        {
		    return Paths.get(keyFile);
        }
        catch(InvalidPathException e)
        {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public boolean hasInputFile()
    {
		return inputFile != null;
    }

    public Path getInputFile()
    {
        try
        {
		    return Paths.get(inputFile);
        }
        catch(InvalidPathException e)
        {
            System.err.println(e.getMessage());
        }

        return null;
    }


    public boolean hasoutputFile()
    {
		return outputFile != null;
    }


    public Path getOutputFile()
    {
        try
        {
		    return Paths.get(outputFile);
        }
        catch(InvalidPathException e)
        {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public boolean hasIVFile()
    {
		return ivFile != null;
    }

    public Path getIVFile()
    {
        try
        {
		    return Paths.get(ivFile);
        }
        catch(InvalidPathException e)
        {
            System.err.println(e.getMessage());
        }

        return null;
    }



    private String opts;
	private String keyFile;
	private String inputFile;
	private String outputFile;
	private String ivFile;
}
