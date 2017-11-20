import java.nio.ByteBuffer;
import java.util.Arrays;
class utils
{
    public static byte[] hexStringToBinary(byte[] hex_string)
    {
        String s = new String(hex_string);
        byte[] rv = new byte[s.length() / 2];
        for(int i = 0; i < s.length(); i += 2)
        {
            rv[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return rv;
    }
	
	//need to overflow check cases where every byte of iv is maxed out (adding 1 does what)
	//modified from: https://stackoverflow.com/questions/32253298/adding-1-to-binary-byte-array
	public static byte[] addOne(byte[] A) throws Exception {
        for (int i = A.length - 1; i >= 0; i--) {
            if (A[i] == 127) {
				A[i] = 0;
				continue;
			}
			if (i == 0) {
                throw new Exception("Overflow");
            }
			A[i] += 1;
			return A;
            
        }
        return A;
    }
	
	public static int intValue(byte[] A) {
		if (A.length < 4)
			return 0;
		ByteBuffer wrapper = ByteBuffer.wrap(Arrays.copyOfRange(A,A.length-4,A.length));
		return wrapper.getInt();
	}
	
	public static void printByteArr(byte[] A) {
		for (int i = 0; i<A.length; i++) {
			System.out.format("%d ", A[i]);
		}
		System.out.format("\n");
	}
}
