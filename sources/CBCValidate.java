// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 3

import java.nio.file.Files;
import java.io.IOException;
import java.lang.UnsupportedOperationException;
import java.lang.SecurityException;
import java.util.Arrays;
import java.nio.file.Paths;
import java.lang.Boolean;

class CBCValidate
{
    public CBCValidate(String[] args)
    {
        
        CommandLineArgParser cmd_args = new CommandLineArgParser(args, "cbcvalidate");
        byte[] data = null, key = null, output = null, mac = null;
        try
        {
            data = Files.readAllBytes(Paths.get(cmd_args.getMsgFile()));
            System.err.println("Data length: "+ Integer.toString(data.length));
            key = utils.hexStringToBinary(Files.readAllBytes(Paths.get(cmd_args.getKeyFile())));
            mac = Files.readAllBytes(Paths.get(cmd_args.getMacFile()));
        }
        catch(IOException e) { System.err.println(e.getMessage()); }
        
        output = CBC.validate(data, key, mac);
        boolean rv = Arrays.equals(output, mac);
        System.out.println(((rv == true) ? "True" : "False"));
    }




    public static void main(String[] args)
    {
        new CBCValidate(args);
    }
}

