
/*
 * $Id$
 */

package edu.jas.ufd;


import org.apache.log4j.Logger;

import edu.jas.structure.GcdRingElem;
import edu.jas.poly.GenPolynomial;
import edu.jas.poly.ExpVector;
import edu.jas.poly.PolyUtil;


/**
 * Greatest common divisor algorithms with primitive polynomial remainder sequence.
 * @author Heinz Kredel
 */

public class GreatestCommonDivisorPrimitive<C extends GcdRingElem<C> > 
       extends GreatestCommonDivisorAbstract<C> {


    private static final Logger logger = Logger.getLogger(GreatestCommonDivisorPrimitive.class);
    private boolean debug = logger.isDebugEnabled();


    /**
     * Univariate GenPolynomial greatest comon divisor.
     * Uses pseudoRemainder for remainder.
     * @param P univariate GenPolynomial.
     * @param S univariate GenPolynomial.
     * @return gcd(P,S).
     */
    public GenPolynomial<C> baseGcd( GenPolynomial<C> P,
                                     GenPolynomial<C> S ) {
        if ( S == null || S.isZERO() ) {
            return P;
        }
        if ( P == null || P.isZERO() ) {
            return S;
        }
        if ( P.ring.nvar > 1 ) {
           throw new RuntimeException(this.getClass().getName()
                                       + " no univariate polynomial");
           //logger.info("pseudoGcd only for univaraite polynomials");
           // guess
           //return P.ring.getONE();
        }
        long e = P.degree(0);
        long f = S.degree(0);
        GenPolynomial<C> q;
        GenPolynomial<C> r;
        if ( f > e ) {
           r = P;
           q = S;
           long g = f;
           f = e;
           e = g;
        } else {
           q = P;
           r = S;
        }
        r = r.abs();
        q = q.abs();
        C a = baseContent( r );
        C b = baseContent( q );
        C c = gcd(a,b);  // indirection
        r = divide(r,a); // indirection
        q = divide(q,b); // indirection
        if ( r.isONE() ) {
           return r.multiply(c);
        }
        if ( q.isONE() ) {
           return q.multiply(c);
        }
        GenPolynomial<C> x;
        while ( !r.isZERO() ) {
            x = PolyUtil.<C>basePseudoRemainder(q,r);
            //System.out.println("x  = " + x);
            q = r;
            r = basePrimitivePart( x );
        }
        //System.out.println("q  = " + q);
        return (q.multiply(c)).abs(); 
    }


    /**
     * Univariate GenPolynomial recursive greatest comon divisor.
     * Uses pseudoRemainder for remainder.
     * @param P univariate recursive GenPolynomial.
     * @param S univariate recursive GenPolynomial.
     * @return gcd(P,S).
     */
    public GenPolynomial<GenPolynomial<C>> 
        recursiveGcd( GenPolynomial<GenPolynomial<C>> P,
                      GenPolynomial<GenPolynomial<C>> S ) {
        if ( S == null || S.isZERO() ) {
            return P;
        }
        if ( P == null || P.isZERO() ) {
            return S;
        }
        if ( P.ring.nvar > 1 ) {
           throw new RuntimeException(this.getClass().getName()
                                       + " no univariate polynomial");
        }
        long e = P.degree(0);
        long f = S.degree(0);
        GenPolynomial<GenPolynomial<C>> q;
        GenPolynomial<GenPolynomial<C>> r;
        if ( f > e ) {
           r = P;
           q = S;
           long g = f;
           f = e;
           e = g;
        } else {
           q = P;
           r = S;
        }
        r = r.abs();
        q = q.abs();
        //System.out.println("rgcd r = " + r);
        //System.out.println("rgcd q = " + q);
        GenPolynomial<C> a = recursiveContent(r);
        GenPolynomial<C> b = recursiveContent(q);
        //System.out.println("rgcd a = " + a);
        //System.out.println("rgcd b = " + b);

        GenPolynomial<C> c = gcd(a,b); // go to recursion
        //System.out.println("rgcd c = " + c);
        r = PolyUtil.<C>recursiveDivide(r,a);
        q = PolyUtil.<C>recursiveDivide(q,b);
        if ( r.isONE() ) {
           return r.multiply(c);
        }
        if ( q.isONE() ) {
           return q.multiply(c);
        }
        //System.out.println("rgcd q = " + q);
        //System.out.println("rgcd r = " + r);
        if ( debug && ( q.isConstant() || r.isConstant() ) ) {
           System.out.println("rgcd q = " + q);
           System.out.println("rgcd r = " + r);
           throw new RuntimeException(this.getClass().getName()
                                       + " error in recursive Content");
        }

        GenPolynomial<GenPolynomial<C>> x;
        while ( !r.isZERO() ) {
            x = PolyUtil.<C>recursivePseudoRemainder(q,r);
            //System.out.println("rgcd x = " + x);
            //if ( !x.isZERO() && x.isConstant() ) {
            //   System.out.println("rpg x = is constant " + x);
            //}
            q = r;
            r = recursivePrimitivePart( x );
            //System.out.println("rgcd r = " + r);
        }
        //System.out.println("sign q = " + q.signum());
        return q.abs().multiply(c); //.abs();
    }

}
