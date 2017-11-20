import java.math.BigInteger;

class testPrimes
{

    public static void main(String[] args)
    {
        BigInteger a;
        for(int i = 2; i < 1024; i++)
        {
            a = RandomGen.Generate(i);
            System.out.printf("Bit Length: %d%n", i);
            System.out.printf("%s%n", a.toString());
            System.out.printf("Is Prime (Java): %b%n", a.isProbablePrime(100));
        }
    }
}
