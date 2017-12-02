// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 3


import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.lang.UnsupportedOperationException;
import java.lang.SecurityException;

class CBCMac
{
    public CBCMac(String[] args)
    {
        CommandLineArgParser cmd_args = new CommandLineArgParser(args, "cbcmac");
        byte[] data = null, key = null, output = null;
        try
        {
            data = Files.readAllBytes(Paths.get(cmd_args.getMsgFile()));
            System.err.println("Data length: "+ Integer.toString(data.length));
            key = utils.hexStringToBinary(Files.readAllBytes(Paths.get(cmd_args.getKeyFile())));
        }
        catch(IOException e) { System.err.println(e.getMessage()); }
        
        output = CBC.mac(data, key);



        try
        {
            Files.write(Paths.get(cmd_args.getMacFile()), output);
        }
        catch(IOException e)                   { System.err.println(e.getMessage()); }
        catch(UnsupportedOperationException e) { System.err.println(e.getMessage()); }
        catch(SecurityException e)             { System.err.println(e.getMessage()); }
    }

    public static void main(String[] args)
    {
        new CBCMac(args);
    }
}
