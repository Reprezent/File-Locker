// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 1
//

import java.nio.file.Files;
import java.io.IOException;
import java.lang.UnsupportedOperationException;
import java.lang.SecurityException;

class CBCDec
{
    public CBCDec(String[] args)
    {
        CommandLineArgParser cmd_args = new CommandLineArgParser(args);
        byte[] data = null, key = null, output = null;
        try
        {
            data = Files.readAllBytes(cmd_args.getInputFile());
            key = utils.hexStringToBinary(Files.readAllBytes(cmd_args.getKeyFile()));
        }
        catch(IOException e) { System.err.println(e.getMessage()); }
        
        output = CBC.decrypt(data, key);

        try
        {
            Files.write(cmd_args.getOutputFile(), output);
        }
        catch(IOException e)                   { System.err.println(e.getMessage()); }
        catch(UnsupportedOperationException e) { System.err.println(e.getMessage()); }
        catch(SecurityException e)             { System.err.println(e.getMessage()); }

    }

    public static void main(String[] args)
    {
        new CBCDec(args);
    }
}
