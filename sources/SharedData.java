

public class SharedData 
{
	byte[] output;

	public SharedData(int sz)
	{
		this.output = new byte[sz];
	}

	public synchronized void insertBlock(int pos, byte[] block)
	{
		int i = 0;
		try {
			//System.err.println(pos);
			for (i = 0; i < block.length; i++) {
				output[pos+i] = block[i];
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.err.println(e.getMessage());
			System.err.println("i: " + i + " pos: " + pos);
		}
	}
	
	public byte[] getOutput() 
	{
		return output;
	}
}
