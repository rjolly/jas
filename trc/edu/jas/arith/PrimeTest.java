/*
 * $Id$
 */

package edu.jas.arith;


import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import edu.jas.kern.PrettyPrint;
import edu.jas.structure.NotInvertibleException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * PrimeInteger and PrimeList tests with JUnit.
 * @author Heinz Kredel.
 */

public class PrimeTest extends TestCase {


    /**
     * main.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }


    /**
     * Constructs a <CODE>PrimeTest</CODE> object.
     * @param name String
     */
    public PrimeTest(String name) {
        super(name);
    }


    /**
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(PrimeTest.class);
        return suite;
    }


    @Override
    protected void setUp() {
    }


    @Override
    protected void tearDown() {
    }


    /**
     * Test prime list.
     */
    public void testPrime() {
        PrimeList primes = new PrimeList();
        //System.out.println("primes = " + primes);
        //assertTrue("all primes ", primes.checkPrimes() );

        int i = 0;
        //System.out.println("primes = ");
        for (java.math.BigInteger p : primes) {
            assertFalse("p != null", p == null);
            //System.out.print("" + p);
            if (i++ > 50) {
                break;
            }
            //System.out.print(", ");
        }
        //System.out.println();
        //System.out.println("primes = " + primes);
        assertTrue("all primes ", primes.checkPrimes());
    }


    /**
     * Test small prime list.
     */
    public void testSmallPrime() {
        List<Long> sp = PrimeInteger.smallPrimes(1, 500);
        //System.out.println("sp = " + sp);
        for (Long p : sp) {
	    java.math.BigInteger P = new java.math.BigInteger(p.toString());
            assertTrue("isPrime: " + p, P.isProbablePrime(16) );
        }
        PrimeList primes = new PrimeList(PrimeList.Range.small);
        //System.out.println("primes = " + primes);
        int i = primes.size() - 1;
        //System.out.println("primes = ");
        for (java.math.BigInteger p : primes) {
            assertTrue("p.isPrime: ", PrimeInteger.isPrime(p.longValue()) );
            //System.out.print(i + " = " + p + ", ");
            if (i-- <= 0) {
                break;
            }
        }
    }


    /**
     * Test low prime list.
     */
    public void testLowPrime() {
        PrimeList primes = new PrimeList(PrimeList.Range.low);
        //System.out.println("primes = " + primes);
        int i = primes.size() - 1;
        //System.out.println("primes = ");
        for (java.math.BigInteger p : primes) {
            assertTrue("p.isPrime: ", PrimeInteger.isPrime(p.longValue()) );
            //System.out.print(i + " = " + p + ", ");
            if (i-- <= 0) {
                break;
            }
        }
    }


    /**
     * Test medium prime list.
     */
    public void testMediumPrime() {
        PrimeList primes = new PrimeList(PrimeList.Range.medium);
        //System.out.println("primes = " + primes);
        int i = primes.size() - 1;
        //System.out.println("primes = ");
        for (java.math.BigInteger p : primes) {
            //System.out.println(i + " = " + p + ", ");
            assertTrue("p.isPrime: ", PrimeInteger.isPrime(p.longValue()) );
            if (i-- <= 0) {
                break;
            }
        }
    }


    /**
     * Test xlarge prime list.
     */
    public void ytestLargePrime() {
        PrimeList primes = new PrimeList(PrimeList.Range.large);
        //System.out.println("primes = " + primes);
        int i = primes.size() - 1;
        //System.out.println("primes = ");
        for (java.math.BigInteger p : primes) {
            System.out.println(i + " = " + p + ", ");
            long pl = p.longValue();
            if (pl < PrimeInteger.BETA) {
                assertTrue("p.isPrime: ", PrimeInteger.isPrime(pl) );
            }
            if (i-- <= 0) {
                break;
            }
        }
    }


    /**
     * Test Mersenne prime list.
     */
    public void ytestMersennePrime() {
        PrimeList primes = new PrimeList(PrimeList.Range.mersenne);
        //System.out.println("primes = " + primes);
        //assertTrue("all primes ", primes.checkPrimes() );
        int i = primes.size() - 1;
        //System.out.println("primes = ");
        for (java.math.BigInteger p : primes) {
            //System.out.println(i + " = " + p + ", ");
            long pl = p.longValue();
            if (pl < PrimeInteger.BETA/1000L) {
                assertTrue("p.isPrime: ", PrimeInteger.isPrime(pl) );
            }
            if (i-- <= 0) {
                break;
            }
        }
        assertTrue("all primes ", primes.checkPrimes(15));
    }


