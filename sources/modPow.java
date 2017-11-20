// Sean Whalen Alex Riedel JT Liso 
// Programming assignment II CS483/CS583
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.lang.Math;
import java.util.*;

/* IMPORTANT NOTICE ************************
 * I'm not completely sure but I think the static method modPow.compute() is NOT threadsafe
 * it may NOT work with threads / multiprocessing!
 * why: the targets Queue used.. each call needs a "fresh" one
 * if it is altered by another call during the recursion bad stuff happens!
 * thus as long as only one call at a time is made it is safe
 * but otherwise who knows! be warned!
 * ******************************/
class modPow {
	private static Queue<BigInteger> targets;
	
	// computes v ^ e mod N
	// using the powers of two method
	public static BigInteger compute(BigInteger v, BigInteger e, BigInteger N) {
		if (v.equals(BigInteger.ZERO))
			return v;

		targets = new LinkedList<BigInteger>();
		// find which bits are set, set those values as our targets
		BigInteger two = new BigInteger("2");
		for (int i=0; i<e.bitLength(); i++) {
			if (e.testBit(i)) {
				targets.add(two.pow(i));
			}
		}
		if (targets.isEmpty())
			return BigInteger.ONE;
		BigInteger first = v.mod(N);
		BigInteger result = BigInteger.ONE;
		if (targets.peek().equals(BigInteger.ONE)) {
			targets.remove();
			result = first;
		}
		result = result.multiply(fastExponentiation(first, BigInteger.ONE.multiply(two), N));
		return result.mod(N);
	}

  // compute successive values of v ^(2^x) mod N using the prev val
	// ex. v^(2^1) mod N = (v^(2^0) * v^(2^0)) mod N
	// every time we find a value of a power (step) 
	// we need (i.e. is in targets Q) return it times the product of the next call
	private static BigInteger fastExponentiation(BigInteger prev, BigInteger step, BigInteger N) {
		// base case, no more targets
		if (targets.isEmpty()) {
			return BigInteger.ONE;
		}
		BigInteger next = prev.multiply(prev).mod(N);
		if (step.equals(targets.peek())) {
			targets.remove();
			return next.multiply(fastExponentiation(next, step.multiply(new BigInteger("2")), N));
		}
		else {
			return fastExponentiation(next, step.multiply(new BigInteger("2")), N);
		}
	}
}



