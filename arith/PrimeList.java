/*
 * $Id$
 */

package edu.jas.arith;

import java.util.Random;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import edu.jas.util.StringUtil;


/**
 * List of big primes.
 * Similar to ALDES/SAC2 SACPOL.PRIME list.
 * @author Heinz Kredel
 * @see ALDES/SAC2 SACPOL.PRIME
 */

public final class PrimeList implements Iterable<java.math.BigInteger> {


    /** The list of probable primes. 
      */
    protected static List<java.math.BigInteger> val;


    //private final static Random random = new Random();


    /**
     * Constructor for PrimeList.
     */
    public PrimeList() {
        this(31);
    }


    /**
     * Constructor for PrimeList with n primes.
     * @param n initial number of primes.
     */
    public PrimeList(int n) {
        if ( val == null ) {
            val = new ArrayList<java.math.BigInteger>((n > 50 ? n: 50));
           // start with some known primes, see knuth (2,390)
           val.add( getLongPrime(63,25) );
           val.add( getLongPrime(63,165) );
           val.add( getLongPrime(63,259) );
           val.add( getLongPrime(63,301) );
           val.add( getLongPrime(63,375) );
           val.add( getLongPrime(63,387) );
           val.add( getLongPrime(63,391) );
           val.add( getLongPrime(63,409) );
           val.add( getLongPrime(63,457) );
           val.add( getLongPrime(63,471) );
           val.add( getLongPrime(60,93) );
           val.add( getLongPrime(60,107) );
           val.add( getLongPrime(60,173) );
           val.add( getLongPrime(60,179) );
           val.add( getLongPrime(60,257) );
           val.add( getLongPrime(60,279) );
           val.add( getLongPrime(60,369) );
           val.add( getLongPrime(60,395) );
           val.add( getLongPrime(60,399) );
           val.add( getLongPrime(60,453) );
           val.add( getLongPrime(59,55) );
           val.add( getLongPrime(59,99) );
           val.add( getLongPrime(59,225) );
           val.add( getLongPrime(59,427) );
           val.add( getLongPrime(59,517) );
           val.add( getLongPrime(59,607) );
           val.add( getLongPrime(59,649) );
           val.add( getLongPrime(59,687) );
           val.add( getLongPrime(59,861) );
           val.add( getLongPrime(59,871) );
           //  val.add( getLongPrime(2,0) );
           int m = val.size();
           java.math.BigInteger start = getLongPrime(63,10000);
           java.math.BigInteger end   = getLongPrime(63,471);
           java.math.BigInteger p = start;
           java.math.BigInteger inc = new java.math.BigInteger(""+10000);
           for ( int i = m; i < n; i++ ) {
               p = p.nextProbablePrime();
               if ( p.compareTo(end) >= 0 ) {
                  end = start;
                  start = start.subtract( inc );
                  p = start;
                  p = p.nextProbablePrime();
               }
               val.add( p );
           }
        }
    }


    /**
     * Method to compute a prime as 2**n - m.
     * @param n power for 2.
     * @param m for 2**n - m.
     */
    protected static java.math.BigInteger getLongPrime(int n, int m) {
       long prime = 2; //2^60-93; // 2^30-35; //19; knuth (2,390)
       for ( int i = 1; i < n; i++ ) {
           prime *= 2;
       }
       prime -= m;
       //System.out.println("p1 = " + prime);
       return new java.math.BigInteger(""+prime);
    }


    /**
     * Check if the list contains really prime numbers.
     */
    protected static boolean checkPrimes() {
        boolean isPrime;
        for ( java.math.BigInteger p : val ) {
            isPrime = p.isProbablePrime(63);
            if ( !isPrime ) {
               System.out.println("not prime = " + p);
               return false;
            }
        }
        return true;
    }


    /**
     * toString.
     */
    public String toString() {
        return val.toString();
    }


    /**
     * Iterator.
     */
    public Iterator<java.math.BigInteger> iterator() {
        return new Iterator<java.math.BigInteger>() {
            int index = -1;
            public boolean hasNext() {
                return true;
            }
            public void remove() {
                throw new RuntimeException("remove not implemented");
            }
            public java.math.BigInteger next() {
                java.math.BigInteger p;
                index++;
                p = null;
                try {
                    p = val.get(index);
                } catch(IndexOutOfBoundsException e) {
                    // ignored;
                }
                if ( p == null ) {
                    p = val.get(0).nextProbablePrime();
                    val.add( 0, p );
                }
                return p;
            }
        }
        ;
    }

}