    /**
     * Test factorize integer.
     */
    public void testFactorInteger() {
        SortedMap<Long, Integer> ff;

        ff = PrimeInteger.IFACT(2 * 3 * 5 * 7 * 2 * 9 * 10 * 19 * 811);
        //System.out.println("ff = " + ff);
        assertEquals("factors: ", ff.size(), 6 );
        for (Long p : ff.keySet()) {
	    java.math.BigInteger P = new java.math.BigInteger(p.toString());
            assertTrue("isPrime: " + p, P.isProbablePrime(16) );
        }

        ff = PrimeInteger.IFACT(991 * 997 * 811 + 1);
        //System.out.println("ff = " + ff);
        assertEquals("factors: ", ff.size(), 3 );
        for (Long p : ff.keySet()) {
	    java.math.BigInteger P = new java.math.BigInteger(p.toString());
            assertTrue("isPrime: " + p, P.isProbablePrime(16) );
        }

        ff = PrimeInteger.IFACT( PrimeList.getLongPrime(15, 135).longValue());
        //System.out.println("ff = " + ff);
        assertEquals("factors: ", ff.size(), 1 );
        for (Long p : ff.keySet()) {
	    java.math.BigInteger P = new java.math.BigInteger(p.toString());
            assertTrue("isPrime: " + p, P.isProbablePrime(16) );
        }

        //ff = PrimeInteger.IFACT( PrimeList.getLongPrime(61, 1).longValue() );
        //ff = PrimeInteger.IFACT( PrimeList.getLongPrime(60, 93).longValue() );
        //ff = PrimeInteger.IFACT( PrimeList.getLongPrime(59, 55).longValue() );
        ff = PrimeInteger.IFACT( PrimeList.getLongPrime(59, 0).longValue() );
        //System.out.println("m = " + PrimeList.getLongPrime(59, 0).longValue());
        //System.out.println("ff = " + ff);
        assertEquals("factors: ", ff.size(), 1 );
        for (Long p : ff.keySet()) {
	    java.math.BigInteger P = new java.math.BigInteger(p.toString());
            assertTrue("isPrime: " + p, P.isProbablePrime(16) );
        }
        //System.out.println("SMPRM = " + PrimeInteger.SMPRM);
    }


    /**
     * Test random integers.
     */
    public void testRandom() {
        SortedMap<Long, Integer> ff;
        BigInteger rnd = BigInteger.ONE;
        //System.out.println("beta = " + PrimeInteger.BETA);

        for (int i = 0; i < 5; i++) {
            BigInteger M = rnd.random(60).abs();
            //System.out.println("M = " + M);
            long m = Math.abs(M.getVal().longValue());
            //System.out.println("M = " + M + ", m = " + m);
            if (m < PrimeInteger.BETA) {
               ff = PrimeInteger.IFACT(m);
               //System.out.println("ff = " + ff);
               assertTrue("isFactorization: " + m + ", ff = " + ff, PrimeInteger.isFactorization(m, ff));
            }
	}
    }


    /**
     * Test random integers to the power of 3.
     */
    public void testRandom3() {
        SortedMap<Long, Integer> ff;
        BigInteger rnd = BigInteger.ONE;
        //System.out.println("beta = " + PrimeInteger.BETA);
        //System.out.println("beta**-2 = " + Roots.sqrtInt(new BigInteger(PrimeInteger.BETA)));
        //System.out.println("beta**-3 = " + Roots.root(new BigInteger(PrimeInteger.BETA), 3));

        for (int i = 0; i < 5; i++) {
            BigInteger M = rnd.random(20).abs();
            M = M.power(3);
            //System.out.println("M = " + M);
            long m = Math.abs(M.getVal().longValue());
            //System.out.println("M = " + M + ", m = " + m);
            if (m < PrimeInteger.BETA) {
               ff = PrimeInteger.IFACT(m);
               //System.out.println("ff = " + ff);
               assertTrue("isFactorization: " + m + ", ff = " + ff, PrimeInteger.isPrimeFactorization(m, ff));
            }
	}
    }

}
