/*
 * $Id$
 */

package edu.jas.arith;

import java.util.Random;

public class BigInteger implements Coefficient {

    private final static Random random = new Random();

    public final static BigInteger ZERO = new BigInteger( java.math.BigInteger.ZERO );
    public final static BigInteger ONE = new BigInteger( java.math.BigInteger.ONE );

    private final java.math.BigInteger val;

    public BigInteger(java.math.BigInteger a) {
	val = a;
    }

    public BigInteger(long a) {
	val = new java.math.BigInteger( String.valueOf(a) );
    }

    public BigInteger(String s) {
	val = new java.math.BigInteger( s );
    }

    public BigInteger() {
	val = java.math.BigInteger.ZERO;
    }

    public java.math.BigInteger getval() {
      return val;
    }

    public /*static*/ Coefficient fromInteger(java.math.BigInteger a) {
	return new BigInteger(a);
    }

    public static BigInteger valueOf(java.math.BigInteger a) {
	return new BigInteger(a);
    }

    public /*static*/ Coefficient fromInteger(long a) {
	return new BigInteger(a);
    }

    public static BigInteger valueOf(long a) {
	return new BigInteger(a);
    }

    public boolean isZERO() {
	return val.equals( java.math.BigInteger.ZERO );
    }

    public boolean isONE() {
	return val.equals( java.math.BigInteger.ONE );
    }

    public String toString() {
	return val.toString();
    }

    public int compareTo(Object b) {
        if ( ! (b instanceof BigInteger) ) {
	   System.err.println("BigInteger.compareTo not a BigInteger");
           return Integer.MAX_VALUE;
        }
	return val.compareTo( ((BigInteger)b).getval() );
    }

    public static int ICOMP(BigInteger A, BigInteger B) {
      if ( A == null ) return -B.signum();
      return A.compareTo(B);
    }

    public boolean equals(Object b) {
	if ( ! ( b instanceof BigInteger ) ) return false;
	return val.equals( ((BigInteger)b).getval() );
    }

    public Coefficient abs() {
      return new BigInteger( val.abs() );
    }

    public static BigInteger IABS(BigInteger A) {
      if ( A == null ) return null;
      return (BigInteger) A.abs();
    }

    public Coefficient negate() {
      return new BigInteger( val.negate() );
    }

    public static BigInteger INEG(BigInteger A) {
      if ( A == null ) return null;
      return (BigInteger) A.negate();
    }

    public int signum() {
      return val.signum();
    }

    public static int ISIGN(BigInteger A) {
      if ( A == null ) return 0;
      return A.signum();
    }

    public Coefficient subtract(Coefficient S) {
      return new BigInteger( val.subtract( ((BigInteger)S).getval() ) );
    }

    public static BigInteger IDIF(BigInteger A, BigInteger B) {
      if ( A == null ) return (BigInteger) B.negate();
      return (BigInteger) A.subtract(B);
    }

    public Coefficient divide(Coefficient S) {
      return new BigInteger( val.divide( ((BigInteger)S).getval() ) );
    }

    public static BigInteger IQ(BigInteger A, BigInteger B) {
      if ( A == null ) return null;
      return (BigInteger) A.divide(B);
    }

    public Coefficient remainder(Coefficient S) {
      return new BigInteger( val.remainder( ((BigInteger)S).getval() ) );
    }

    public static BigInteger IREM(BigInteger A, BigInteger B) {
      if ( A == null ) return null;
      return (BigInteger) A.remainder(B);
    }

    public Coefficient[] divideAndRemainder(Coefficient S) {
      Coefficient[] qr = new BigInteger[2];
      java.math.BigInteger[] C = val.divideAndRemainder( 
                                     ((BigInteger)S).getval() );
      qr[0] = new BigInteger( C[0] );
      qr[1] = new BigInteger( C[1] );
      return qr;
    }

    /**
    Integer quotient and remainder.  A and B are integers, B ne 0.  Q is
    the quotient, integral part of A/B, and R is the remainder A-B*Q.
    */

    public static BigInteger[] IQR(BigInteger A, BigInteger B) {
      if ( A == null ) return null;
      return (BigInteger[]) A.divideAndRemainder(B);
    }

    public Coefficient gcd(Coefficient S) {
      return new BigInteger( val.gcd( ((BigInteger)S).getval() ) );
    }

    public static BigInteger IGCD(BigInteger A, BigInteger B) {
      if ( A == null ) return null;
      return (BigInteger) A.gcd(B);
    }

    public Coefficient random(int n) {
      return IRAND( n );
    }

    public static BigInteger IRAND(int NL) {
      return new BigInteger( new java.math.BigInteger( NL, random ) );
    }

    public Coefficient multiply(Coefficient S) {
      return new BigInteger( val.multiply( ((BigInteger)S).getval() ) );
    }

    public static BigInteger IPROD(BigInteger A, BigInteger B) {
      if ( A == null ) return null;
      return (BigInteger) A.multiply(B);
    }

    public Coefficient add(Coefficient S) {
      return new BigInteger( val.add( ((BigInteger)S).getval() ) );
    }

    public static BigInteger ISUM(BigInteger A, BigInteger B) {
      if ( A == null ) return null;
      return (BigInteger) A.add(B);
    }


    // the mod methods do not belong here -------------------

    public Coefficient modInverse(Coefficient M) {
      return new BigInteger( val.modInverse( ((BigInteger)M).getval() ) );
    }

    // beware of parameter order
    public static BigInteger MIINV(BigInteger M, BigInteger A) {
      if ( A == null ) return null;
      return (BigInteger) A.modInverse(M);
    }

    public Coefficient mod(Coefficient M) {
      return new BigInteger( val.mod( ((BigInteger)M).getval() ) );
    }

    // beware of parameter order
    public static BigInteger MIHOM(BigInteger M, BigInteger A) {
      if ( A == null ) return null;
      return (BigInteger) A.mod(M);
    }

}
